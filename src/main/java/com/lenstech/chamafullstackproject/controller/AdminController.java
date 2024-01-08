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
		algorithmCycleService.pay(id);
		return "redirect:/admin/group-1-cycle";
	}
	
	@RequestMapping("/group-2-cycle/pay/{id}")
	public String payGroup2Member(@PathVariable("id") Long id) {
		algorithmCycleService.pay(id);
		return "redirect:/admin/group-2-cycle";
	}
	
	@RequestMapping("/members/credit")
	public String creditMembers(Model model) {
		algorithm.creditMembers();
		return "redirect:/admin/dashboard";
	}
}
