package service;

import model.Ticket;
import controller.TicketController;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import model.Seat; // Nếu bạn có lớp Seat để đại diện cho ghế

public class TicketService {
    private static final String INSERT_TICKET_SQL = "INSERT INTO tickets (user_id, showtime_id, seat_number, movie_title, showtime_details, movie_id) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String CHECK_USER_SQL = "SELECT COUNT(*) FROM users WHERE user_id = ?";
    private static final String CHECK_SHOWTIME_SQL = "SELECT COUNT(*) FROM showtimes WHERE showtime_id = ?";
    private static final String CHECK_SEAT_AVAILABILITY_SQL = "SELECT booked FROM seats WHERE showtime_id = ? AND seat_number = ?";
    private static final String GET_BOOKED_SEATS_SQL = "SELECT seat_number FROM tickets WHERE showtime_id = ?";


    private List<String> getBookedSeatsForShowtime(int showtimeId) {
        List<String> bookedSeats = new ArrayList<>();
        String sql = "SELECT seat_number FROM tickets WHERE showtime_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, showtimeId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bookedSeats.add(rs.getString("seat_number"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookedSeats;
    }
    public List<Ticket> getTicketsByUserId(int userId) {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT t.*, m.title AS movie_title, m.movie_id FROM tickets t " +
                "JOIN movies m ON t.movie_id = m.movie_id WHERE t.user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Tickets found for user ID " + userId + ": " + tickets.size());
            while (rs.next()) {
                int ticketId = rs.getInt("ticket_id");
                int showtimeId = rs.getInt("showtime_id");
                String seatNumber = rs.getString("seat_number");
                String movieTitle = rs.getString("movie_title");
                String showtimeDetails = rs.getString("showtime_details");
                int movieId = rs.getInt("movie_id"); // Lấy movieId từ kết quả

                tickets.add(new Ticket(ticketId, userId, showtimeId, seatNumber, movieTitle, showtimeDetails, movieId, rs.getTimestamp("booked_at")));


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }
    public Ticket getTicketById(int ticketId) {
        String sql = "SELECT * FROM tickets WHERE ticket_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ticketId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Ticket(
                        rs.getInt("ticket_id"),
                        rs.getInt("user_id"),
                        rs.getInt("showtime_id"),
                        rs.getString("seat_number"),
                        rs.getString("movie_title"),
                        rs.getString("showtime_details"),
                        rs.getInt("movie_id"),
                        rs.getTimestamp("booked_at") // Thêm trường bookedAt
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Không tìm thấy vé
    }

    public boolean deleteTicket(int ticketId) {
        boolean statement = false;
        Ticket ticket = getTicketById(ticketId); // Lấy thông tin vé từ ID
        if (ticket == null) {
            return statement; // Vé không tồn tại
        }

        // Cập nhật trạng thái ghế
        updateSeatStatus(ticket.getShowtimeId(), ticket.getSeatNumber(), false);

        String sql = "DELETE FROM tickets WHERE ticket_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ticketId);
            statement = stmt.executeUpdate() > 0; // Xóa vé
        } catch (SQLException e) {
            e.printStackTrace();


        }
        return statement;
    }
    private void updateSeatStatus(int showtimeId, String seatNumber, boolean booked) {
        String sql = "UPDATE seats SET booked = ? WHERE showtime_id = ? AND seat_number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, booked);
            stmt.setInt(2, showtimeId);
            stmt.setString(3, seatNumber);
            stmt.executeUpdate(); // Cập nhật trạng thái ghế
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean bookSeat(int showtimeId, String seatNumber, int userId) {
        String sql = "UPDATE seats SET booked = 1, user_id = ? WHERE showtime_id = ? AND seat_number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, showtimeId);
            stmt.setString(3, seatNumber);
            return stmt.executeUpdate() > 0; // Nếu cập nhật thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    public boolean bookTicket(Ticket ticket) {
        // Kiểm tra người dùng và suất chiếu
        String[] availableSeats = getAvailableSeats(ticket.getShowtimeId());

        if (!isUserValid(ticket.getUserId()) || !isShowtimeValid(ticket.getShowtimeId())) {
            System.out.println("Người dùng hoặc suất chiếu không hợp lệ.");
            return false;
        }

        // Kiểm tra ghế có sẵn
        if (!isSeatAvailable(ticket.getShowtimeId(), ticket.getSeatNumber())) {
            System.out.println("Ghế đã được đặt. Vui lòng chọn ghế khác.");
            return false;
        }

        // Cập nhật trạng thái ghế
        TicketController ticketController = new TicketController();
        ticketController.updateSeatStatus(ticket.getShowtimeId(), ticket.getSeatNumber(), true);

        // Chèn vé vào cơ sở dữ liệu
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_TICKET_SQL)) {
            stmt.setInt(1, ticket.getUserId());
            stmt.setInt(2, ticket.getShowtimeId());
            stmt.setString(3, ticket.getSeatNumber());
            stmt.setString(4, ticket.getMovieTitle() != null ? ticket.getMovieTitle() : "Chưa có tiêu đề"); // Đảm bảo không NULL
            stmt.setString(5, ticket.getShowtimeDetails() != null ? ticket.getShowtimeDetails() : "Chưa có chi tiết"); // Đảm bảo không NULL
            stmt.setInt(6, ticket.getMovieId()); // Đảm bảo có movieId
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    private boolean isUserValid(int userId) {
        return checkExistence(CHECK_USER_SQL, userId);
    }

    private boolean isShowtimeValid(int showtimeId) {
        return checkExistence(CHECK_SHOWTIME_SQL, showtimeId);
    }
    private boolean checkExistence(String sql, int id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isSeatAvailable(int showtimeId, String seatNumber) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CHECK_SEAT_AVAILABILITY_SQL)) {
            stmt.setInt(1, showtimeId);
            stmt.setString(2, seatNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && !rs.getBoolean("booked");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String[] getAvailableSeats(int showtimeId) {
        List<String> bookedSeats = getBookedSeatsForShowtime(showtimeId);
        System.out.println("Ghế đã đặt: " + bookedSeats); // Ghi log ghế đã đặt

        List<String> allSeats = new ArrayList<>(Arrays.asList("A1", "A2", "A3", "B1", "B2", "B3"));
        allSeats.removeAll(bookedSeats);

        System.out.println("Ghế có sẵn: " + allSeats); // Ghi log ghế có sẵn
        return allSeats.toArray(new String[0]);
    }



}
