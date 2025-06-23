package ex1;

import java.util.List;

public interface PersonDAO {
    List<Person> findAll();
    List<Person> findByJob(String job);
    Person insert(Person person);
    void update(Person person);
    void delete(int personId);
}
