package org.repository;

import org.model.Category;
import org.model.User;
import org.util.Constants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository implements AutoCloseable {

    private Connection conn;

    public CategoryRepository() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.conn = DriverManager.getConnection(Constants.DB_URL, Constants.USERNAME, Constants.PASSWORD);
        } catch (Exception ignored) {
        }
    }

    public Category getById(int id) {
        String sql = "select * from categories where categories.id=?";
        try(PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next())
                return null;
            Category category = new Category();
            category.setId(resultSet.getInt(1));
            category.setName(resultSet.getString(2));
            category.setIdUser(resultSet.getInt(3));
            return category;
        } catch (SQLException ignored) {
        }
        return null;
    }

    public List<Category> getCategoriesByUserId(int idUser) {
        String sql = "select * from categories where categories.id_u=?";
        ArrayList<Category> categories = new ArrayList<>();
        try(PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, idUser);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Category category = new Category();
                category.setId(resultSet.getInt(1));
                category.setName(resultSet.getString(2));
                category.setIdUser(resultSet.getInt(3));

                categories.add(category);
            }
        } catch (SQLException e) {
        }
        return categories;
    }

    public boolean delete(Category category) {
        String sql = "delete from categories where categories.id=?";
        try(PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, category.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
        }
        return false;
    }

    public boolean add(Category category) {
        String sql = "insert into categories(name,id_u) values (?,?)";
        try(PreparedStatement preparedStatement = this.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, category.getName());
            preparedStatement.setInt(2, category.getIdUser());

            int row = preparedStatement.executeUpdate();
            if (row <= 0)
                return false;
            try(ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next())
                    category.setId(generatedKeys.getInt(1));
            }
            return true;
        } catch (SQLException ignored) {
        }
        return false;
    }

    public boolean update(Category category) {
        String sql = "update categories set categories.name=?, categories.id_u=? where categories.id=?";
        try(PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            preparedStatement.setString(1, category.getName());
            preparedStatement.setInt(2, category.getIdUser());
            preparedStatement.setInt(3, category.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
        }
        return false;
    }

    @Override
    public void close() {
        if (this.conn != null)
            try {
                this.conn.close();
            } catch (SQLException ignored) {
            }
    }
}
