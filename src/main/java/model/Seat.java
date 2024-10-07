package model;

public class Seat {
    private String seatId;
    private boolean booked;
    private int showtimeId;
    private String seatNumber; // Giữ nguyên kiểu String cho seatNumber

    // Constructor mặc định
    public Seat() {
        this.seatId = "";
        this.showtimeId = 0; // Cần khởi tạo showtimeId nếu không sử dụng
        this.booked = false;
        this.seatNumber = ""; // Khởi tạo seatNumber
    }

    public Seat(String seatId, int showtimeId, boolean booked, String seatNumber) {
        this.seatId = seatId;
        this.showtimeId = showtimeId;
        this.booked = booked;
        this.seatNumber = seatNumber; // Gán seatNumber
    }

    public Seat(String seatId, int showtimeId, boolean booked) {
        if (seatId == null || seatId.isEmpty()) {
            throw new IllegalArgumentException("seatId không được để trống");
        }
        this.seatId = seatId;
        this.showtimeId = showtimeId; // Gán showtimeId
        this.booked = booked;
        this.seatNumber = ""; // Khởi tạo seatNumber nếu không có
    }

    public int getShowtimeId() {
        return showtimeId;
    }

    public String getSeatId() {
        return seatId;
    }

    public boolean isBooked() {
        return booked;
    }

    // Thêm phương thức để cập nhật trạng thái ghế
    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    // Thêm phương thức để hủy đặt
    public void cancelBooking() {
        this.booked = false;
    }

    public void setShowtimeId(int showtimeId) {
        this.showtimeId = showtimeId; // Đảm bảo phương thức này tồn tại
    }

    public boolean book() {
        if (!booked) {
            this.booked = true;
            return true;
        } else {
            throw new IllegalStateException("Ghế đã được đặt.");
        }
    }

    public String getSeatNumber() { // Thay đổi kiểu trả về thành String
        return seatNumber; // Đảm bảo rằng đây là String
    }

    public String getStatus() {
        return booked ? "Đã đặt" : "Chưa đặt";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Seat seat = (Seat) obj;
        return seatId.equals(seat.seatId);
    }

    @Override
    public int hashCode() {
        return seatId.hashCode();
    }

    @Override
    public String toString() {
        return "Seat{" +
                "seatId='" + seatId + '\'' +
                ", booked=" + booked +
                ", showtimeId=" + showtimeId + // Thêm showtimeId vào toString
                ", seatNumber='" + seatNumber + '\'' + // Thêm seatNumber vào toString
                '}';
    }
}
