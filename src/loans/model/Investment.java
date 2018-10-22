/*
 * LB Loan Management API
 * API for loan management
 *
 * OpenAPI spec version: 0.0.1
 * Contact: raynard.eiger@outlook.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package loans.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
public class Investment implements Validatable {

	public static final String AMOUNT_INVALID = "[Investment amount has to be above zero.]";
	public static final String LOAN_EMPTY = "[Loan must be specified for an investment.]";
	public static final String INVESTOR_EMPTY = "[Investor must be specified for an investment.]";
	public static final String START_DATE_EMPTY = "[Start date must be specified for an investment.]";
	public static final String END_DATE_EMPTY = "[End date must be specified for an investment.]";
	public static final String END_DATE_BEFORE_START = "[End date has to be after start date.]";

	@JsonProperty("id")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id = null;

	@JsonProperty("loan")
	@JsonBackReference("loan_investment")
	@ManyToOne(
      cascade = CascadeType.ALL
  )
	private Loan loan = null;
	
	@JsonProperty("investor")
	@JsonBackReference("investor_investment")
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "investor_id", nullable = false)
	private Investor investor = null;

	@JsonProperty("startDate")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate startDate = null;

	@JsonProperty("endDate")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate endDate = null;
	
	@JsonProperty("amount")
	private Double amount;

	public Investment id(UUID id) {
		this.id = id;
		return this;
	}

	@Schema(example = "d290f1ee-6c54-4b01-90e6-d701748f0851", required = true, description = "")
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Investment loan(Loan loan) {
		this.loan = loan;
		return this;
	}

	@Schema(required = true, description = "")
	public Loan getLoan() {
		return loan;
	}

	public void setLoan(Loan loan) {
		this.loan = loan;
	}
	
	public Investment investor(Investor investor) {
		this.investor = investor;
		return this;
	}

	@Schema(required = true, description = "")
	public Investor getInvestor() {
		return investor;
	}

	public void setInvestor(Investor investor) {
		this.investor = investor;
	}

	public Investment startDate(LocalDate startDate) {
		this.startDate = startDate;
		return this;
	}

	@Schema(required = true, description = "")
	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public Investment endDate(LocalDate endDate) {
		this.endDate = endDate;
		return this;
	}

	@Schema(required = true, description = "")
	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	
	public Investment amount(Double amount) {
		this.amount = amount;
		return this;
	}

	@Schema(required = true, description = "")
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Investment investment = (Investment) o;
		return Objects.equals(this.id, investment.id) && Objects.equals(this.loan, investment.loan) && Objects.equals(this.investor, investment.investor) 
		    && Objects.equals(this.startDate, investment.startDate) && Objects.equals(this.endDate, investment.endDate);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(id, loan, investor, startDate, endDate);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Investment {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    loan: ").append(toIndentedString(loan)).append("\n");
		sb.append("    investor: ").append(toIndentedString(investor)).append("\n");
		sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
		sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}

	@Override
	public List<String> validate() {
		List<String> errors = new ArrayList<>();
		
		if (loan == null) {
			errors.add(LOAN_EMPTY);
		} else {
			errors.addAll(loan.validate());
		}
		
		if (investor == null) {
			errors.add(INVESTOR_EMPTY);
		}
		
		if (startDate == null) {
			errors.add(START_DATE_EMPTY);
		}
		
		if (endDate == null) {
			errors.add(END_DATE_EMPTY);
		}
		
		if (startDate != null && endDate != null && !endDate.isAfter(startDate)) {
			errors.add(END_DATE_BEFORE_START);
		}
		
		if (amount == null || amount <= 0) {
			errors.add(AMOUNT_INVALID);
		}
		
		return errors;
	}

}