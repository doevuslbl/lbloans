package loans.api;

import static java.util.Collections.singletonList;
import static org.javamoney.calc.common.SimpleInterest.of;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.money.Monetary;
import javax.money.MonetaryAmount;

import org.javamoney.calc.common.RateAndPeriods;
import org.javamoney.calc.common.SimpleInterest;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ibm.icu.math.BigDecimal;

import loans.model.ErrorResponse;
import loans.model.Investment;
import loans.model.InvestorAmountsDue;
import loans.model.InvestorInterestRequest;
import loans.model.Loan;
import loans.respository.InvestmentRespository;
import loans.respository.InvestorRespository;
import loans.respository.LoanRespository;

@RestController
public class InvestmentApi {
	
	@Autowired private LoanRespository loanRepository;
	@Autowired private InvestmentRespository investmentRepository;
	@Autowired private InvestorRespository investorRepository;
	
	ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
	
	Logger logger = Logger.getLogger(InvestmentApi.class);

	@RequestMapping(value = "/investments", method = RequestMethod.POST)
	public ResponseEntity<Object> create(@RequestBody String investmentJson) {
		try {
			Investment investment = mapper.readValue(investmentJson, Investment.class);
			
			// to avoid requiring different models, for now, we expect existing models to just have IDs provided. We can then load them at this point.
			if (investment.getLoan() != null) {
				Optional<Loan> optionalLoan = loanRepository.findById(investment.getLoan().getId());
				optionalLoan.ifPresent(loan -> {investment.setLoan(loan); loan.addInvestmentItem(investment);});
				
				if (!optionalLoan.isPresent()) {
					investment.setLoan(null); // for now, this simplifies validation that happens later in investment.validate()
				}
			}
			
			List<String> errors = investment.validate();
			
			if (!errors.isEmpty()) {
				ErrorResponse errorResponse = new ErrorResponse(errors);
				return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);
			}
			
			loanRepository.save(investment.getLoan());
			investorRepository.save(investment.getInvestor());
			investmentRepository.save(investment);
			
			logger.info("Successfully saved investment with ID [" + investment.getId() + "] for investor [" + investment.getInvestor().getId() + "] to the database.");
			System.out.println("Successfully saved investment with ID [" + investment.getId() + "] for investor [" + investment.getInvestor().getId() + "] to the database.");
			
			return ResponseEntity.status(HttpStatus.CREATED).body(investment);
		} catch (Exception e) {
			logger.error("Failed to create investment.", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(singletonList(e.getMessage())));
		}
	}
	
	@RequestMapping(value = "/interest/investors", method = RequestMethod.POST)
	public ResponseEntity<Object> calculateInterestForInvestors(@RequestBody String requestJson) {
		try {
			InvestorInterestRequest investorInterestRequest = mapper.readValue(requestJson, InvestorInterestRequest.class);
			investorRepository.findById(investorInterestRequest.getInvestorIds().get(0));
			List<InvestorAmountsDue> amountsDue = investorInterestRequest.getInvestorIds()
														 .stream()
														 .map(investorId -> investorRepository.findById(investorId))
														 .flatMap(optional -> optional.map(Stream::of).orElseGet(Stream::empty))
														 .map(investor -> {
															 Map<String, Double> amountsDuePerCurrency = new HashMap<>();
															 investor.getInvestments().forEach(investment -> {
																 
																 Loan loan = investment.getLoan();
																 
																 // ideally, should check that the request dates are fully within the loan period
																 int daysToCalculate = (int) (investorInterestRequest.getEndDate().toEpochDay() - investorInterestRequest.getStartDate().toEpochDay());
																 
																 // we presume only one rate is present. Ideally multiple different rate periods should be allowed
																 SimpleInterest interest = of(RateAndPeriods.of(BigDecimal.valueOf(loan.getRates().get(0).getRate())
																		 																											.divide(BigDecimal.valueOf(100), 4, 4) // rate is specified as %, so needs dividing by 100
																		 																											.divide(BigDecimal.valueOf(365), 7, 4)
																		 																											.doubleValue(), daysToCalculate));
																 
																 
																 MonetaryAmount amountDue = interest.apply(Monetary.getDefaultAmountFactory()
																		 																							 .setCurrency(loan.getCurrency())
																		 																							 .setNumber(investment.getAmount())
																		 																							 .create());
																 
																 if (amountsDuePerCurrency.containsKey(loan.getCurrency())) {
																	 amountsDuePerCurrency.put(loan.getCurrency(), amountsDuePerCurrency.get(loan.getCurrency()) + amountDue.getNumber().doubleValue());
																 } else {
																	 amountsDuePerCurrency.put(loan.getCurrency(), amountDue.getNumber().doubleValue());
																 }
																 
															 });
															 return new InvestorAmountsDue().amountsDuePerCurrency(amountsDuePerCurrency).investorId(investor.getId());
														 })
														 .collect(Collectors.toList());
		
			return ResponseEntity.ok(amountsDue);
		} catch (Exception e) {
			logger.error("Failed to determine interest amounts due.", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(singletonList(e.getMessage())));
		}
	}
}
