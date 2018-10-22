package loans.respository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import loans.model.Rate;

public interface RateRespository extends CrudRepository<Rate, UUID> {

}
