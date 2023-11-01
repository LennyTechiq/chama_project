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
import com.lenstech.chamafullstackproject.model.User;
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
	
	public AdminController(
			UserService userService, 
			Algorithm algorithm,
			AlgorithmCycleService algorithmCycleService,
			AccountsService accountsService) {
		this.userService = userService;
		this.algorithm = algorithm;
		this.algorithmCycleService = algorithmCycleService;
		this.accountsService = accountsService;
	}

	@GetMapping("/dashboard")
	public String getAdminProfilePage(Model model) {
		List<UserDto> users = userService.findAllUsers();
		
		Accounts account = accountsService.getAccounts("main");
		Long balance = account.getBalance();
		
		model.addAttribute("users", users);
		model.addAttribute("balance", balance);
		return "admin/dashboard";
	}
	
	@GetMapping("/members")
	public String getMembersPage(Model model) {
		List<UserDto> users = userService.findAllUsers();
		model.addAttribute("users", users);
		return "admin/members";
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
	
	@GetMapping("/member/add-to-cycle")
	public String getAddToCycle(Model model) {
		User user = algorithm.randomUser();
		model.addAttribute("user", user);
		return "admin/add-to-cycle";
	}
	
	@PostMapping("/member/add-to-cycle")
	public String addToCycle(@ModelAttribute User user, Model model) {
		userService.changeUserState(user);
		String message = user.getUsername() + " has been added to cycle.";
		model.addAttribute("message", message);
		return "redirect:/admin/members";
	}
	
	@GetMapping("/cycle")
	public String getcyclePage(Model model) {
		List<AlgorithmCycle> cycleData =  algorithmCycleService.findAllUsers();
		model.addAttribute("cycle_members", cycleData);
		return "admin/cycle";
	}
	
	@RequestMapping("/cycle/remove/{id}")
	public String removeFromCycle(@PathVariable("id") Long id) {
		algorithmCycleService.removeMember(id);
		return "redirect:/admin/cycle";
	}
	
	@RequestMapping("/cycle/pay/{id}")
	public String payMember(@PathVariable("id") Long id) {
		algorithmCycleService.pay(id);
		return "redirect:/admin/cycle";
	}
	
	@RequestMapping("/members/credit")
	public String creditMembers(Model model) {
		algorithm.creditMembers();
		return "redirect:/admin/dashboard";
	}
}
