package ex1;

import java.util.List;

public interface CreditCardDAO {
    List<CreditCard> findAll();
    List<CreditCard> findByPersonId(int personId);
    CreditCard insert(CreditCard creditCard);
    void update(CreditCard creditCard);
    void delete(int creditCardId);
}
