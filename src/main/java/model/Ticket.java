package model;

import java.sql.Timestamp;

public class Ticket {
    private int ticketId;
    private int userId;
    private int showtimeId;
    private String seatNumber;
    private String movieTitle;
    private String showtimeDetails;
    private int movieId; // Để lưu ID phim
    private Timestamp bookedAt; // Thêm thuộc tính bookedAt

    // Constructor mặc định
    public Ticket() {
        // Khởi tạo giá trị mặc định nếu cần
    }

    // Constructor cho việc lưu vé
    public Ticket(int ticketId, int userId, int showtimeId, String seatNumber, String movieTitle, String showtimeDetails, int movieId, Timestamp bookedAt) {
        this.ticketId = ticketId;
        this.userId = userId;
        this.showtimeId = showtimeId;
        this.seatNumber = seatNumber;
        this.movieTitle = movieTitle;
        this.showtimeDetails = showtimeDetails;
        this.movieId = movieId;
        this.bookedAt = bookedAt; // Thêm trường bookedAt
    }


    // Getter methods
    public int getTicketId() {
        return ticketId;
    }

    public int getUserId() {
        return userId;
    }

    public int getShowtimeId() {
        return showtimeId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getShowtimeDetails() {
        return showtimeDetails;
    }

    public int getMovieId() {
        return movieId;
    }

    public Timestamp getBookedAt() { // Thêm getter cho bookedAt
        return bookedAt;
    }

    // Setter methods
    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setShowtimeId(int showtimeId) {
        this.showtimeId = showtimeId;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public void setShowtimeDetails(String showtimeDetails) {
        this.showtimeDetails = showtimeDetails;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public void setBookedAt(Timestamp bookedAt) {
        this.bookedAt = bookedAt; // Bạn cần thêm thuộc tính bookedAt trong lớp Ticket
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId=" + ticketId +
                ", userId=" + userId +
                ", showtimeId=" + showtimeId +
                ", seatNumber='" + seatNumber + '\'' +
                ", movieTitle='" + movieTitle + '\'' +
                ", showtimeDetails='" + showtimeDetails + '\'' +
                ", movieId=" + movieId +
                ", bookedAt=" + bookedAt + // Thêm bookedAt vào toString
                '}';
    }
}
