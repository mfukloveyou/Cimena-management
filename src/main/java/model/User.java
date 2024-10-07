package model;

public class User {
    private int userId;
    private String username;
    private String password;
    private String email; // Có thể có hoặc không
    private String phoneNumber; // Có thể có hoặc không

    // Constructor không tham số
    public User() {}

    // Constructor chính
    public User(int userId, String username, String password, String email, String phoneNumber) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("username không được để trống");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("password không được để trống");
        }
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    // Constructor đơn giản
    public User(String username, String password) {
        this(0, username, password, null, null); // Giá trị mặc định cho userId và các trường khác
    }

    public boolean validatePassword(String password) {
        return this.password.equals(password);
    }

    // Getters
    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password; // Thêm phương thức này
    }

    public String getUserInfo() {
        return String.format("Người dùng: %s, Email: %s, Số điện thoại: %s", username, email, phoneNumber);
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    // Setters
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("username không được để trống");
        }
        this.username = username;
    }

    public void setPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("password không được để trống");
        }
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return userId == user.userId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(userId);
    }
}
