package service;

import model.Movie;
import model.Showtime;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieService {

    // Phương thức để lấy danh sách các suất chiếu cho một bộ phim dựa vào movieId
    public List<Showtime> getShowtimesForMovie(int movieId) {
        List<Showtime> showtimes = new ArrayList<>();
        String sql = "SELECT showtime_id, movie_id, showtime, available_seats FROM showtimes WHERE movie_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Thiết lập tham số cho câu truy vấn
            stmt.setInt(1, movieId);

            // Thực thi truy vấn
            try (ResultSet rs = stmt.executeQuery()) {
                // Lặp qua kết quả
                while (rs.next()) {
                    showtimes.add(new Showtime(
                            rs.getInt("showtime_id"), // ID suất chiếu
                            rs.getString("movie_id"), // Lấy movieId nếu cần thiết
                            rs.getTimestamp("showtime").toLocalDateTime(), // Thời gian suất chiếu
                            rs.getInt("available_seats"), // Số ghế có sẵn
                            movieId // Cung cấp movieId cho Showtime
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("Lỗi truy vấn cơ sở dữ liệu: " + e.getMessage()); // Có thể thay bằng logger
        }

        return showtimes; // Trả về danh sách các suất chiếu
    }



    public int getMovieIdByShowtimeId(int showtimeId) {
        String sql = "SELECT movie_id FROM showtimes WHERE showtime_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, showtimeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("movie_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Hoặc xử lý ngoại lệ theo cách bạn muốn
    }


    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT movie_id, title, image_path, description, genre, duration FROM movies";
        // Sửa từ id thành movie_id

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("movie_id"); // Sửa từ id thành movie_id
                String title = rs.getString("title");
                String imagePath = rs.getString("image_path");
                String description = rs.getString("description");
                String genre = rs.getString("genre");
                int duration = rs.getInt("duration");
                movies.add(new Movie(id, title, imagePath, description, genre, duration));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }

}
