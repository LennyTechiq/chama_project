package com.lenstech.chamafullstackproject.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.lenstech.chamafullstackproject.dto.UserDto;
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
    private CycleRepository cycleRepository;
    
    // Constructor injection of repositories
    public Algorithm(UserRepository userRepository, AccountsRepository accountsRepository, CycleRepository cycleRepository) {
        this.userRepository = userRepository;
        this.accountsRepository = accountsRepository;
        this.cycleRepository = cycleRepository;
    }
    
    // Randomly select a member from Group 1 who has not been cycled yet
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
    
    // Randomly select a member from Group 2 who has not been cycled yet
    public User cycle2Randomizer() {
        List<User> members = userRepository.findByState(State.Not_Cycled);
        List<User> toCycle2List = new ArrayList<>();
        
        for(User member : members) {
            if(member.getGroup().equals(M_Group.Group_2)) {
                toCycle2List.add(member);
            }
        }
        
        Random rand = new Random();
        User user = null;

        if(!toCycle2List.isEmpty()) {
            user = toCycle2List.get(rand.nextInt(toCycle2List.size()));
        }
        
        return user;
    }
    
    // Credit members of Group 1 based on certain conditions
    public String creditGroup1() {
        // Fetch all members
        List<User> members = userRepository.findAll();
        // Find the account for Group 1
        Accounts account = accountsRepository.findByName("group1");
        String message = null;
        List<User> group1Members = new ArrayList<>();
        
        // Filter members belonging to Group 1
        for(User user : members) {
            if(user.getGroup().equals(M_Group.Group_1)) {
                group1Members.add(user);
            }
        }
        
        // Calculate the size of Group 1
        int size = group1Members.size();
        int group1SubAmount = 300;
        
        // Check if all members of Group 1 have sufficient balance
        if(checkGroup1MembersBalance().equals("Success")) {
            for(User user : group1Members) {
                if(user.getGiven_amount() < size * group1SubAmount) {
                    // Update user balance and given amount
                    Long balance = user.getBalance() - user.getSub_amount();
                    Long given_amount = user.getGiven_amount() + user.getSub_amount();
                    Long accountBalance = account.getBalance();
                    
                    user.setBalance(balance);
                    user.setGiven_amount(given_amount);
                    
                    // Update account balance
                    accountBalance += user.getSub_amount();
                    account.setBalance(accountBalance);
                    
                    userRepository.save(user);
                    accountsRepository.save(account);
                } 
            } 
        } else if(checkGroup1MembersBalance().equals("Fail")) {
            message = "Failed";
        }
                
        return message;
    }
    
    // Credit members of Group 2 based on certain conditions
    public String creditGroup2() {
        // Fetch all members
        List<User> members = userRepository.findAll();
        // Find the account for Group 2
        Accounts account = accountsRepository.findByName("group2");
        String message = null;
        List<User> group2Members = new ArrayList<>();
        
        // Filter members belonging to Group 2
        for(User user : members) {
            if(user.getGroup().equals(M_Group.Group_2)) {
                group2Members.add(user);
            }
        }
        
        // Calculate the size of Group 2
        int size = group2Members.size();
        int group2SubAmount = 600;
        
        // Check if all members of Group 2 have sufficient balance
        if(checkGroup2MembersBalance().equals("Success")) {
            for(User user : group2Members) {
                if(user.getGiven_amount() < size * group2SubAmount) {
                    // Update user balance and given amount
                    Long balance = user.getBalance() - user.getSub_amount();
                    Long given_amount = user.getGiven_amount() + user.getSub_amount();
                    Long accountBalance = account.getBalance();
                    
                    user.setBalance(balance);
                    user.setGiven_amount(given_amount);
                    
                    // Update account balance
                    accountBalance += user.getSub_amount();
                    account.setBalance(accountBalance);
                    
                    userRepository.save(user);
                    accountsRepository.save(account);
                } 
            } 
        } else if(checkGroup2MembersBalance().equals("Fail")) {
            message = "Failed";
        }
                
        return message;
    }
    
    // Check the balance of members in Group 1 and update their status accordingly
    public String checkGroup1MembersBalance() {
        List<User> members = userRepository.findAll();
        String status = "Success";
        List<User> group1Members = new ArrayList<>();
        
        // Filter members belonging to Group 1
        for(User user : members) {
            if(user.getGroup().equals(M_Group.Group_1)) {
                group1Members.add(user);
            }
        }
        
        // Set all Group 1 members to inactive
        for(User user : group1Members) {
            user.setActive(false);
            userRepository.save(user);
        }
        
        // Check balance of each Group 1 member
        for(User user : group1Members) {
            if(user.getBalance() >= user.getSub_amount()) {
                user.setActive(true);
                userRepository.save(user);
            } else {
                // Deduct penalty from balance and set status to fail
                Long bal = user.getBalance() - 30;
                user.setBalance(bal);
                userRepository.save(user);
                status = "Fail";
            }
        }
                
        return status;
    }
    
    // Check the balance of members in Group 2 and update their status accordingly
    public String checkGroup2MembersBalance() {
        List<User> members = userRepository.findAll();
        String status = "Success";
        List<User> group2Members = new ArrayList<>();
        
        // Filter members belonging to Group 2
        for(User user : members) {
            if(user.getGroup().equals(M_Group.Group_2)) {
                group2Members.add(user);
            }
        }
        
        // Set all Group 2 members to inactive
        for(User user : group2Members) {
            user.setActive(false);
            userRepository.save(user);
        }
        
        // Check balance of each Group 2 member
        for(User user : group2Members) {
            if(user.getBalance() >= user.getSub_amount()) {
                user.setActive(true);
                userRepository.save(user);
            } else {
                // Deduct penalty from balance and set status to fail
                Long bal = user.getBalance() - 60;
                user.setBalance(bal);
                userRepository.save(user);
                status = "Fail";
            }
        }
                
        return status;
    }
    
    // Check the eligibility of a new member to join Group 1 based on their balance
    public String checkNewMemberGroup1(String group, String email) {
        String message1 = null;
        
        User user = userRepository.findByEmail(email);
        CycleCount cycleCount = cycleRepository.findByGroupName(group);
        
        if(cycleCount != null) {
        	Long count = cycleCount.getCount();
            
            if(user.getBalance() >= (900 + count*300)) {
                user.setGroup(M_Group.Group_1);
                user.setSub_amount(300);
                userRepository.save(user);
            } else {
                message1 = "Fail";
            }
        }
        
        return message1;
    }
    
 // Check the eligibility of a new member to join Group 2 based on their balance
    public String checkNewMemberGroup2(String group, String email) {
        String message1 = null;
        
        User user = userRepository.findByEmail(email);
        CycleCount cycleCount = cycleRepository.findByGroupName(group);
        
        if(cycleCount != null) {
        	Long count = cycleCount.getCount();
            
            if(user.getBalance() >= (1800 + count*600)) {
                user.setGroup(M_Group.Group_2);
                user.setSub_amount(600);
                userRepository.save(user);
            } else {
                message1 = "Fail";
            }
        }
        
        return message1;
    }
}
