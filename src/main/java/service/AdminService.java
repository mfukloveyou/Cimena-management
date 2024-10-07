package service;

import model.Movie;
import model.Showtime;
import model.Seat;
import model.User; // Giả sử bạn có lớp User
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminService {

    public boolean login(String username, String password) {
        return "admin".equals(username) && "1234".equals(password);
    }

    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM movies"; // Giả sử bạn có bảng movies trong cơ sở dữ liệu

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Giả sử bạn có các cột như id, title, imagePath, description, genre, duration trong bảng movies
                int id = rs.getInt("movie_id");
                String title = rs.getString("title");
                String imagePath = rs.getString("image_path");
                String description = rs.getString("description");
                String genre = rs.getString("genre");
                int duration = rs.getInt("duration");

                Movie movie = new Movie(id, title, imagePath, description, genre, duration);
                movies.add(movie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return movies; // Trả về danh sách các bộ phim
    }

    // Các phương thức khác

    public boolean addMovie(Movie movie) {
        String sql = "INSERT INTO movies (title, image_path, description, genre, duration) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, movie.getTitle());
            stmt.setString(2, movie.getImagePath());
            stmt.setString(3, movie.getDescription());
            stmt.setString(4, movie.getGenre());
            stmt.setInt(5, movie.getDuration());
            return stmt.executeUpdate() > 0; // Nếu thêm thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Thêm thất bại
        }
    }
    public List<Showtime> getAllShowtime() {
        List<Showtime> showtimes = new ArrayList<>();
        String sql = "SELECT * FROM showtimes";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Showtime showtime = new Showtime();
                showtime.setId(rs.getInt("showtime_id"));
                showtime.setMovieTitle(rs.getString("title"));
                showtime.setShowtime(rs.getTimestamp("showtime").toLocalDateTime());
                showtime.setAvailableSeats(rs.getInt("available_seats"));
                showtime.setMovieId(rs.getInt("movie_id"));
                showtimes.add(showtime);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return showtimes;
    }
    public List<Seat> getAllSeat() {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT * FROM seats";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                // Kiểm tra giá trị của cột "booked"
                boolean booked = rs.getObject("booked") != null && rs.getBoolean("booked");

                // Tạo đối tượng Seat từ các dữ liệu lấy được
                Seat seat = new Seat(
                        rs.getString("seat_id"),       // seat_id là chuỗi
                        rs.getInt("showtime_id"),      // showtime_id là số nguyên
                        booked,                        // booked là boolean
                        rs.getString("seat_number")    // seat_number là chuỗi
                );
                seats.add(seat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seats;
    }


    public boolean deleteMovie(int movieId) {
        String sql = "DELETE FROM movies WHERE movie_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, movieId);
            return stmt.executeUpdate() > 0; // Nếu xóa thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Xóa thất bại
        }
    }

    public boolean addShowtime(Showtime showtime) {
        String sql = "INSERT INTO showtimes (movie_id, showtime, available_seats) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, showtime.getMovieId());
            stmt.setTimestamp(2, Timestamp.valueOf(showtime.getShowtime()));
            stmt.setInt(3, showtime.getAvailableSeats());
            return stmt.executeUpdate() > 0; // Nếu thêm thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Thêm thất bại
        }
    }

    public boolean deleteShowtime(int showtimeId) {
        String sql = "DELETE FROM showtimes WHERE showtime_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, showtimeId);
            return stmt.executeUpdate() > 0; // Nếu xóa thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Xóa thất bại
        }
    }

    public boolean addSeat(Seat seat) {
        String sql = "INSERT INTO seats (seat_id, showtime_id, booked, seat_number) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, seat.getSeatId());
            stmt.setInt(2, seat.getShowtimeId());
            stmt.setBoolean(3, seat.isBooked());
            stmt.setString(4, seat.getSeatNumber()); // Thêm seatNumber vào câu lệnh SQL
            return stmt.executeUpdate() > 0; // Nếu thêm thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Thêm thất bại
        }
    }

    public boolean deleteSeat(String seatId) {
        String sql = "DELETE FROM seats WHERE seat_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, seatId);
            return stmt.executeUpdate() > 0; // Nếu xóa thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Xóa thất bại
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users"; // Giả sử bạn có bảng users
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                // Thêm các trường khác nếu cần
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0; // Nếu xóa thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Xóa thất bại
        }
    }


}
