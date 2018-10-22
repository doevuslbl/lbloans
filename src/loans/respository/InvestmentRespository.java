package loans.respository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import loans.model.Investment;

public interface InvestmentRespository extends CrudRepository<Investment, UUID> {

}
