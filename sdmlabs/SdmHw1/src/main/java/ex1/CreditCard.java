package ex1;

import java.util.Objects;

public class CreditCard {
    private int id;
    private String iban;
    private double amount;
    private int personId; // foreign key to Person

    public CreditCard(String iban, double amount, int personId) {
        this.iban = iban;
        this.amount = amount;
        this.personId = personId;
    }

    public CreditCard(int id, String iban, double amount, int personId) {
        this.id = id;
        this.iban = iban;
        this.amount = amount;
        this.personId = personId;
    }

    public int getId() {
        return id;
    }

    public String getIban() {
        return iban;
    }

    public double getAmount() {
        return amount;
    }

    public int getPersonId() {
        return personId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreditCard)) return false;
        CreditCard that = (CreditCard) o;
        return id == that.id &&
                Double.compare(that.amount, amount) == 0 &&
                personId == that.personId &&
                Objects.equals(iban, that.iban);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, iban, amount, personId);
    }

    @Override
    public String toString() {
        return "CreditCard{" +
                "id=" + id +
                ", iban='" + iban + '\'' +
                ", amount=" + amount +
                ", personId=" + personId +
                '}';
    }
}
