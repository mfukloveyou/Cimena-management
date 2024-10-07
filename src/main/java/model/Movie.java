package model;

import java.util.List;
import java.util.ArrayList;

public class Movie {
    private int id;
    private String title;
    private String description;
    private int duration;
    private String genre;
    private String imagePath;
    private List<Showtime> showtimes;
    public Movie() {
        this.showtimes = new ArrayList<>();
    }

    // Constructor với tất cả các tham số
    public Movie(int id, String title, String imagePath, String description, String genre, int duration) {
        if (duration <= 0) {
            throw new IllegalArgumentException("Thời lượng phim phải lớn hơn 0.");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Tiêu đề phim không được để trống.");
        }
        this.id = id;
        this.title = title;
        this.imagePath = imagePath;
        this.description = description;
        this.genre = genre;
        this.duration = duration;
        this.showtimes = new ArrayList<>();
    }

    // Constructor đơn giản
    public Movie(String title, String imagePath, String description, String genre, int duration) {
        this(0, title, imagePath, description, genre, duration);
    }



    // Thêm và xóa suất chiếu
    public void addShowtime(Showtime showtime) {
        if (!showtimes.contains(showtime)) { // Kiểm tra trùng lặp
            this.showtimes.add(showtime);
        } else {
            System.out.println("Suất chiếu đã tồn tại.");
        }
    }

    public void removeShowtime(Showtime showtime) {
        this.showtimes.remove(showtime);
    }

    // Tính tổng thời lượng của tất cả các suất chiếu
    public int getTotalShowtimeDuration() {
        return this.showtimes.size() * this.duration;
    }

    public void setShowtimes(List<Showtime> showtimes) {
        this.showtimes = showtimes; // Setter cho showtimes
    }

    // Getters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id; // Setter cho id
    }
    public String getTitle() {
        return title;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getDescription() {
        return description;
    }

    public String getGenre() {
        return genre;
    }

    public int getDuration() {
        return duration;
    }

    public List<Showtime> getShowtimes() {
        return showtimes;
    }

    // Setters
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Tiêu đề phim không được để trống.");
        }
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setDuration(int duration) {
        if (duration <= 0) {
            throw new IllegalArgumentException("Thời lượng phim phải lớn hơn 0.");
        }
        this.duration = duration;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", duration=" + duration +
                '}';
    }
}
