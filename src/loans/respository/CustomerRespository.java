package loans.respository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import loans.model.Customer;

public interface CustomerRespository extends CrudRepository<Customer, UUID> {

}
