package hello.example_board.service;

import hello.example_board.domain.Board;
import hello.example_board.repository.BoardRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BoardServiceIntegrationTest {

    @Autowired BoardService boardService;
    @Autowired BoardRepository boardRepository;

    @Test
    @DisplayName("게시글 등록 및 조회")
    void saveAndFind() {
        // given
        Board board = new Board("테스트 제목", "테스트 내용", "작성자");

        // when
        Long saveId = boardService.register(board);

        // then
        Board findBoard = boardService.findOne(saveId);
        assertThat(findBoard.getTitle()).isEqualTo("테스트 제목");
        assertThat(findBoard.getContent()).isEqualTo("테스트 내용");

        // 조회수 로직 검증
        assertThat(findBoard.getViewCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("전체 조회")
    void FindAll() {
        // given
        Board board1 = new Board("제목1", "내용1", "작성자1");
        Board board2 = new Board("제목2", "내용2", "작성자2");
        boardService.register(board1);
        boardService.register(board2);

        // when
        List<Board> boards = boardService.findBoards();

        // then
        Assertions.assertThat(boards.size()).isGreaterThanOrEqualTo(4);
    }

}