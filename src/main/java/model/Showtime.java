package model;

import java.time.LocalDateTime;

public class Showtime {
    private int id;
    private String movieTitle;
    private LocalDateTime showtime;
    private int availableSeats;
    private int movieId;

    // Constructor mặc định
    public Showtime() {
        // Constructor mặc định không cần thiết phải thiết lập showtime
    }

    // Constructor với tất cả tham số
    public Showtime(int id, String movieTitle, LocalDateTime showtime, int availableSeats, int movieId) {
        this.id = id;
        this.movieTitle = movieTitle;
        this.showtime = showtime;
        this.availableSeats = availableSeats;
        this.movieId = movieId;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public LocalDateTime getShowtime() {
        return showtime;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public void setShowtime(LocalDateTime showtime) {
        this.showtime = showtime;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    // Phương thức để lấy chi tiết suất chiếu
    public String getShowtimeDetails() {
        return movieTitle + " - " + showtime.toString() +
                " | Available Seats: " + availableSeats;
    }

    @Override
    public String toString() {
        return "Showtime{" +
                "id=" + id +
                ", movieTitle='" + movieTitle + '\'' +
                ", showtime=" + showtime +
                ", availableSeats=" + availableSeats +
                ", movieId=" + movieId +
                '}';
    }
}
