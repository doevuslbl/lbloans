package loans.respository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import loans.model.Investor;

public interface InvestorRespository extends CrudRepository<Investor, UUID> {

}
