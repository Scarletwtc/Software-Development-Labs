package ex1;

import java.sql.*;

public class Main {
  public static void main(String[] args) {
    String url = "jdbc:mariadb://localhost:3306/jdbcex";
    String city = "Bucuresti";
    String getPersonsFromCitySQL = "SELECT p.name FROM persons p"
            + " JOIN addresses a ON p.address=a.id WHERE a.city = ?";
    try (
            Connection con = DriverManager.getConnection(url, "root", "root");
            PreparedStatement getPersonsFromCity = con.prepareStatement(getPersonsFromCitySQL);
    ) {
      //so setString(1,city) means replace the first "?" with city
      //JOIN addresses a ON p.address = a.id
      //WHERE a.city = 'Bucuresti';
      getPersonsFromCity.setString(1, city);
      ResultSet rs = getPersonsFromCity.executeQuery();
      while (rs.next())
        System.out.println(rs.getString("name"));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
