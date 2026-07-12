package hello.example_board.repository;

import hello.example_board.domain.Board;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class JdbcBoardRepository implements BoardRepository {

    // 의존성 주입
    private final DataSource dataSource;

    // 생성자로 스프링이 주입해주는 DataSource를 받는다(HikariCP 커넥션 풀 들어옴)
    public JdbcBoardRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Board save(Board board) {
        String sql = "insert into board(title, content, writer, reg_date, view_count)" +
                " values(?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            // RETURN_GENERATED_KEYS: DB에서 자동 증가된 ID(PK) 값을 다시 받아오기 위함
            pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, board.getTitle());
            pstmt.setString(2, board.getContent());
            pstmt.setString(3, board.getWriter());
            pstmt.setTimestamp(4, Timestamp.valueOf(java.time.LocalDateTime.now()));
            pstmt.setInt(5, 0);

            pstmt.executeUpdate();  // DB에서 쿼리 실행

            // 생성된 ID 꺼내기
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                board.setId(rs.getLong(1));
            }
            return board;
        } catch (SQLException e) {
            throw new IllegalStateException(e); // 체크 예외를 언체크로 변환
        } finally {
            close(conn, pstmt, rs); // 자원 반납
        }
    }

    @Override
    public Optional<Board> findById(Long id) {
        String sql = "select * from board where id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                Board board = new Board();
                board.setId(rs.getLong("id"));
                board.setTitle(rs.getString("title") == null ? rs.getString("title") : rs.getString("title"));
                board.setContent(rs.getString("content"));
                board.setWriter(rs.getString("writer"));
                board.setRegDate(rs.getTimestamp("reg_date").toLocalDateTime());
                board.setViewCount(rs.getInt("view_count"));
                return Optional.of(board);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (conn != null) {
            DataSourceUtils.releaseConnection(conn, dataSource);
        }
    }

    private Connection getConnection() {
        return DataSourceUtils.getConnection(dataSource);
    }

    @Override
    public List<Board> findAll() {
        String sql = "select * from board order by id desc";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            List<Board> boards = new ArrayList<>();
            while (rs.next()) {
                Board board = new Board();
                board.setId(rs.getLong("id"));
                board.setTitle(rs.getString("title"));
                board.setContent(rs.getString("content"));
                board.setWriter(rs.getString("writer"));
                board.setRegDate(rs.getTimestamp("reg_date").toLocalDateTime());
                board.setViewCount(rs.getInt("view_count"));
                boards.add(board);
            }
            return boards;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public void update(Long id, Board updateParam) {
        String sql = "update board set title = ?, content = ?, writer = ? where id = ? ";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, updateParam.getTitle());
            pstmt.setString(2, updateParam.getContent());
            pstmt.setString(3, updateParam.getWriter());
            pstmt.setLong(4, id);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, null);
        }

    }

    @Override
    public void delete(Long id) {
        String sql = "delete from board where id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, null);
        }

    }
}
