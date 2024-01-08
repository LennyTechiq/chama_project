package com.lenstech.chamafullstackproject.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.lenstech.chamafullstackproject.model.Accounts;
import com.lenstech.chamafullstackproject.model.M_Group;
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
	
	public User cycle2Randomizer() {
		List<User> members = userRepository.findByState(State.Not_Cycled);
		List<User> toCycle1List = new ArrayList<>();
		
		for(User member : members) {
			if(member.getGroup().equals(M_Group.Group_2)) {
				toCycle1List.add(member);
			}
		}
		
		Random rand = new Random();
		User user = null;

		if(!toCycle1List.isEmpty()) {
			user = toCycle1List.get(rand.nextInt(toCycle1List.size()));
		}
		
		return user;
	}
	
	public User cycle1Randomizer() {
		List<User> members = userRepository.findByState(State.Not_Cycled);
		List<User> toCycle1List = new ArrayList<>();
		
		for(User member : members) {
			if(member.getGroup().equals(M_Group.Group_1)) {
				toCycle1List.add(member);
			}
		}
		
		Random rand = new Random();
		User user = null;

		if(!toCycle1List.isEmpty()) {
			user = toCycle1List.get(rand.nextInt(toCycle1List.size()));
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
