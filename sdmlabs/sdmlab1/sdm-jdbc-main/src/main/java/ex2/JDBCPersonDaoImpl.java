package ex2;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class JDBCPersonDaoImpl extends CoreJDBCDao implements PersonDAO {
    @Override
    public Set<Person> findAll() {
        Set<Person> persons = new HashSet<>();
        String findAllPersonSQL = "SELECT * FROM persons";
        try (
                PreparedStatement findAllPerson = connection.prepareStatement(findAllPersonSQL)
        ) {
            ResultSet rs = findAllPerson.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int addressId = rs.getInt("address");

                // Fetch Address by ID
                Address address = null;
                String findAddressSQL = "SELECT * FROM addresses WHERE id = ?";
                try (PreparedStatement findAddress = connection.prepareStatement(findAddressSQL)) {
                    findAddress.setInt(1, addressId);
                    ResultSet addressRs = findAddress.executeQuery();
                    if (addressRs.next()) {
                        address = new Address(
                                addressRs.getInt("id"),
                                addressRs.getString("city"),
                                addressRs.getString("street")
                        );
                    }
                }

                Person p = new Person(id, name, address);
                persons.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return persons;
    }

    @Override
    public Set<Person>  findByName(String name) {
        String findByNamePersonSQL = "SELECT p.id AS person_id, p.name, a.id as address_id, a.city, a.street FROM persons p " +
                "JOIN addresses a ON p.address = a.id WHERE p.name = ?";
        Set<Person> persons = new HashSet<>();
        try(
                PreparedStatement findByNamePerson = connection.prepareStatement(findByNamePersonSQL);
                ){
            findByNamePerson.setString(1,name);
            ResultSet rs = findByNamePerson.executeQuery();
            while (rs.next()){
                int id = rs.getInt("person_id");
                String personName = rs.getString("name");
                int addressId = rs.getInt("address_id");
                String city = rs.getString("city");
                String street = rs.getString("street");

                Address address = new Address(addressId, city, street);
                Person p = new Person(id, personName, address);
                persons.add(p);
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return persons;
    }
    @Override
    public Set<Person> findByAddress(Address address) {
        String findByAddressSQL =" SELECT p.id as person_id, p.name, a.id AS address_id, a.city, a.street FROM persons p" +
                " JOIN addresses a ON p.address = a.id WHERE a.city =? AND a.street = ?";
        Set<Person> persons = new HashSet<>();
        try(
                PreparedStatement findByAddress = connection.prepareStatement(findByAddressSQL);
                ){
            findByAddress.setString(1,address.getCity());
            findByAddress.setString(2,address.getStreet());
            ResultSet rs = findByAddress.executeQuery();
            while (rs.next()){
                int id = rs.getInt("person_id");
                String personName = rs.getString("name");
                int addressId = rs.getInt("address_id");
                String city = rs.getString("city");
                String street = rs.getString("street");

                Address ad = new Address(addressId, city, street);
                Person p = new Person(id, personName, ad);
                persons.add(p);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return persons;
    }
    @Override
    public Person insert(Person person) {
        String insertPersonSQL = "INSERT INTO persons(name,address) VALUES(?,?)";
        try (
                PreparedStatement insertPerson = connection.prepareStatement(insertPersonSQL, Statement.RETURN_GENERATED_KEYS);
        ) {
            insertPerson.setString(1, person.getName());
            insertPerson.setInt(2, person.getAddress().getId());
            insertPerson.executeUpdate();
            var generatedKeys = insertPerson.getGeneratedKeys();
            if (generatedKeys.next()) {
                person.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return person;
    }
    @Override
    public Person insert2(Person person) {
        Address address = person.getAddress();
        int addressId = -1;
        String checkAddressSQL = "SELECT id FROM addresses WHERE city = ? AND street = ?";
        try (PreparedStatement checkAddress = connection.prepareStatement(checkAddressSQL)) {
            checkAddress.setString(1, address.getCity());
            checkAddress.setString(2, address.getStreet());
            ResultSet rs = checkAddress.executeQuery();
            if (rs.next()) {
                addressId = rs.getInt("id");
            } else {
                String insertAddressSQL = "INSERT INTO addresses(city, street) VALUES (?, ?)";
                try (PreparedStatement insertAddress = connection.prepareStatement(insertAddressSQL, Statement.RETURN_GENERATED_KEYS)) {
                    insertAddress.setString(1, address.getCity());
                    insertAddress.setString(2, address.getStreet());
                    insertAddress.executeUpdate();
                    ResultSet generatedKeys = insertAddress.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        addressId = generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        address.setId(addressId);
        String insertPersonSQL = "INSERT INTO persons(name, address) VALUES(?,?)";
        try (PreparedStatement insertPerson = connection.prepareStatement(insertPersonSQL, Statement.RETURN_GENERATED_KEYS)) {
            insertPerson.setString(1, person.getName());
            insertPerson.setInt(2, addressId);
            insertPerson.executeUpdate();
            ResultSet keys = insertPerson.getGeneratedKeys();
            if (keys.next()) {
                person.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return person;

    }
    @Override
    public void update(Person person) {
        String updatePersonSQL = "UPDATE persons SET name=?, address=? WHERE id=?";
        try(
                PreparedStatement updatePerson = connection.prepareStatement(updatePersonSQL);
                ){
            updatePerson.setString(1,person.getName());
            updatePerson.setInt(2,person.getAddress().getId());
            updatePerson.setInt(3,person.getId());
            updatePerson.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void delete(int personId) {
        String deletePersonSQL = "DELETE FROM persons WHERE id=?";
        try(
                PreparedStatement deletePerson = connection.prepareStatement(deletePersonSQL);
                ){
            deletePerson.setInt(1,personId);
            deletePerson.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
