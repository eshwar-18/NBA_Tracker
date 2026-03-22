package persistence;

import helper.UserInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class User_CRUD {

    public static UserInfo login(String username, String password) {
        String sql = "SELECT user_id, username, email, role, password FROM Users WHERE username = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                String dbPass = rs.getString("password");
                if (password != null && password.equals(dbPass)) {
                    return new UserInfo(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("role")
                    );
                }
            }
        } catch (Exception e) {
            System.out.println("User_CRUD.login error: " + e.getMessage());
        }
        return null;
    }

    public static boolean register(String username, String password, String email) {
        String sql = "INSERT INTO Users(username, password, email, role) VALUES(?,?,?,'user')";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, email);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("User_CRUD.register error: " + e.getMessage());
        }
        return false;
    }
}
