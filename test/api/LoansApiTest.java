package api;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import loans.api.LoansApi;
import loans.model.Customer;
import loans.model.ErrorResponse;
import loans.model.Investment;
import loans.model.Loan;
import loans.model.Loan.Frequency;
import loans.model.Rate;
import loans.respository.CustomerRespository;
import loans.respository.LoanRespository;

@RunWith(MockitoJUnitRunner.class)
public class LoansApiTest {

	@InjectMocks private LoansApi loansApi = new LoansApi();
	
	@Mock private LoanRespository loanRepository;
	@Mock private CustomerRespository customerRepository;
	
	@Captor ArgumentCaptor<Loan> loanCaptor;
	@Captor ArgumentCaptor<Customer> customerCaptor;
	
	private static final UUID LOAN_UUID = UUID.fromString("91f99fc3-c0b7-4ce4-b7a5-3b69726d6ad5");
	
	@Test
	public void testCreateValidLoan() throws Exception {
		LocalDate startDate = LocalDate.of(2018, 8, 15);

		Loan loan = new Loan().rates(Collections.singletonList(new Rate().rate(3.75).startDate(startDate)))
													.periods(12)
													.frequency(Frequency.MONTHLY)
													.loanAmount(10000.00)
													.currency("GBP")
													.startDate(startDate)
													.endDate(startDate.plusMonths(12))
													.customer(new Customer().firstName("Jonathan").lastName("Smith").creditScore(750));
		
		ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
		
		ResponseEntity<Object> responseEntity = loansApi.create(mapper.writeValueAsString(loan));
		
		assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.CREATED));
		
		verify(loanRepository).save(loanCaptor.capture());
		verify(customerRepository).save(customerCaptor.capture());
		
		assertThat(loanCaptor.getValue().getStartDate(), equalTo(startDate)); // ideally should check more properties
		assertThat(customerCaptor.getValue().getFirstName(), equalTo("Jonathan")); // ideally should check more properties
	}
	
	@Test
	public void testEmptyInput() throws JsonProcessingException {
		ResponseEntity<Object> response = loansApi.create("{}");
		
		assertThat(response.getStatusCode(), equalTo(HttpStatus.UNPROCESSABLE_ENTITY));
		ErrorResponse errorResponse = (ErrorResponse) response.getBody();
		assertThat(errorResponse.getErrors().size(), equalTo(8));
		assertTrue(errorResponse.getErrors().contains(Loan.CUSTOMER_EMPTY)); // ideally should check more properties
		
		verifyZeroInteractions(loanRepository);
		verifyZeroInteractions(customerRepository);
	}
	
	@Test
	public void testMissingRates() throws Exception {
		LocalDate startDate = LocalDate.of(2018, 8, 15);
		Loan loan = new Loan().periods(12)
													.frequency(Frequency.MONTHLY)
													.loanAmount(10000.00)
													.currency("GBP")
													.startDate(startDate)
													.endDate(startDate.plusMonths(12))
													.customer(new Customer().firstName("Jonathan").lastName("Smith").creditScore(750));
		
		ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
		
		ResponseEntity<Object> responseEntity = loansApi.create(mapper.writeValueAsString(loan));
		
		assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.UNPROCESSABLE_ENTITY));
		
		ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
		assertThat(errorResponse.getErrors().size(), equalTo(1));
		assertTrue(errorResponse.getErrors().contains(Loan.RATES_EMPTY));
		
		verifyZeroInteractions(loanRepository);
		verifyZeroInteractions(customerRepository);
	}
	
	
	@Test
	public void testInvestmentsMoreThanLoanValue() throws Exception {
		Loan loan = new Loan();
		loan.setRates(Collections.singletonList(new Rate().rate(3.75).startDate(LocalDate.of(2018, 8, 15))));
		loan.setPeriods(12);
		loan.setFrequency(Frequency.MONTHLY);
		loan.setLoanAmount(10000.00);
		loan.setCurrency("GBP");
		loan.setStartDate(LocalDate.of(2018, 8, 15));
		loan.setEndDate(LocalDate.of(2019, 8, 14));
		loan.setCustomer(new Customer().firstName("Jonathan").lastName("Smith").creditScore(750));
		ArrayList<Investment> investments = new ArrayList<>();
		investments.add(new Investment().amount(50000.00));
		investments.add(new Investment().amount(70000.00)); // 70k + 50k > 100k
		loan.setInvestments(investments);
		
		ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
		
		ResponseEntity<Object> responseEntity = loansApi.create(mapper.writeValueAsString(loan));
		
		assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.UNPROCESSABLE_ENTITY));
		
		ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
		assertThat(errorResponse.getErrors().size(), equalTo(1));
		assertTrue(errorResponse.getErrors().contains(Loan.INVESTMENTS_MORE_THAN_LOAN));
		
		verifyZeroInteractions(loanRepository);
		verifyZeroInteractions(customerRepository);
	}
	
	
	@Test
	public void testRetrieveExistingLoan() {
		Loan existingLoan = mock(Loan.class);
		when(loanRepository.findById(LOAN_UUID)).thenReturn(Optional.of(existingLoan));
		
		ResponseEntity<Loan> responseEntity = loansApi.retrieveLoan(LOAN_UUID);
		
		assertThat(responseEntity.getBody(), equalTo(existingLoan));
		assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
	}
	
	@Test
	public void testRetrieveNonExistingLoan() {
		when(loanRepository.findById(LOAN_UUID)).thenReturn(Optional.empty());
		
		ResponseEntity<Loan> responseEntity = loansApi.retrieveLoan(LOAN_UUID);
		
		assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
	}
	
	@Test
	public void testDeleteExistingLoan() {
		Loan existingLoan = mock(Loan.class);
		when(loanRepository.findById(LOAN_UUID)).thenReturn(Optional.of(existingLoan));
		
		ResponseEntity<Object> responseEntity = loansApi.deleteLoan(LOAN_UUID);
		
		assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.NO_CONTENT));
	}
	
	@Test
	public void testAttemptDeleteNonExistingLoan() {
		when(loanRepository.findById(LOAN_UUID)).thenReturn(Optional.empty());
		
		ResponseEntity<Object> responseEntity = loansApi.deleteLoan(LOAN_UUID);
		
		assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
	}
}
