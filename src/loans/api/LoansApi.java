package loans.api;

import static java.util.Collections.singletonList;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import loans.model.ErrorResponse;
import loans.model.Loan;
import loans.respository.CustomerRespository;
import loans.respository.InvestmentRespository;
import loans.respository.InvestorRespository;
import loans.respository.LoanRespository;

@RestController
public class LoansApi {
	
	@Autowired private LoanRespository loanRepository;
	@Autowired private CustomerRespository customerRepository;
	@Autowired private InvestmentRespository investmentRepository;
	@Autowired private InvestorRespository investorRepository;
	
	Logger logger = Logger.getLogger(LoansApi.class);


	@RequestMapping(value = "/loans", method = RequestMethod.POST)
	public ResponseEntity<Object> create(@RequestBody String loanJson) {
		try {
			ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
			
			Loan loan = mapper.readValue(loanJson, Loan.class);

			List<String> errors = loan.validate();
			if (!errors.isEmpty()) {
				ErrorResponse errorResponse = new ErrorResponse(errors);
				return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);
			}
			
			// currently we presume all customers are new customers
			customerRepository.save(loan.getCustomer());
			if (loan.getInvestments() != null) {
				loan.getInvestments().forEach(investment -> { investment.setLoan(loan);
																											investorRepository.save(investment.getInvestor());
																											investmentRepository.save(investment); });
			}

			loanRepository.save(loan);
			
			logger.info("Successfully saved loan with ID [" + loan.getId() + "] to the database.");
			
			return ResponseEntity.status(HttpStatus.CREATED).body(loan);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(singletonList(e.getMessage())));
		}
	}
	
	
	@RequestMapping(value = "/loans/{loanId}", method = RequestMethod.GET)
	public ResponseEntity<Loan> retrieveLoan(@PathVariable UUID loanId) {
		Optional<Loan> optionalLoan = loanRepository.findById(loanId);
		return optionalLoan.map(loan -> ResponseEntity.ok(loan))
											 .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	
	@RequestMapping(value = "/loans/{loanId}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deleteLoan(@PathVariable UUID loanId) {
		Optional<Loan> optionalLoan = loanRepository.findById(loanId);
		
		if (optionalLoan.isPresent()) {
			loanRepository.deleteById(loanId);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
}
