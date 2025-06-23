package ex2;

import java.sql.Connection;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        JDBCAddressDaoImpl addressDao=new JDBCAddressDaoImpl();
        Set<Address> addresses=addressDao.findAll();
        addresses.stream().forEach(System.out::println);
        Address newAddress = new Address("Iasi","Principala");
        addressDao.insert(newAddress);
        System.out.println("==============================");
        addresses=addressDao.findAll();
        addresses.stream().forEach(System.out::println);
        addressDao.closeConnection();
    }
}
