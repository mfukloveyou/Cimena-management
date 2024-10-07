package controller;

import service.UserService;
import model.User;

public class UserController {
    private User loggedInUser;
    private UserService userService = new UserService();

    // Đăng ký người dùng mới
    public boolean registerUser(String username, String password, String email, String phoneNumber) {
        User newUser = new User(0, username, password, email, phoneNumber);
        boolean isRegistered = userService.register(newUser);

        if (isRegistered) {
            System.out.println("Đăng ký thành công!");
        } else {
            System.out.println("Đăng ký thất bại. Tên người dùng đã tồn tại.");
        }

        return isRegistered;
    }

    // Cập nhật thông tin người dùng
    public void update(User user) {
        boolean updated = userService.update(user);
        if (updated) {
            System.out.println("Cập nhật thông tin người dùng thành công!");
        } else {
            System.out.println("Không tìm thấy người dùng để cập nhật.");
        }
    }

    // Đăng nhập người dùng
    public boolean loginUser(String username, String password) {
        boolean isLoggedIn = userService.login(username, password);
        if (isLoggedIn) {
            loggedInUser = getUserByUsername(username);
            if (loggedInUser != null) {
                System.out.println("Đã đăng nhập: " + loggedInUser.getUserId());
                System.out.println("Đăng nhập thành công!");
            }
        } else {
            System.out.println("Đăng nhập thất bại. Vui lòng kiểm tra lại tên người dùng và mật khẩu.");
        }
        return isLoggedIn;
    }

    public User getUserByUsername(String username) {
        return userService.getUserByUsername(username);
    }

    // Đăng xuất người dùng
    public void logoutUser() {
        loggedInUser = null;
        System.out.println("Đã đăng xuất thành công!");
    }

    // Getter cho loggedInUser (nếu cần)
    public User getLoggedInUser() {
        return loggedInUser;
    }
}
