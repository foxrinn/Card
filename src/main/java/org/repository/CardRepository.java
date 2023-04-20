package org.repository;

import org.model.Card;
import org.model.Category;
import org.model.User;
import org.util.Constants;

import java.sql.*;

public class CardRepository implements AutoCloseable {

    private Connection conn;

    public CardRepository() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.conn = DriverManager.getConnection(Constants.DB_URL, Constants.USERNAME, Constants.PASSWORD);
        } catch (Exception ignored) {
        }
    }

    public Card getById(int id) {
        String sql = "select * from cards where cards.id=?";
        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next())
                return null;
            Card trainer = new Card();
            trainer.setId(resultSet.getInt(1));
            trainer.setQuestion(resultSet.getString(2));
            trainer.setAnswer(resultSet.getString(3));
            trainer.setIdCategory(resultSet.getInt(4));
            trainer.setCreationDate(resultSet.getDate(5));
            return trainer;
        } catch (SQLException e) {
        }
        return null;
    }

    public boolean delete(Card card) {
        String sql = "delete from cardss where cards.id=?";
        try(PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, card.getId());
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
