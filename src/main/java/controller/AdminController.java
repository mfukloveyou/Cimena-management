package controller;
import model.User; // Thêm import cho User
import java.util.List; // Thêm dòng này
import java.util.ArrayList; // Thêm dòng này nếu bạn cũng sử dụng ArrayList
import service.AdminService;
import model.Movie;
import model.Showtime;
import model.Seat; // Thêm import cho Seat
import java.time.LocalDateTime;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;
public class AdminController {
    private AdminService adminService = new AdminService();
    private Scanner scanner = new Scanner(System.in);
    public void showAllShowtimes() {
        List<Showtime> showtimes = adminService.getAllShowtime();
        System.out.println("Danh sách suất chiếu:");
        for (Showtime showtime : showtimes) {
            System.out.println("ID: " + showtime.getId() + ", Phim: " + showtime.getMovieTitle() + ", Suất chiếu: " + showtime.getShowtime());
        }
    }

    public void showAllSeats() {
        List<Seat> seats = adminService.getAllSeat();
        System.out.println("Danh sách ghế:");
        for (Seat seat : seats) {
            System.out.println("ID ghế: " + seat.getSeatId() + ", ID suất chiếu: " + seat.getShowtimeId() + ", Số ghế: " + seat.getSeatNumber() + ", Đã đặt: " + seat.isBooked());
        }
    }

    public void adminLogin() {
        System.out.print("Nhập tên đăng nhập: ");
        String username = scanner.nextLine();
        System.out.print("Nhập mật khẩu: ");
        String password = scanner.nextLine();

        if (adminService.login(username, password)) {
            System.out.println("Đăng nhập thành công! Bạn có thể quản lý phim và suất chiếu.");
            manageMoviesAndShowtimes();
        } else {
            System.out.println("Tên đăng nhập hoặc mật khẩu không đúng.");
        }
    }
    public List<Movie> getAllMovies() {
        // Lấy tất cả các phim từ AdminService
        return adminService.getAllMovies(); // Giả sử AdminService có phương thức này
    }

    public void manageMoviesAndShowtimes() {
        while (true) {
            System.out.println("Chọn một hành động:");
            System.out.println("1. Thêm phim");
            System.out.println("2. Xóa phim");
            System.out.println("3. Thêm suất chiếu");
            System.out.println("4. Xóa suất chiếu");
            System.out.println("5. Thêm ghế");
            System.out.println("6. Xóa ghế");
            System.out.println("7. Xóa người dùng"); // Thêm tùy chọn xóa người dùng
            System.out.println("8. Thoát");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    addMovie();
                    break;
                case 2:
                    deleteMovie();
                    break;
                case 3:
                    addShowtime();
                    break;
                case 4:
                    deleteShowtime();
                    break;
                case 5:
                    addSeat();
                    break;
                case 6:
                    deleteSeat();
                    break;
                case 7:
                    removeUser(); // Gọi phương thức để xóa người dùng
                    break;
                case 8: // Cập nhật lại số thứ tự cho thoát
                    System.out.println("Thoát quản lý phim và suất chiếu.");
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ, vui lòng chọn lại.");
            }
        }
    }
    public AdminService getAdminService() {
        return adminService;
    }

    public void removeUser() {
        List<User> users = adminService.getAllUsers(); // Gọi phương thức để lấy danh sách người dùng

        // Hiển thị danh sách người dùng
        System.out.println("Danh sách người dùng:");
        for (User user : users) {
            System.out.println("ID: " + user.getUserId() + ", Tên đăng nhập: " + user.getUsername());
        }

        System.out.print("Nhập ID người dùng cần xóa: ");
        Scanner scanner = new Scanner(System.in);
        int userId = scanner.nextInt();

        if (adminService.deleteUser(userId)) {
            System.out.println("Xóa người dùng thành công!");
        } else {
            System.out.println("Xóa người dùng thất bại.");
        }
    }

    public void addMovie() {
        System.out.print("Nhập tiêu đề phim: ");
        String title = scanner.nextLine();
        System.out.print("Nhập đường dẫn hình ảnh: ");
        String imagePath = scanner.nextLine();
        System.out.print("Nhập mô tả: ");
        String description = scanner.nextLine();
        System.out.print("Nhập thể loại: ");
        String genre = scanner.nextLine();
        System.out.print("Nhập thời gian (phút): ");
        int duration = scanner.nextInt();
        scanner.nextLine(); // Clear buffer

        Movie movie = new Movie(title, imagePath, description, genre, duration); // Tạo đối tượng Movie
        if (adminService.addMovie(movie)) {
            System.out.println("Thêm phim thành công.");
        } else {
            System.out.println("Thêm phim thất bại.");
        }
    }

    public void deleteMovie() {
        System.out.print("Nhập ID phim cần xóa: ");
        int movieId = scanner.nextInt();
        scanner.nextLine(); // Clear buffer

        if (adminService.deleteMovie(movieId)) {
            System.out.println("Xóa phim thành công.");
        } else {
            System.out.println("Xóa phim thất bại.");
        }
    }

    public void addShowtime() {
        Scanner scanner = new Scanner(System.in); // Đảm bảo đã khởi tạo Scanner
        System.out.print("Nhập ID suất chiếu: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Clear buffer

        System.out.print("Nhập tiêu đề phim: ");
        String movieTitle = scanner.nextLine();

        System.out.print("Nhập thời gian suất chiếu (yyyy-MM-dd HH:mm:ss): ");

        // Sử dụng DateTimeFormatter để phân tích chuỗi thời gian
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime showtime = LocalDateTime.parse(scanner.nextLine(), formatter);

        System.out.print("Nhập số ghế còn trống: ");
        int availableSeats = scanner.nextInt();

        System.out.print("Nhập ID phim: ");
        int movieId = scanner.nextInt();

        // Tạo đối tượng Showtime
        Showtime newShowtime = new Showtime(id, movieTitle, showtime, availableSeats, movieId);
        if (adminService.addShowtime(newShowtime)) {
            System.out.println("Thêm suất chiếu thành công!");
        } else {
            System.out.println("Thêm suất chiếu thất bại.");
        }}
    public void deleteShowtime() {
        showAllShowtimes();
        System.out.print("Nhập ID suất chiếu cần xóa: ");
        int showtimeId = scanner.nextInt();
        scanner.nextLine(); // Clear buffer

        if (adminService.deleteShowtime(showtimeId)) {
            System.out.println("Xóa suất chiếu thành công.");
        } else {
            System.out.println("Xóa suất chiếu thất bại.");
        }
    }
    public void addSeat() {

        System.out.print("Nhập ID ghế: ");
        String seatId = scanner.nextLine();

        System.out.print("Nhập ID suất chiếu: ");
        int showtimeId = scanner.nextInt();

        System.out.print("Ghế đã được đặt? (true/false): ");
        boolean isBooked = scanner.nextBoolean();
        scanner.nextLine(); // Đọc bỏ dòng còn lại

        System.out.print("Nhập số ghế: "); // Thêm yêu cầu nhập số ghế
        String seatNumber = scanner.nextLine(); // Thay đổi kiểu từ int sang String

        Seat newSeat = new Seat(seatId, showtimeId, isBooked, seatNumber); // Tạo đối tượng Seat với seatNumber
        if (adminService.addSeat(newSeat)) {
            System.out.println("Thêm ghế thành công!");
        } else {
            System.out.println("Thêm ghế thất bại.");
        }
    }


    public void deleteSeat() {
        showAllSeats();
        System.out.print("Nhập ID ghế cần xóa: ");
        String seatId = scanner.nextLine(); // Thay đổi kiểu từ int thành String nếu Seat sử dụng seat_id là String

        if (adminService.deleteSeat(seatId)) {
            System.out.println("Xóa ghế thành công.");
        } else {
            System.out.println("Xóa ghế thất bại.");
        }
    }
}
