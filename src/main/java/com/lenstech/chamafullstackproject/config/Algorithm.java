package com.lenstech.chamafullstackproject.config;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.lenstech.chamafullstackproject.model.Accounts;
import com.lenstech.chamafullstackproject.model.State;
import com.lenstech.chamafullstackproject.model.User;
import com.lenstech.chamafullstackproject.repository.AccountsRepository;
import com.lenstech.chamafullstackproject.repository.UserRepository;

@Component
public class Algorithm {
	
	private UserRepository userRepository;
	private AccountsRepository accountsRepository;
	
	public Algorithm(UserRepository userRepository, AccountsRepository accountsRepository) {
		this.userRepository = userRepository;
		this.accountsRepository = accountsRepository;
	}
	
	public User randomUser() {
		List<User> members = userRepository.findByState(State.Not_Cycled);
		Random rand = new Random();
		User user = null;

		if(!members.isEmpty()) {
			user = members.get(rand.nextInt(members.size()));
		}
		
		return user;
	}
	
	public String checkMembersBalance() {
		List<User> members = userRepository.findAll();
		String status = "Success";
		
		for(User user : members) {
			user.setActive(false);
			userRepository.save(user);
		}
		
		for(User user : members) {
			if(user.getBalance() >= user.getSub_amount()) {
				user.setActive(true);
				userRepository.save(user);
			}
			else {
				status = "Fail";
			}
		}
		
		return status;
	}
	
	public String creditMembers() {
		List<User> members = userRepository.findAll();
		Accounts account = accountsRepository.findByName("main");
		String message = null;
		
		if(checkMembersBalance().equals("Success")) {
			for(User user : members) {
				if(user.getGiven_amount() < 3600) {
						Long balance = user.getBalance() - user.getSub_amount();
						Long given_amount = user.getGiven_amount() + user.getSub_amount();
						Long accountBalance = account.getBalance();
						
						user.setBalance(balance);
						user.setGiven_amount(given_amount);
						
						accountBalance += user.getSub_amount();
						account.setBalance(accountBalance);
						
						userRepository.save(user);
						accountsRepository.save(account);
				} 
			} 
			
			message = "Succeeded";
			
		} else if(checkMembersBalance().equals("Fail")) {
			
			message = "Failed";
		}
		
		return message;
	}
}
