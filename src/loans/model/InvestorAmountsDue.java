package loans.model;

import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InvestorAmountsDue {

	@JsonProperty("investorId")
	private UUID investorId;
	
	@JsonProperty("amountsDuePerCurrency")
	private Map<String, Double> amountsDuePerCurrency;
	
	public InvestorAmountsDue investorId(UUID investorId) {
		this.investorId = investorId;
		return this;
	}

	public UUID getInvestorId() {
		return investorId;
	}

	public void setInvestorId(UUID investorId) {
		this.investorId = investorId;
	}

	public InvestorAmountsDue amountsDuePerCurrency(Map<String, Double> amountsDuePerCurrency) {
		this.amountsDuePerCurrency = amountsDuePerCurrency;
		return this;
	}

	public Map<String, Double> getAmountsDuePerCurrency() {
		return amountsDuePerCurrency;
	}

	public void setAmountsDuePerCurrency(Map<String, Double> amountsDuePerCurrency) {
		this.amountsDuePerCurrency = amountsDuePerCurrency;
	}
}