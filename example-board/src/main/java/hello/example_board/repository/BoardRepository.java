package hello.example_board.repository;

import hello.example_board.domain.Board;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {

    // 게시글 저장 기능
    Board save(Board board);

    // 게시글 조회(ID로 조회)
    Optional<Board> findById(Long id);

    // 전체 게시글 조회
    List<Board> findAll();

    // 게시글 수정
    void update(Long id, Board updateParam);

    // 게시글 삭제
    void delete(Long id);
}
