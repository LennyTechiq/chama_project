package com.lenstech.chamafullstackproject.service;

import org.springframework.stereotype.Service;

import com.lenstech.chamafullstackproject.model.Accounts;
import com.lenstech.chamafullstackproject.repository.AccountsRepository;

@Service
public class AccountsService {
	private AccountsRepository accountsRepository;

	public AccountsService(AccountsRepository accountsRepository) {
		this.accountsRepository = accountsRepository;
	}

	public Accounts getAccounts(String name) {
		return accountsRepository.findByName(name);
	}
}
