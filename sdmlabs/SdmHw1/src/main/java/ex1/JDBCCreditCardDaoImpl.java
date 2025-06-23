package ex1;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCCreditCardDaoImpl extends CoreJDBCDao implements CreditCardDAO {

    @Override
    public List<CreditCard> findAll() {
        List<CreditCard> cards = new ArrayList<>();
        String sql = "SELECT * FROM credit_cards";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                CreditCard card = new CreditCard(
                        rs.getInt("id"),
                        rs.getString("iban"),
                        rs.getDouble("amount"),
                        rs.getInt("person")
                );
                cards.add(card);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cards;
    }

    @Override
    public List<CreditCard> findByPersonId(int personId) {
        List<CreditCard> cards = new ArrayList<>();
        String sql = "SELECT * FROM credit_cards WHERE person=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, personId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CreditCard card = new CreditCard(
                            rs.getInt("id"),
                            rs.getString("iban"),
                            rs.getDouble("amount"),
                            rs.getInt("person")
                    );
                    cards.add(card);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cards;
    }

    @Override
    public CreditCard insert(CreditCard creditCard) {
        String sql = "INSERT INTO credit_cards(iban, amount, person) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, creditCard.getIban());
            ps.setDouble(2, creditCard.getAmount());
            ps.setInt(3, creditCard.getPersonId());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    creditCard.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return creditCard;
    }

    @Override
    public void update(CreditCard creditCard) {
        String sql = "UPDATE credit_cards SET iban=?, amount=?, person=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, creditCard.getIban());
            ps.setDouble(2, creditCard.getAmount());
            ps.setInt(3, creditCard.getPersonId());
            ps.setInt(4, creditCard.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int creditCardId) {
        String sql = "DELETE FROM credit_cards WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, creditCardId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
