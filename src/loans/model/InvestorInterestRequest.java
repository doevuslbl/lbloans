package loans.model;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class InvestorInterestRequest {

	@JsonProperty("startDate")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate startDate;
	
	@JsonProperty("endDate")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate endDate;
	
	@JsonProperty("investorIds")
	private List<UUID> investorIds;
	
	public InvestorInterestRequest startDate(LocalDate startDate) {
		this.startDate = startDate;
		return this;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	
	public InvestorInterestRequest endDate(LocalDate endDate) {
		this.endDate = endDate;
		return this;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	
	public InvestorInterestRequest investorIds(List<UUID> investorIds) {
		this.investorIds = investorIds;
		return this;
	}

	public List<UUID> getInvestorIds() {
		return investorIds;
	}

	public void setInvestorIds(List<UUID> investorIds) {
		this.investorIds = investorIds;
	}
}
