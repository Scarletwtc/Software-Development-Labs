package ex1;

import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        JDBCAddressDaoImpl addressDao = new JDBCAddressDaoImpl();
        JDBCPersonDaoImpl personDao = new JDBCPersonDaoImpl();
        JDBCCreditCardDaoImpl creditCardDao = new JDBCCreditCardDaoImpl();

        System.out.println(" All Addresses:");
        Set<Address> addresses = addressDao.findAll();
        addresses.forEach(System.out::println);

        Address newAddress = new Address("Iasi", "Principala");
        addressDao.insert(newAddress);
        System.out.println("\n New address inserted.");

        Person newPerson = new Person("Ali", "Accountant", newAddress.getId());
        personDao.insert(newPerson);
        System.out.println("New person inserted: " + newPerson.getName());

        CreditCard newCard = new CreditCard("RO49AAAA1B31007593840000", 3000.00, newPerson.getId());
        creditCardDao.insert(newCard);
        System.out.println("Credit card inserted for " + newPerson.getName());

        System.out.println("\n Updated Addresses:");
        addressDao.findAll().forEach(System.out::println);

        System.out.println("\n All Persons:");
        personDao.findAll().forEach(System.out::println);

        System.out.println("\n All Credit Cards:");
        creditCardDao.findAll().forEach(System.out::println);

        // ===== Close connections =====
        addressDao.closeConnection();
        personDao.closeConnection();
        creditCardDao.closeConnection();
    }
}
