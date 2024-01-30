package com.lenstech.chamafullstackproject.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lenstech.chamafullstackproject.config.Algorithm;
import com.lenstech.chamafullstackproject.dto.UserDto;
import com.lenstech.chamafullstackproject.model.Accounts;
import com.lenstech.chamafullstackproject.model.AlgorithmCycle;
import com.lenstech.chamafullstackproject.model.M_Group;
import com.lenstech.chamafullstackproject.model.User;
import com.lenstech.chamafullstackproject.repository.UserRepository;
import com.lenstech.chamafullstackproject.service.AccountsService;
import com.lenstech.chamafullstackproject.service.AlgorithmCycleService;
import com.lenstech.chamafullstackproject.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	private UserService userService;
	private Algorithm algorithm;
	private AlgorithmCycleService algorithmCycleService;
	private AccountsService accountsService;
	private UserRepository userRepository;
	
	public AdminController(
			UserService userService, 
			Algorithm algorithm,
			AlgorithmCycleService algorithmCycleService,
			AccountsService accountsService,
			UserRepository userRepository) {
		this.userService = userService;
		this.algorithm = algorithm;
		this.algorithmCycleService = algorithmCycleService;
		this.accountsService = accountsService;
		this.userRepository = userRepository;
	}

	@GetMapping("/dashboard")
	public String getAdminProfilePage(Model model) {
		List<UserDto> users = userService.findAllUsers();
		
		Accounts group1AccountBalance = accountsService.getAccounts("group1");
		Accounts group2AccountBalance = accountsService.getAccounts("group2");
		Long group1Balance = group1AccountBalance.getBalance();
		Long group2Balance = group2AccountBalance.getBalance();
		
		model.addAttribute("users", users);
		model.addAttribute("group1Balance", group1Balance);
		model.addAttribute("group2Balance", group2Balance);
		return "admin/dashboard";
	}
	
	@GetMapping("/members")
	public String getMembers(Model model) {
		List<UserDto> users = userService.findAllUsers();
		model.addAttribute("users", users);
		return "admin/members";
	}
	
	@GetMapping("/group-1")
	public String getGroup1(Model model) {
		List<UserDto> users = userService.findGroup1();
		model.addAttribute("users", users);
		return "admin/group-1-members";
	}
	
	@GetMapping("/group-2")
	public String getGroup2(Model model) {
		List<UserDto> users = userService.findGroup2();
		model.addAttribute("users", users);
		return "admin/group-2-members";
	}
	
	@GetMapping("/member/edit/{id}")
	public String getEditMember(@PathVariable("id") Long id, Model model) {
		UserDto member = userService.findById(id);
		model.addAttribute("member", member);
		return "admin/edit-member";
	}
	
	@PostMapping("/member/edit")
	public String editMember(@ModelAttribute UserDto userDto) {
		userService.editMember(userDto);
		return "redirect:/admin/members";
	}
	
	@RequestMapping("/delete-member/{id}")
	public String deleteMember(@PathVariable("id") Long id) {
		userService.delete(id);
		return "redirect:/admin/members";
	}
	
	@GetMapping("/member/add-to-group/{id}")
	public String getAddGroupPage(@PathVariable("id") Long id, Model model) {
		UserDto userDto = userService.findById(id);
		model.addAttribute("userDto", userDto);
		return "admin/add-to-group";
	}
	
	@RequestMapping("/member/add-to-group-1/{email}")
	public String addToGroup1(@PathVariable("email") String email, Model model) {
		List<UserDto> users = userService.findAllUsers();
		User user = userService.findUserByEmail(email);
		String message1 = null;
		String username = user.getUsername();
		
		if(user.getBalance() >= 900) {
			user.setGroup(M_Group.Group_1);
			user.setSub_amount(300);
			userRepository.save(user);
		} else {
			message1 = "Fail";
		}
		
		model.addAttribute("username", username);
		model.addAttribute("users", users);
		model.addAttribute("message1", message1);
		return "admin/members";
	}
	
	@RequestMapping("/member/add-to-group-2/{email}")
	public String addToGroup2(@PathVariable("email") String email, Model model) {
		List<UserDto> users = userService.findAllUsers();
		User user = userService.findUserByEmail(email);
		String message2 = null;
		String username = user.getUsername();
		
		if(user.getBalance() >= 1800) {
			user.setGroup(M_Group.Group_2);
			user.setSub_amount(600);
			userRepository.save(user);
		} else {
			message2 = "Fail";
		}
		
		model.addAttribute("username", username);
		model.addAttribute("users", users);
		model.addAttribute("message2", message2);
		return "admin/members";
	}
	
	@GetMapping("/member/add-to-cycle2")
	public String getAddToCycle2(Model model) {
		User user = algorithm.cycle2Randomizer();
		model.addAttribute("user", user);
		return "admin/add-to-cycle2";
	}
	
	@PostMapping("/member/add-to-cycle2")
	public String addToCycle2(@ModelAttribute User user, Model model) {
		userService.changeUserState(user);
		model.addAttribute("message");
		return "redirect:/admin/group-2";
	}
	
	@GetMapping("/member/add-to-cycle1")
	public String getAddToCycle1(Model model) {
		User user = algorithm.cycle1Randomizer();
		model.addAttribute("user", user);
		return "admin/add-to-cycle1";
	}
	
	@PostMapping("/member/add-to-cycle1")
	public String addToCycle1(@ModelAttribute User user, Model model) {
		userService.changeUserState(user);
		model.addAttribute("message");
		return "redirect:/admin/group-1";
	}
	
	@GetMapping("/group-1-cycle")
	public String getcycle1Page(Model model) {
		List<AlgorithmCycle> cycleData =  algorithmCycleService.findGroup1Members();
		model.addAttribute("cycle_members", cycleData);
		return "admin/group-1-cycle";
	}
	
	@GetMapping("/group-2-cycle")
	public String getcycle2Page(Model model) {
		List<AlgorithmCycle> cycleData =  algorithmCycleService.findGroup2Members();
		model.addAttribute("cycle_members", cycleData);
		return "admin/group-2-cycle";
	}
	
	@RequestMapping("/group-1-cycle/remove/{id}")
	public String removeFromGroup1Cycle(@PathVariable("id") Long id) {
		algorithmCycleService.removeMember(id);
		return "redirect:/admin/group-1-cycle";
	}
	
	@RequestMapping("/group-2-cycle/remove/{id}")
	public String removeFromGroup2Cycle(@PathVariable("id") Long id) {
		algorithmCycleService.removeMember(id);
		return "redirect:/admin/group-2-cycle";
	}
	
	@RequestMapping("/group-1-cycle/pay/{id}")
	public String payGroup1Member(@PathVariable("id") Long id) {
		algorithmCycleService.group1Pay(id);
		return "redirect:/admin/group-1-cycle";
	}
	
	@RequestMapping("/group-2-cycle/pay/{id}")
	public String payGroup2Member(@PathVariable("id") Long id) {
		algorithmCycleService.group2Pay(id);
		return "redirect:/admin/group-2-cycle";
	}
	
	@RequestMapping("/group1/credit")
	public String creditGroup1(Model model) {
		String msg = algorithm.creditGroup1();
		
		List<UserDto> users = userService.findAllUsers();
		
		Accounts group1AccountBalance = accountsService.getAccounts("group1");
		Accounts group2AccountBalance = accountsService.getAccounts("group2");
		Long group1Balance = group1AccountBalance.getBalance();
		Long group2Balance = group2AccountBalance.getBalance();
		
		model.addAttribute("users", users);
		model.addAttribute("group1Balance", group1Balance);
		model.addAttribute("group2Balance", group2Balance);
		model.addAttribute("msg", msg);
		return "admin/dashboard";
	}
	
	@RequestMapping("/group2/credit")
	public String creditGroup2(Model model) {
		String message = algorithm.creditGroup2();		
		List<UserDto> users = userService.findAllUsers();
		
		Accounts group1AccountBalance = accountsService.getAccounts("group1");
		Accounts group2AccountBalance = accountsService.getAccounts("group2");
		Long group1Balance = group1AccountBalance.getBalance();
		Long group2Balance = group2AccountBalance.getBalance();
		
		model.addAttribute("users", users);
		model.addAttribute("group1Balance", group1Balance);
		model.addAttribute("group2Balance", group2Balance);
		model.addAttribute("message", message);
		
		return "admin/dashboard";
	}
}
