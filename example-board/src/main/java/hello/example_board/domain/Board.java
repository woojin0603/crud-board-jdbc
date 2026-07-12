package hello.example_board.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Board {

    private Long id;                // 게시글 식별자
    private String title;           // 제목
    private String content;         // 내용
    private String writer;          // 작성자
    private LocalDateTime regDate;  // 등록일
    private int viewCount;          // 조회수

    // 기본 생성자(스프링, 라이브러리가 객체 생성 시 필요함.)
    public Board() {
    }

    // 데이터를 처음 등록할 때 사용하는 생성자(나머지는 DB, Service에서 세팅함)
    public Board(String title, String content, String writer) {
        this.title = title;
        this.content = content;
        this.writer = writer;
    }
}
