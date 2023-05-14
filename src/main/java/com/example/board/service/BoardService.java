package com.example.board.service;

import com.example.board.domain.Board;
import com.example.board.domain.BoardDTO;
import com.example.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    @Autowired
    private final BoardRepository boardRepository;
//    public void register(Board board){
//        boardRepository.save(board);
//    }
//    public List<Board> list(){
//        return boardRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
//    }
//
//    public Board detail(Long id){
//        return boardRepository.findById(id).orElse(null);
//    }

    public Set<Board> getRelatedBoards(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시물이 존재하지 않습니다."));
        Set<Board> relatedBoards = board.getRelatedBoards();
        relatedBoards.remove(board);
        return relatedBoards;
    }

    public Board getBoard(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시물이 존재하지 않습니다."));
    }



    // 새 게시글을 만들고, 연관 게시글과 찾아 연결
    @Transactional
    public Board createdBoard(Board board){
        board.setCreatedDate(LocalDateTime.now());
        Board savedBoard = boardRepository.save(board);
        List<Board> relatedBoards = findRelatedBoards(board, getAllBoardsExcept(board));
        relatedBoards.forEach(savedBoard::addRelatedBoard);

        return savedBoard;
    }

    // 모든 게시글 목록을 가져와 BoardDTO 객체로 변환
    public List<BoardDTO> getBoardList(){
        return boardRepository.findAll()
                .stream()
                .map(BoardDTO::new)
                .collect(Collectors.toList());
    }

    // 파라미터로 전달된 게시글을 제외한 모든 게시글 목록을 가져옴
    private List<Board> getAllBoardsExcept(Board board){
        return boardRepository.findAll().stream().filter(b -> !b.equals(board)).collect(Collectors.toList());
    }

    // 파라미터로 전달된 게시글과 연관된 게시글을 찾아서 리스트로 반환
    private List<Board> findRelatedBoards(Board board, List<Board> allBoards){
        Map<String, Integer> wordCountMap = getWordCountMap(board.getContent());
        List<String> ignoredWords = getIgnoredWords(wordCountMap, allBoards.size());

        Map<Board, Integer> relatedBoardCountMap = new HashMap<>();
        for(Board otherBoard : allBoards){
            int count = 0;
            Map<String, Integer> otherWordCountMap = getWordCountMap(otherBoard.getContent());
            for(String word : wordCountMap.keySet()){
                if(ignoredWords.contains(word)){
                    continue;
                }
                if(otherWordCountMap.containsKey(word)){
                    count += Math.min(wordCountMap.get(word), otherWordCountMap.get(word));
                }
            }
            if(count > 0 && count <= Math.min(wordCountMap.size(), otherWordCountMap.size()) / 2){
                relatedBoardCountMap.put(otherBoard, count);
            }
        }
    List<Board> relatedBoards = new ArrayList<>(relatedBoardCountMap.keySet());
        relatedBoards.sort((b1, b2) -> relatedBoardCountMap.get(b2) - relatedBoardCountMap.get(b1));
    return relatedBoards;
    }

    // 60% 이상의 게시글에서 나타난 단어를 배제하기 위한 단어 리스트 반환
    private List<String> getIgnoredWords(Map<String, Integer> wordCountMap, int numOfBoards){
        List<String> ignoredWords = new ArrayList<>();
        for(String word : wordCountMap.keySet()){
            int count = wordCountMap.get(word);
            if(count > 0.6 * numOfBoards){
                ignoredWords.add(word);
            }
        }
        return ignoredWords;
    }

    // 게시글 내용에서 각 단어의 출현빈도를 카운트한 맵을 반환
    private Map<String, Integer> getWordCountMap(String content){
        String[] words = content.split("\\s+");
        Map<String, Integer> wordCountMap = new HashMap<>();
        for(String word : words){
            if(word.length() < 2) {
                continue;
            }
            wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
        }
        return wordCountMap;
    }

    // 두 개시글이 연관되었는지 확인
    private boolean isRelated(Map<String, Integer> wordCountMap1, Map<String, Integer> wordCountMap2,
                              List<String> ignoredWords){
        int threshold = (int) (0.4 * Math.min(wordCountMap1.size(), wordCountMap2.size()));
        int commonCount = 0;
        for (String word : wordCountMap1.keySet()) {
            if (ignoredWords.contains(word)) {
                continue;
            }
            if (wordCountMap2.containsKey(word)) {
                commonCount++;
                if (commonCount >= threshold) {
                    return true;
                }
            }
        }
        return false;
    }
}
