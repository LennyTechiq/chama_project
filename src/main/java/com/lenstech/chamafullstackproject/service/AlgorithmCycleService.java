package com.lenstech.chamafullstackproject.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.lenstech.chamafullstackproject.model.Accounts;
import com.lenstech.chamafullstackproject.model.AlgorithmCycle;
import com.lenstech.chamafullstackproject.model.CycleState;
import com.lenstech.chamafullstackproject.model.State;
import com.lenstech.chamafullstackproject.model.User;
import com.lenstech.chamafullstackproject.repository.AccountsRepository;
import com.lenstech.chamafullstackproject.repository.AlgorithmCycleRepository;
import com.lenstech.chamafullstackproject.repository.UserRepository;

@Service
public class AlgorithmCycleService {
	
	private AlgorithmCycleRepository algorithmCycleRepository;
	private UserRepository userRepository;
	private AccountsRepository accountsRepository;

    public AlgorithmCycleService(
    		AlgorithmCycleRepository algorithmCycleRepository,
    		UserRepository userRepository,
    		AccountsRepository accountsRepository) {
		this.algorithmCycleRepository = algorithmCycleRepository;
		this.userRepository = userRepository;
		this.accountsRepository = accountsRepository;
	}

	public List<AlgorithmCycle> findAllUsers() {
        List<AlgorithmCycle> algorithmCycle = algorithmCycleRepository.findAll();
        return algorithmCycle;
    }
    
    public void addMember(User user) {
    	AlgorithmCycle cycleData = new AlgorithmCycle();
    	cycleData.setMember(user.getEmail());
    	cycleData.setState(CycleState.UnBenefitted);
    	algorithmCycleRepository.save(cycleData);
    }
    
    public void removeMember(Long id) {
    	Optional<AlgorithmCycle> member = algorithmCycleRepository.findById(id);
    	User user = userRepository.findByEmail(member.get().getMember());
    	
    	user.setState(State.Not_Cycled);
    	
    	userRepository.save(user);
    	algorithmCycleRepository.deleteById(id);
    }
    
    public AlgorithmCycle pay(Long id) {
    	Optional<AlgorithmCycle> member = algorithmCycleRepository.findById(id);
    	User user = userRepository.findByEmail(member.get().getMember());
    	
    	Accounts account = accountsRepository.findByName("main");
    	AlgorithmCycle member1 = member.get();
    	
    	Long balance = user.getBalance();
    	Long accountBalance = account.getBalance();
    	
    	if(accountBalance >= 3600) {
    		Long updatedBalance = balance + 3600;
        	accountBalance -= 3600;
        	user.setBalance(updatedBalance);
        	account.setBalance(accountBalance);
        	
        	member.get().setState(CycleState.Benefitted);
        	user.setSub_amount(600);
    	}
    	
    	accountsRepository.save(account);
    	userRepository.save(user);
    	
    	return algorithmCycleRepository.save(member1);
    }
	
}
