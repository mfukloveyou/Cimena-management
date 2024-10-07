package service;

import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    private boolean isUsernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0; // Nếu có ít nhất 1 dòng, username đã tồn tại
        } catch (SQLException e) {
            e.printStackTrace(); // Thay bằng logger nếu cần
        }
        return false; // Không tìm thấy username
    }

    public boolean register(User user) {
        if (isUsernameExists(user.getUsername())) {
            return false; // Tên người dùng đã tồn tại
        }

        String sql = "INSERT INTO users (username, password, email, phone) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhoneNumber());
            stmt.executeUpdate();
            return true; // Đăng ký thành công
        } catch (SQLException e) {
            e.printStackTrace(); // Thay bằng logger nếu cần
            return false; // Đăng ký thất bại
        }
    }

    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("user_id"), // Chú ý sửa key từ userId thành user_id
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("phone")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Thay bằng logger nếu cần
        }
        return null; // Không tìm thấy user
    }

    public boolean login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Nếu có dòng nào trả về, đăng nhập thành công
        } catch (SQLException e) {
            e.printStackTrace(); // Thay bằng logger nếu cần
            return false; // Đăng nhập thất bại
        }
    }

    public boolean update(User user) {
        String sql = "UPDATE users SET username = ?, password = ?, email = ?, phone = ? WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhoneNumber());
            stmt.setInt(5, user.getUserId());
            stmt.executeUpdate();
            return true; // Cập nhật thành công
        } catch (SQLException e) {
            e.printStackTrace(); // Thay bằng logger nếu cần
            return false; // Cập nhật thất bại
        }
    }
}
