package org.repository;

import org.model.User;
import org.util.Constants;

import java.sql.*;

public class UserRepository implements AutoCloseable {

    private Connection conn;

    public UserRepository() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.conn = DriverManager.getConnection(Constants.DB_URL, Constants.USERNAME, Constants.PASSWORD);
        } catch (Exception ignored) {
        }
    }

    public boolean add(User user) {
        String sql = "insert into users(login,password,name,reg_date) values (?,?,?,?)";
        try(PreparedStatement preparedStatement = this.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setDate(4, (Date) user.getRegDate());

            int row = preparedStatement.executeUpdate();
            if (row <= 0)
                return false;
            try(ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next())
                    user.setId(generatedKeys.getInt(1));
            }
            return true;
        } catch (SQLException ignored) {
        }
        return false;
    }

    public User getById(int id) {
        String sql = "select * from users where users.id=?";
        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next())
                return null;
            User trainer = new User();
            trainer.setId(resultSet.getInt(1));
            trainer.setLogin(resultSet.getString(2));
            trainer.setPassword(resultSet.getString(3));
            trainer.setName(resultSet.getString(4));
            trainer.setRegDate(resultSet.getDate(5));
            return trainer;
        } catch (SQLException e) {
        }
        return null;
    }

    public boolean delete(User user) {
        String sql = "delete from users where users.id=?";
        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, user.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ignored) {
        }
        return false;
    }




    @Override
    public void close() throws Exception {
        if (this.conn != null)
            try {
                this.conn.close();
            } catch (SQLException ignored) {
            }
    }
}
