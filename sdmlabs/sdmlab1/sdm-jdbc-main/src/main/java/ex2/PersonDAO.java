package ex2;

import java.util.Set;

public interface PersonDAO {
    Set<Person> findAll();
    Set<Person> findByName(String name);
    Set<Person> findByAddress(Address address);
    Person insert(Person person);
    Person insert2(Person person);
    void update(Person person);
    void delete(int personId);

}
