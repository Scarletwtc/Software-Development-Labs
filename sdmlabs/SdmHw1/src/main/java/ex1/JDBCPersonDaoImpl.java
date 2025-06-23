package ex1;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCPersonDaoImpl extends CoreJDBCDao implements PersonDAO {

    @Override
    public List<Person> findAll() {
        List<Person> people = new ArrayList<>();
        String sql = "SELECT * FROM persons";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Person person = new Person(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("job"),
                        rs.getInt("address")
                );
                people.add(person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return people;
    }

    @Override
    public List<Person> findByJob(String job) {
        List<Person> people = new ArrayList<>();
        String sql = "SELECT * FROM persons WHERE job=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, job);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Person person = new Person(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("job"),
                            rs.getInt("address")
                    );
                    people.add(person);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return people;
    }

    @Override
    public Person insert(Person person) {
        String sql = "INSERT INTO persons(name, job, address) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, person.getName());
            ps.setString(2, person.getJob());
            ps.setInt(3, person.getAddressId());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    person.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return person;
    }

    @Override
    public void update(Person person) {
        String sql = "UPDATE persons SET name=?, job=?, address=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, person.getName());
            ps.setString(2, person.getJob());
            ps.setInt(3, person.getAddressId());
            ps.setInt(4, person.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int personId) {
        String sql = "DELETE FROM persons WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, personId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
