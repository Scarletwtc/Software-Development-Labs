package ex1;

import java.util.Objects;

public class Person {
    private int id;
    private String name;
    private String job;
    private int addressId; // foreign key to Address

    public Person(String name, String job, int addressId) {
        this.name = name;
        this.job = job;
        this.addressId = addressId;
    }

    public Person(int id, String name, String job, int addressId) {
        this.id = id;
        this.name = name;
        this.job = job;
        this.addressId = addressId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getJob() {
        return job;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        return id == person.id && addressId == person.addressId &&
                Objects.equals(name, person.name) &&
                Objects.equals(job, person.job);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, job, addressId);
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", job='" + job + '\'' +
                ", addressId=" + addressId +
                '}';
    }
}
