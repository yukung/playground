package org.yukung.sandbox.mybatis.user.jdbc;

import org.yukung.sandbox.mybatis.user.Gender;
import org.yukung.sandbox.mybatis.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yukung
 */
public class UserDao {

    private ConnectionFactory connectionFactory;

    protected UserDao(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public int insert(User user) {
        String sql = "INSERT INTO user (name, age, gender) VALUES (?, ?, ?)";
        int rowCount = 0;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getName());
            ps.setInt(2, user.getAge());
            ps.setString(3, user.getGender().name());
            rowCount = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowCount;
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM user";
        List<User> users = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setName(rs.getString("name"));
                user.setAge(rs.getInt("age"));
                user.setGender(Gender.valueOf(rs.getString("gender")));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public User findById(Long id) {
        String sql = "SELECT * FROM user WHERE id = ?";
        User user = null;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                user = new User();
                user.setId(rs.getLong("id"));
                user.setName(rs.getString("name"));
                user.setAge(rs.getInt("age"));
                user.setGender(Gender.valueOf(rs.getString("gender")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    private Connection getConnection() throws SQLException {
        Connection conn = null;
        if (connectionFactory != null) {
            conn = connectionFactory.getConnection();
        }
        return conn;
    }
}
