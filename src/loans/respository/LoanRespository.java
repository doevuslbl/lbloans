package loans.respository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import loans.model.Loan;

public interface LoanRespository extends CrudRepository<Loan, UUID> {

}
