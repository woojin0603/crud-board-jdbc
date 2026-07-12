package hello.example_board.web;

import hello.example_board.domain.Board;
import hello.example_board.service.BoardService;
import org.h2.engine.Mode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.List;

@Controller
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    /*
     * 1. 게시글 목록 조회
     * */
    @GetMapping
    public String list(Model model) {
        List<Board> boards = boardService.findBoards();
        model.addAttribute("boards", boards);
        return "boards/list";
    }

    /*
     * 2. 게시글 상세 조회
     * */
    @GetMapping("/{boardId}")
    public String detail(@PathVariable Long boardId, Model model) {
        Board board = boardService.findOne(boardId);
        model.addAttribute("board", board);
        return "boards/detail";
    }

    /*
     * 3. 게시글 등록 폼 이동
     * */
    @GetMapping("/add")
    public String addForm() {
        return "boards/addForm";
    }

    /*
     * 4. 게시글 실제 등록(POST)
     * */
    @PostMapping("/add")
    public String add(@ModelAttribute Board board) {
        boardService.register(board);
        return "redirect:/boards";
    }

    /*
     * 5. 게시글 수정 폼 이동
     **/
    @GetMapping("/{boardId}/edit")
    public String editForm(@PathVariable Long boardId, Model model) {
        Board board = boardService.findOne(boardId);
        model.addAttribute("board", board);
        return "boards/editForm";
    }

    /*
     * 6. 게시글 수정(POST)
     * */
    @PostMapping("/{boardId}/edit")
    public String edit(@PathVariable Long boardId, @ModelAttribute Board updateParam) {
        boardService.updateBoard(boardId, updateParam.getTitle(), updateParam.getContent());
        return "redirect:/boards/{boardId}";
    }

    /*
     * 7. 게시글 삭제(GET)
     * */
    @GetMapping("/{boardId}/delete")
    public String delete(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);
        return "redirect:/boards";
    }
}
