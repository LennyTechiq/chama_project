package com.lenstech.chamafullstackproject.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.lenstech.chamafullstackproject.model.Accounts;
import com.lenstech.chamafullstackproject.model.CycleCount;
import com.lenstech.chamafullstackproject.model.M_Group;
import com.lenstech.chamafullstackproject.model.State;
import com.lenstech.chamafullstackproject.model.User;
import com.lenstech.chamafullstackproject.repository.AccountsRepository;
import com.lenstech.chamafullstackproject.repository.CycleRepository;
import com.lenstech.chamafullstackproject.repository.UserRepository;

@Component
public class Algorithm {
	
	private UserRepository userRepository;
	private AccountsRepository accountsRepository;
	//private CycleRepository cycleRepository;
	
	public Algorithm(UserRepository userRepository, AccountsRepository accountsRepository, CycleRepository cycleRepository) {
		this.userRepository = userRepository;
		this.accountsRepository = accountsRepository;
		//this.cycleRepository = cycleRepository;
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
		
		int size = group1Members.size();
		int group1SubAmount = 300;
		
		if(checkGroup1MembersBalance().equals("Success")) {
			for(User user : group1Members) {
				if(user.getGiven_amount() < size * group1SubAmount) {
						Long balance = user.getBalance() - user.getSub_amount();
						Long given_amount = user.getGiven_amount() + user.getSub_amount();
						Long accountBalance = account.getBalance();
						//CycleCount cycleCount = cycleRepository.findByGroupName("group1");
						
						user.setBalance(balance);
						user.setGiven_amount(given_amount);
						
						accountBalance += user.getSub_amount();
						account.setBalance(accountBalance);
						
						/**Long count = cycleCount.getCount();
						System.out.println(count);
						cycleCount.setCount(count++);**/
						
						//cycleRepository.save(cycleCount);
						userRepository.save(user);
						accountsRepository.save(account);
				} 
			} 
			
			//message = "Succeeded";
			
		} else if(checkGroup1MembersBalance().equals("Fail")) {
			
			message = "Failed";
		}
				
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
		
		int size = group2Members.size();
		int group2SubAmount = 600;
		
		if(checkGroup2MembersBalance().equals("Success")) {
			for(User user : group2Members) {
				if(user.getGiven_amount() < size * group2SubAmount) {
						Long balance = user.getBalance() - user.getSub_amount();
						Long given_amount = user.getGiven_amount() + user.getSub_amount();
						Long accountBalance = account.getBalance();
						//
						
						user.setBalance(balance);
						user.setGiven_amount(given_amount);
						
						accountBalance += user.getSub_amount();
						account.setBalance(accountBalance);
						
						/**Long count = cycleCount.getCount();
						cycleCount.setCount(count++);**/
						
						userRepository.save(user);
						accountsRepository.save(account);
						//cycleRepository.save(cycleCount);
				} 
			} 
			
			//message = "Succeeded";
			
		} else if(checkGroup2MembersBalance().equals("Fail")) {
			
			message = "Failed";
		}
				
		return message;
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
				Long bal = user.getBalance() - 30;
				user.setBalance(bal);
				userRepository.save(user);
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
				Long bal = user.getBalance() - 60;
				user.setBalance(bal);
				userRepository.save(user);
				status = "Fail";
			}
		}
				
		return status;
	}
}
