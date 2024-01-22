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
	
	public String checkGroup1MembersBalance() {
		List<User> members = userRepository.findAll();
		String status = "Success";
		List<User> group1Members = new ArrayList<>();
		
		for(User user : members) {
			if(user.getGroup().equals(M_Group.Group_1)) {
				group1Members.add(user);
			}
		}
		
		for(User user : group1Members) {
			user.setActive(false);
			userRepository.save(user);
		}
		
		for(User user : group1Members) {
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
	
	public String checkGroup2MembersBalance() {
		List<User> members = userRepository.findAll();
		String status = "Success";
		List<User> group2Members = new ArrayList<>();
		
		for(User user : members) {
			if(user.getGroup().equals(M_Group.Group_2)) {
				group2Members.add(user);
			}
		}
		
		for(User user : group2Members) {
			user.setActive(false);
			userRepository.save(user);
		}
		
		for(User user : group2Members) {
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
	
	public String creditGroup1() {
		List<User> members = userRepository.findAll();
		Accounts account = accountsRepository.findByName("group1");
		String message = null;
		List<User> group1Members = new ArrayList<>();
		
		for(User user : members) {
			if(user.getGroup().equals(M_Group.Group_1)) {
				group1Members.add(user);
			}
		}
		
		if(checkGroup1MembersBalance().equals("Success")) {
			for(User user : group1Members) {
				if(user.getGiven_amount() < 1800) {
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
			
			//message = "Succeeded";
			
		} else if(checkGroup1MembersBalance().equals("Fail")) {
			
			message = "Failed";
		}
		
		//System.out.println(message);
		
		return message;
	}
	
	public String creditGroup2() {
		List<User> members = userRepository.findAll();
		Accounts account = accountsRepository.findByName("group2");
		String message = null;
		List<User> group2Members = new ArrayList<>();
		
		for(User user : members) {
			if(user.getGroup().equals(M_Group.Group_2)) {
				group2Members.add(user);
			}
		}
		
		if(checkGroup2MembersBalance().equals("Success")) {
			for(User user : group2Members) {
				if(user.getGiven_amount() < 1800) {
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
			
			//message = "Succeeded";
			
		} else if(checkGroup2MembersBalance().equals("Fail")) {
			
			message = "Failed";
		}
		
		//System.out.println(message);
		
		return message;
	}
}
