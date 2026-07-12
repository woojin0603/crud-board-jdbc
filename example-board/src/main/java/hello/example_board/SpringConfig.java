package hello.example_board;

import hello.example_board.repository.BoardRepository;
import hello.example_board.repository.JdbcBoardRepository;
import hello.example_board.service.BoardService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class SpringConfig {

    private final DataSource dataSource;

    public SpringConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public BoardService boardService() {
        // service는 repository bean을 주입받음
        return new BoardService(boardRepository());
    }

    @Bean
    public BoardRepository boardRepository() {
        return new JdbcBoardRepository(dataSource);
    }
}
