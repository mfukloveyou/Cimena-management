package controller;

import service.DatabaseConnection;
import service.TicketService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet; // Nhập khẩu ResultSet
import java.sql.SQLException;
import java.util.ArrayList; // Nhập khẩu ArrayList
import java.util.List; // Nhập khẩu List

import model.Ticket; // Đảm bảo đường dẫn package chính xác

public class TicketController {



    public boolean updateSeatStatus(int showtimeId, String seatNumber, boolean booked) {
        String sql = "UPDATE seats SET booked = ? WHERE showtime_id = ? AND seat_number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, booked);
            stmt.setInt(2, showtimeId);
            stmt.setString(3, seatNumber);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
