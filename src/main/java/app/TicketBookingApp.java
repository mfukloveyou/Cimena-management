package app;
import service.DatabaseConnection;
import controller.UserController;
import model.Movie;
import model.Showtime;
import model.User;
import service.MovieService;
import service.TicketService; // Đảm bảo dòng này không có lỗi
import controller.TicketController; // Đảm bảo dòng này không có lỗi
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import model.Ticket;
import model.Seat; // Thêm dòng này
import java.util.ArrayList; // Thêm dòng này
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import controller.AdminController;
import javax.swing.JOptionPane;

public class TicketBookingApp extends JFrame {
    private UserController userController = new UserController();
    private MovieService movieService = new MovieService(); // Khởi tạo MovieService
    private TicketService ticketService = new TicketService(); // Khởi tạo TicketService
    private TicketController ticketController = new TicketController(); // Khởi tạo TicketController
    private User loggedInUser;

    private int currentUserId; // Thêm biến ở đây
    public TicketBookingApp() {
        setTitle("Ứng Dụng Đặt Vé");
        setSize(800, 600); // Thay đổi kích thước cửa sổ
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI(); // Gọi initUI để khởi tạo giao diện
    }

    private void showStaffLogin() {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        panel.add(new JLabel("Tên Đăng Nhập:"));
        panel.add(usernameField);
        panel.add(new JLabel("Mật Khẩu:"));
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Đăng Nhập Nhân Viên", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // Kiểm tra tài khoản nhân viên
            if (validateStaffLogin(username, password)) {
                JOptionPane.showMessageDialog(null, "Đăng Nhập Thành Công!");
                // Chuyển đến giao diện nhân viên
                showStaffInterface();
            } else {
                JOptionPane.showMessageDialog(null, "Tên đăng nhập hoặc mật khẩu không đúng!");
            }
        }
    }
    private boolean validateStaffLogin(String username, String password) {
        // Kiểm tra thông tin đăng nhập
        return "admin".equals(username) && "1234".equals(password); // Tài khoản mặc định
    }
    private void showStaffInterface() {
        JFrame staffFrame = new JFrame("Giao Diện Nhân Viên");
        staffFrame.setSize(800, 600);
        staffFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        staffFrame.setLayout(new BorderLayout());

        // Tạo panel cho các chức năng
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2)); // Cập nhật số hàng để thêm các nút

        // Tạo đối tượng AdminController
        AdminController adminController = new AdminController();

        // Nút thêm suất chiếu
        JButton addShowtimeButton = new JButton("Thêm Suất Chiếu");
        addShowtimeButton.addActionListener(e -> {
            // Gọi phương thức thêm suất chiếu
            adminController.addShowtime();
        });

        // Nút xóa suất chiếu
        JButton deleteShowtimeButton = new JButton("Xóa Suất Chiếu");
        deleteShowtimeButton.addActionListener(e -> {
            // Gọi phương thức xóa suất chiếu
            adminController.deleteShowtime();
        });

        // Nút thêm ghế
        JButton addSeatButton = new JButton("Thêm Ghế");
        addSeatButton.addActionListener(e -> {
            // Gọi phương thức thêm ghế
            adminController.addSeat();
        });

        // Nút xóa ghế
        JButton deleteSeatButton = new JButton("Xóa Ghế");
        deleteSeatButton.addActionListener(e -> {
            // Gọi phương thức xóa ghế
            adminController.deleteSeat();
        });

        // Nút xóa người dùng
        JButton deleteUserButton = new JButton("Xóa Người Dùng");
        deleteUserButton.addActionListener(e -> {
            // Gọi phương thức xóa người dùng
            adminController.removeUser();
        });

        // Nút thêm phim
        JButton addMovieButton = new JButton("Thêm Phim");
        addMovieButton.addActionListener(e -> {
            // Gọi phương thức thêm phim
            adminController.addMovie();
        });

        JButton deleteMovieButton = new JButton("Xóa Phim");
        deleteMovieButton.addActionListener(e -> {
            // Lấy danh sách tất cả phim
            List<Movie> movies = adminController.getAdminService().getAllMovies(); // Gọi từ adminService

            // Hiển thị danh sách phim để người dùng chọn phim cần xóa
            StringBuilder movieList = new StringBuilder("Danh sách phim:\n");
            for (Movie movie : movies) {
                movieList.append("ID: ").append(movie.getId()).append(", Tiêu đề: ").append(movie.getTitle()).append("\n");
            }

            String selectedMovieId = JOptionPane.showInputDialog(movieList.toString() + "Nhập ID phim cần xóa:");
            if (selectedMovieId != null && !selectedMovieId.isEmpty()) {
                try {
                    int movieId = Integer.parseInt(selectedMovieId); // Chuyển đổi chuỗi sang int
                    adminController.deleteMovie(); // Gọi phương thức xóa phim với ID đã nhập
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "ID phim không hợp lệ. Vui lòng nhập lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });



        // Thêm các nút vào panel
        panel.add(addShowtimeButton);
        panel.add(deleteShowtimeButton);
        panel.add(addSeatButton);
        panel.add(deleteSeatButton);
        panel.add(deleteUserButton);
        panel.add(addMovieButton); // Thêm nút thêm phim
        panel.add(deleteMovieButton); // Thêm nút xóa phim

        // Thêm panel vào frame
        staffFrame.add(panel, BorderLayout.CENTER);

        // Thêm thanh trạng thái
        JLabel statusLabel = new JLabel("Trạng thái: ");
        staffFrame.add(statusLabel, BorderLayout.SOUTH);

        staffFrame.setVisible(true);
    }


    private void initUI() {
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(new ImageIcon("C:/Users/admin/Downloads/tải xuống.jpg").getImage(), 0, 0, getWidth(), getHeight(), this);
                drawTitle(g);
            }

            private void drawTitle(Graphics g) {
                String title = "Ứng Dụng Đặt Vé";
                Font font = new Font("Arial", Font.BOLD, 48);
                g.setFont(font);
                FontMetrics metrics = g.getFontMetrics(font);
                int x = (getWidth() - metrics.stringWidth(title)) / 2;
                int y = (getHeight() / 2) - (metrics.getHeight() / 2) + metrics.getAscent();

                // Vẽ viền đen
                g.setColor(Color.BLACK);
                g.drawString(title, x - 2, y);
                g.drawString(title, x + 2, y);
                g.drawString(title, x, y - 2);
                g.drawString(title, x, y + 2);

                // Vẽ chữ màu xanh lá
                g.setColor(Color.GREEN);
                g.drawString(title, x, y);
            }
        };

        setLayout(new BorderLayout());
        add(backgroundPanel);
    }
    private void showTicketsOption() {
        String[] options = {"Xem Vé Đã Đặt", "Trở Lại"};
        int choice = JOptionPane.showOptionDialog(this, "Bạn muốn làm gì?", "Chọn Tùy Chọn",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (choice == 0) {
            List<Ticket> tickets = ticketService.getTicketsByUserId(currentUserId); // Cập nhật để sử dụng currentUserId
            if (tickets.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Bạn chưa đặt vé nào.");
            } else {
                StringBuilder ticketList = new StringBuilder("Danh Sách Vé Đã Đặt:\n");
                for (Ticket ticket : tickets) {
                    ticketList.append("Phim: ").append(ticket.getMovieTitle())
                            .append(", Ghế: ").append(ticket.getSeatNumber()) // Sửa từ getSeatId thành getSeatNumber
                            .append(", Suất Chiếu: ").append(ticket.getShowtimeDetails())
                            .append("\n");
                }
                JOptionPane.showMessageDialog(this, ticketList.toString());
            }
        }
    }
    private Seat getSeatByNumber(String seatNumber, int showtimeId) {
        List<Seat> seats = loadSeats(showtimeId); // Tải ghế cho suất chiếu đó
        for (Seat seat : seats) {
            if (seat.getSeatId().equals(seatNumber)) {
                return seat; // Trả về ghế nếu tìm thấy
            }
        }
        return null; // Không tìm thấy
    }
    private void showLoginOrRegisterOption() {
        if (loggedInUser != null) {
            // Nếu người dùng đã đăng nhập, không hiển thị hộp thoại
            return;
        }

        JFrame optionFrame = new JFrame("Chọn Tùy Chọn");
        optionFrame.setSize(400, 300);
        optionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        optionFrame.setLayout(new GridLayout(5, 1)); // Đổi số hàng thành 5

        optionFrame.getContentPane().setBackground(Color.DARK_GRAY);

        JLabel titleLabel = new JLabel("Bạn muốn làm gì?", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        optionFrame.add(titleLabel);

        JButton loginButton = new JButton("Đăng Nhập");
        styleButton(loginButton);
        loginButton.addActionListener(e -> {
            if (showLoginPanel()) {
                optionFrame.dispose();
                showMovieSelectionPanel(); // Hiển thị giao diện đặt phim
            }
        });
        optionFrame.add(loginButton);

        JButton customerLoginButton = new JButton("Đăng Nhập Nhân Viên"); // Thêm nút mới
        styleButton(customerLoginButton);
        // Thay đổi từ showEmployeeLoginPanel() sang showStaffLogin()
        customerLoginButton.addActionListener(e -> {
            showStaffLogin(); // Gọi phương thức đăng nhập nhân viên
            optionFrame.dispose(); // Đóng cửa sổ chọn tùy chọn sau khi đăng nhập
        });

        optionFrame.add(customerLoginButton); // Thêm nút vào khung

        JButton registerButton = new JButton("Đăng Ký");
        styleButton(registerButton);
        registerButton.addActionListener(e -> {
            optionFrame.dispose();
            showRegisterPanel();
        });
        optionFrame.add(registerButton);

        JButton exitButton = new JButton("Thoát");
        styleButton(exitButton);
        exitButton.addActionListener(e -> System.exit(0));
        optionFrame.add(exitButton);

        optionFrame.setLocationRelativeTo(null); // Đưa frame vào giữa màn hình
        optionFrame.setVisible(true);
    }

    private boolean showLoginPanel() {
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.setBackground(new Color(60, 63, 65)); // Màu nền xám cho panel

        // Tạo JLabel với màu chữ trắng
        JLabel usernameLabel = new JLabel("Tên Người Dùng:");
        usernameLabel.setForeground(Color.WHITE); // Đổi màu chữ sang trắng
        panel.add(usernameLabel);
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Mật Khẩu:");
        passwordLabel.setForeground(Color.WHITE); // Đổi màu chữ sang trắng
        panel.add(passwordLabel);
        panel.add(passwordField);

        // Tạo một logo và điều chỉnh kích thước
        ImageIcon originalLogo = new ImageIcon("C:\\Users\\admin\\Downloads\\pngtree-movie-board-icon-image_1455346.jpg"); // Đường dẫn tới logo phim
        Image logoImage = originalLogo.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH); // Thay đổi kích thước
        ImageIcon logo = new ImageIcon(logoImage);

        int option = JOptionPane.showOptionDialog(this, panel, "Đăng Nhập",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, logo, null, null);

        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            boolean isLoggedIn = userController.loginUser(username, password);
            if (isLoggedIn) {
                loggedInUser = userController.getUserByUsername(username);
                currentUserId = loggedInUser.getUserId(); // Cập nhật ID người dùng
                JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
                return true; // Đăng nhập thành công
            } else {
                JOptionPane.showMessageDialog(this, "Đăng nhập thất bại.");
            }
        }

        return false; // Đăng nhập thất bại hoặc người dùng không điền đủ thông tin
    }
    private void logout() {
        loggedInUser = null; // Đặt lại biến
        showLoginOrRegisterOption(); // Hiển thị lại tùy chọn đăng nhập
    }
    private void showRegisterPanel() {
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.setBackground(new Color(60, 63, 65));

        // Thêm logo vào panel
        ImageIcon logoIcon = new ImageIcon("C:\\Users\\admin\\Downloads\\pngtree-movie-board-icon-image_1455346.jpg"); // Đường dẫn tới logo
        Image img = logoIcon.getImage().getScaledInstance(100, 75, Image.SCALE_SMOOTH); // Kích thước nhỏ hơn
        logoIcon = new ImageIcon(img);
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(logoLabel);

        // Thay đổi màu chữ sang trắng
        JLabel usernameLabel = new JLabel("Tên Người Dùng:");
        usernameLabel.setForeground(Color.WHITE);
        panel.add(usernameLabel);
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Mật Khẩu:");
        passwordLabel.setForeground(Color.WHITE);
        panel.add(passwordLabel);
        panel.add(passwordField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.WHITE);
        panel.add(emailLabel);
        panel.add(emailField);

        JLabel phoneLabel = new JLabel("Số Điện Thoại:");
        phoneLabel.setForeground(Color.WHITE);
        panel.add(phoneLabel);
        panel.add(phoneField);

        boolean registrationSuccessful = false;

        while (!registrationSuccessful) {
            int option = JOptionPane.showOptionDialog(this, panel, "Đăng Ký",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);

            if (option == JOptionPane.OK_OPTION) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String email = emailField.getText();
                String phoneNumber = phoneField.getText();

                if (username.isEmpty() || password.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng điền đủ thông tin.");
                } else {
                    boolean registered = userController.registerUser(username, password, email, phoneNumber);
                    if (registered) {
                        User user = userController.getUserByUsername(username);
                        if (user != null) {
                            JOptionPane.showMessageDialog(this, "Đăng ký thành công! Thông tin: " + user.getUsername());
                            registrationSuccessful = true;
                            showLoginOrRegisterOption(); // Quay lại tùy chọn
                        } else {
                            JOptionPane.showMessageDialog(this, "Đăng ký thất bại!");
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Đăng ký thất bại!");
                    }
                }
            } else {
                showLoginOrRegisterOption(); // Quay lại tùy chọn
                break; // Thoát khỏi vòng lặp
            }
        }
    }
    private void showBookedTickets() {
        List<Ticket> tickets = ticketService.getTicketsByUserId(currentUserId);
        if (tickets.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bạn chưa đặt vé nào.");
            return;
        }

        StringBuilder ticketInfo = new StringBuilder("Danh Sách Vé Đã Đặt:\n");
        String[] ticketOptions = new String[tickets.size()];
        for (int i = 0; i < tickets.size(); i++) {
            Ticket ticket = tickets.get(i);
            ticketOptions[i] = "Phim: " + ticket.getMovieTitle() + ", Ghế: " + ticket.getSeatNumber() + ", Suất Chiếu: " + ticket.getShowtimeDetails();
            ticketInfo.append(ticketOptions[i]).append("\n");
        }

        int choice = JOptionPane.showConfirmDialog(this, ticketInfo.toString() +
                "\nBạn có muốn xóa vé nào không?", "Thông Tin Vé", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            // Sử dụng JOptionPane với danh sách các vé đã đặt
            String ticketToDelete = (String) JOptionPane.showInputDialog(this,
                    "Chọn vé bạn muốn xóa:",
                    "Xóa Vé",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    ticketOptions,
                    ticketOptions[0]);

            if (ticketToDelete != null) {  // Kiểm tra người dùng có chọn vé
                Ticket ticketToRemove = null;
                for (Ticket ticket : tickets) {
                    if (ticketToDelete.contains(ticket.getSeatNumber())) {
                        ticketToRemove = ticket;
                        break;
                    }
                }

                if (ticketToRemove != null) {
                    deleteTicket(ticketToRemove.getTicketId(), ticketToRemove.getSeatNumber(), ticketToRemove.getShowtimeId());
                    JOptionPane.showMessageDialog(this, "Xóa vé thành công!");
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy vé.");
                }
            }
        }
    }

    private void deleteTicket(int ticketId, String seatNumber, int showtimeId) {
        ticketService.deleteTicket(ticketId);
        // Lấy ghế dựa vào seatNumber
        Seat seat = getSeatByNumber(seatNumber, showtimeId);
        if (seat != null) {
            updateSeatStatus(seat.getSeatId(), false, showtimeId); // Thay đổi showtime.getId() thành showtimeId
        }
    }
    private void showMovieSelectionPanel() {
        JFrame movieFrame = new JFrame("Chọn Bộ Phim");
        movieFrame.setSize(1280, 720);
        movieFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        movieFrame.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Chọn Bộ Phim Để Đặt Vé", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        movieFrame.getContentPane().setBackground(Color.DARK_GRAY);
        movieFrame.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.DARK_GRAY);

        JButton viewTicketsButton = new JButton("Xem Vé Đã Đặt");
        styleButton(viewTicketsButton);
        viewTicketsButton.addActionListener(e -> showBookedTickets());
        buttonPanel.add(viewTicketsButton);

        JButton showTicketsOptionButton = new JButton("Xem Tùy Chọn Vé");
        styleButton(showTicketsOptionButton);
        showTicketsOptionButton.addActionListener(e -> showTicketsOption());
        buttonPanel.add(showTicketsOptionButton);

        JButton exitButton = new JButton("Thoát");
        styleButton(exitButton);
        exitButton.addActionListener(e -> {
            movieFrame.dispose(); // Đóng cửa sổ chọn phim
            logout(); // Gọi phương thức đăng xuất
        });
        buttonPanel.add(exitButton);

        movieFrame.add(buttonPanel, BorderLayout.SOUTH);

        JPanel moviePanel = new JPanel(new GridLayout(0, 2));
        moviePanel.setBackground(Color.DARK_GRAY);
        List<Movie> movies = movieService.getAllMovies();
        for (Movie movie : movies) {
            JButton movieButton = new JButton(movie.getTitle());
            movieButton.setBackground(new Color(60, 63, 65));
            movieButton.setForeground(Color.WHITE);
            movieButton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));

            try {
                ImageIcon icon = new ImageIcon(movie.getImagePath());
                Image img = icon.getImage().getScaledInstance(480, 270, Image.SCALE_SMOOTH);
                movieButton.setIcon(new ImageIcon(img));
            } catch (Exception e) {
                movieButton.setText("Không tìm thấy hình ảnh");
                movieButton.setIcon(null);
            }

            movieButton.addActionListener(new MovieButtonAction(movie));
            moviePanel.add(movieButton);
        }

        JScrollPane scrollPane = new JScrollPane(moviePanel);
        movieFrame.add(scrollPane, BorderLayout.CENTER);

        movieFrame.setLocationRelativeTo(null);
        movieFrame.setVisible(true);
    }
    private void styleButton(JButton button) {
        button.setBackground(new Color(60, 63, 65)); // Màu nền tối
        button.setForeground(Color.WHITE); // Màu chữ
        button.setBorder(BorderFactory.createRaisedBevelBorder()); // Viền nổi
        button.setFocusPainted(false); // Bỏ viền khi nút được chọn
        button.setPreferredSize(new Dimension(120, 35)); // Kích thước nhỏ hơn
        button.setFont(new Font("Arial", Font.BOLD, 14)); // Kích thước phông chữ nhỏ hơn
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Đổi con trỏ chuột khi di chuột qua
        button.setMargin(new Insets(5, 10, 5, 10)); // Lề cho nút
        button.setOpaque(true); // Bật tính năng opaqueness
        button.setContentAreaFilled(false); // Tắt tính năng làm đầy khu vực nội dung
        button.setBorderPainted(true); // Hiện viền
    }
    private void updateSeatStatus(String seatNumber, boolean booked, int showtimeId) {
        String sql = "UPDATE seats SET booked = ? WHERE seat_number = ? AND showtime_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, booked);
            stmt.setString(2, seatNumber);
            stmt.setInt(3, showtimeId); // Cung cấp showtimeId
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private class MovieButtonAction implements ActionListener {
        private final Movie movie;

        public MovieButtonAction(Movie movie) {
            this.movie = movie;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Lấy suất chiếu cho bộ phim
            List<Showtime> showtimes = movieService.getShowtimesForMovie(movie.getId());
            movie.setShowtimes(showtimes); // Cập nhật suất chiếu vào đối tượng movie
            showMovieDetails(movie); // Hiển thị thông tin phim
        }
    }
    private void showMovieDetails(Movie movie) {
        StringBuilder showtimeDetails = new StringBuilder();

        if (movie.getShowtimes() != null && !movie.getShowtimes().isEmpty()) {
            showtimeDetails.append("<ul>"); // Mở thẻ ul cho danh sách suất chiếu
            for (Showtime showtime : movie.getShowtimes()) {
                showtimeDetails.append("<li>")
                        .append("Suất chiếu: ").append(showtime.getShowtime().toString()) // Giả sử getShowtime() trả về LocalDateTime

                        .append("</li><br/>"); // Xuống dòng giữa các suất chiếu
            }
            showtimeDetails.append("</ul>"); // Đóng thẻ ul
        } else {
            showtimeDetails.append("Không có suất chiếu nào.");
        }

        // Tạo nội dung HTML cho JOptionPane
        String message = "<html>" +
                "<h2 style='color: #FF0000;'>Tên Phim: " + movie.getTitle() + "</h2><br/>" + // Tên phim
                "<p style='color: white; background-color: rgba(0, 0, 0, 0.6); padding: 10px;'>Mô Tả: " + movie.getDescription() + "</p><br/>" + // Mô tả phim
                "<h4 style='color: #FF0000;'>Suất Chiếu:</h4>" +
                "<p style='color: white; background-color: rgba(0, 0, 0, 0.6); padding: 10px;'>" + showtimeDetails.toString() + "</p>" +
                "</html>";

        // Hiển thị hộp thoại với thông tin phim và suất chiếu
        JOptionPane.showMessageDialog(this, message, "Thông Tin Phim", JOptionPane.INFORMATION_MESSAGE);

        // Mở giao diện chọn suất chiếu
        showShowtimeSelectionPanel(movie);
    }

    private void showSeatSelectionPanel(Showtime showtime) {
        JFrame seatFrame = new JFrame("Chọn Ghế");
        seatFrame.setSize(800, 600);
        seatFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        seatFrame.setLayout(new BorderLayout());

        // Thêm tiêu đề vào panel riêng
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Chọn Ghế", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.setBackground(Color.DARK_GRAY); // Màu nền cho panel tiêu đề
        titlePanel.add(titleLabel); // Thêm label vào panel tiêu đề
        seatFrame.add(titlePanel, BorderLayout.NORTH); // Thêm panel tiêu đề vào khung

        JPanel seatPanel = new JPanel(new GridLayout(0, 10, 10, 10)); // Thêm khoảng cách giữa các ghế
        seatPanel.setBackground(Color.LIGHT_GRAY); // Thiết lập nền màu xám nhẹ cho panel chứa ghế
        List<Seat> seats = loadSeats(showtime.getId());
        for (Seat seat : seats) {
            JButton seatButton = new JButton(seat.getSeatId());
            if (seat.isBooked()) {
                seatButton.setBackground(Color.BLUE); // Ghế đã đặt
            } else {
                seatButton.setBackground(Color.WHITE); // Ghế chưa đặt
                seatButton.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2)); // Viền vàng
            }
            seatButton.addActionListener(e -> handleSeatSelection(seat, showtime, seatButton, seatFrame));
            seatPanel.add(seatButton);
        }

        seatFrame.add(seatPanel, BorderLayout.CENTER); // Thêm panel ghế vào khung

        seatFrame.setLocationRelativeTo(null); // Đưa cửa sổ vào giữa màn hình
        seatFrame.setVisible(true);
    }
    private void handleSeatSelection(Seat seat, Showtime showtime, JButton seatButton, JFrame seatFrame) {
        if (!seat.isBooked()) {
            int movieId = movieService.getMovieIdByShowtimeId(showtime.getId());
            seat.book();
            seatButton.setBackground(Color.RED);

            int result = JOptionPane.showConfirmDialog(seatFrame, "Bạn đã chọn ghế " + seat.getSeatId() +
                    "\nBạn có muốn xác nhận đặt vé không?", "Thông Tin Vé", JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                Ticket ticket = new Ticket(0,
                        loggedInUser.getUserId(),
                        showtime.getId(),
                        seat.getSeatId(),
                        showtime.getMovieTitle(),
                        showtime.getShowtimeDetails(),
                        movieId, // Sử dụng movieId từ dịch vụ
                        new Timestamp(System.currentTimeMillis())); // Thêm giá trị cho bookedAt

                if (ticketService.bookTicket(ticket)) {
                    updateSeatStatus(seat.getSeatId(), true, showtime.getId());
                    JOptionPane.showMessageDialog(seatFrame, "Bạn đã đặt vé thành công!");
                } else {
                    JOptionPane.showMessageDialog(seatFrame, "Đặt vé thất bại! Vui lòng thử lại.");
                }
            } else {
                seat.cancelBooking();
                seatButton.setBackground(Color.GREEN);
                JOptionPane.showMessageDialog(seatFrame, "Hủy đặt ghế thành công!");
            }
        } else {
            JOptionPane.showMessageDialog(seatFrame, "Ghế này đã được đặt!");
        }
    }
    private void showShowtimeSelectionPanel(Movie movie) {
        JFrame showtimeFrame = new JFrame("Chọn Suất Chiếu");
        showtimeFrame.setSize(800, 600);
        showtimeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        showtimeFrame.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Chọn Suất Chiếu cho " + movie.getTitle(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        showtimeFrame.getContentPane().setBackground(Color.DARK_GRAY);
        showtimeFrame.add(titleLabel, BorderLayout.NORTH);

        JPanel showtimePanel = new JPanel();
        showtimePanel.setLayout(new BoxLayout(showtimePanel, BoxLayout.Y_AXIS)); // Use BoxLayout for vertical stacking
        showtimePanel.setBackground(Color.DARK_GRAY);
        showtimePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        List<Showtime> showtimes = movie.getShowtimes();

        for (Showtime showtime : showtimes) {
            // Create a JPanel for each showtime with spacing
            JPanel showtimeItemPanel = new JPanel();
            showtimeItemPanel.setLayout(new BoxLayout(showtimeItemPanel, BoxLayout.Y_AXIS));
            showtimeItemPanel.setBackground(Color.DARK_GRAY);

            JLabel showtimeLabel = new JLabel(showtime.getShowtime() + " - ");
            showtimeLabel.setForeground(Color.WHITE);
            showtimeLabel.setFont(new Font("Arial", Font.PLAIN, 18));

            showtimeItemPanel.add(showtimeLabel);
            showtimeItemPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Space after the label

            JButton selectButton = new JButton("Chọn");
            selectButton.setBackground(new Color(60, 63, 65));
            selectButton.setForeground(Color.WHITE);
            selectButton.setBorder(BorderFactory.createRaisedBevelBorder());
            selectButton.setFont(new Font("Arial", Font.BOLD, 16));
            selectButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            selectButton.addActionListener(e -> {
                showtimeFrame.dispose(); // Đóng cửa sổ chọn suất chiếu
                showSeatSelectionPanel(showtime); // Gọi phương thức để chọn ghế
            });

            showtimeItemPanel.add(selectButton);

            // Add space after each item
            showtimePanel.add(showtimeItemPanel);
            showtimePanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add space between each showtime item
        }

        JScrollPane scrollPane = new JScrollPane(showtimePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        showtimeFrame.add(scrollPane, BorderLayout.CENTER);

        showtimeFrame.setLocationRelativeTo(null); // Đưa frame vào giữa màn hình
        showtimeFrame.setVisible(true);
    }
    private List<Seat> loadSeats(int showtimeId) {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT seat_number, booked FROM seats WHERE showtime_id = ?"; // Đảm bảo điều kiện này
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, showtimeId); // Thêm điều kiện showtime_id
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String seatNumber = rs.getString("seat_number");
                boolean booked = rs.getBoolean("booked");

                // Khởi tạo đối tượng Seat với seatNumber, showtimeId và trạng thái booked
                Seat seat = new Seat(seatNumber, showtimeId, booked);
                seats.add(seat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seats; // Trả về danh sách ghế
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TicketBookingApp app = new TicketBookingApp();
            app.setVisible(true);
            app.showLoginOrRegisterOption(); // Chỉ gọi một lần
        });
    }

}