package hello.example_board.service;

import hello.example_board.domain.Board;
import hello.example_board.repository.BoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    /*
     * 게시글 등록
     * */
    @Transactional
    public Long register(Board board) {
        boardRepository.save(board);
        return board.getId();
    }

    /*
     * 전체 게시글 조회
     * */
    public List<Board> findBoards() {
        return boardRepository.findAll();
    }

    /*
     * 게시글 하나 조회
     * */
    @Transactional
    public Board findOne(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다." + boardId));

        // 비즈니스 로직: 게시글 읽을때마다 조회수 증가
        board.setViewCount(board.getViewCount() + 1);
        boardRepository.update(boardId, board);

        return board;
    }

    /*
     * 게시글 삭제
     * */
    @Transactional
    public void deleteBoard(Long boardId) {
        boardRepository.delete(boardId);
    }

    /*
     * 게시글 업데이트
     * */
    @Transactional
    public void updateBoard(Long boardId, String title, String content) {
        // 1. 수정할 게시글이 있는지 먼저 DB에서 조회
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다." + boardId));

        // 2. 파라미터로 넘어온 값으로 데이터 변환
        board.setTitle(title);
        board.setContent(content);

        // 3. Repository의 update를 호출해서 DB에 반영
        boardRepository.update(boardId, board);
    }
}
