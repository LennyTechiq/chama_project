package com.lenstech.chamafullstackproject.dto;

public class WithdrawForm {
	
	private Long phoneNumber;
	private Long withdrawalAmount;

	public Long getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(Long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Long getWithdrawalAmount() {
		return withdrawalAmount;
	}

	public void setWithdrawalAmount(Long withdrawalAmount) {
		this.withdrawalAmount = withdrawalAmount;
	}

}
