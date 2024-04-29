package com.lenstech.chamafullstackproject.controller;

import com.lenstech.chamafullstackproject.dto.DepositForm;
import com.lenstech.chamafullstackproject.dto.UserDto;
import com.lenstech.chamafullstackproject.dto.WithdrawForm;
import com.lenstech.chamafullstackproject.model.User;
import com.lenstech.chamafullstackproject.service.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;

	@GetMapping("/register")
    public String getSignUpPage(Model model){
        // create model object to store form data
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "user/register";
    }
    
    @PostMapping("/register/save")
    public String registrationUser(@Valid @ModelAttribute("user") UserDto userDto,
                               BindingResult result,
                               Model model){
        User existingUser = userService.findUserByEmail(userDto.getEmail());

        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
            result.rejectValue("email", null,
                    "There is already an account registered with the same email");
        }

        if(result.hasErrors()){
            model.addAttribute("user", userDto);
            return "/user/register";
        }

        userService.saveUser(userDto);
        return "redirect:/register?success";
    }
    
    @GetMapping("/profile")
    public String getProfilePage(Principal principal, Model model) {
    	String email = principal.getName();
    	
    	User loggedInUser = userService.findUserByEmail(email);
    	
    	String username = loggedInUser.getUsername();
    	long balance = loggedInUser.getBalance();
    	long sub_amount = loggedInUser.getSub_amount();
    	
    	model.addAttribute("depositForm", new DepositForm());
    	model.addAttribute("withdrawForm", new WithdrawForm());
    	model.addAttribute("user", loggedInUser);
    	model.addAttribute("username", username);
    	model.addAttribute("sub_amount", sub_amount);
    	model.addAttribute("balance", balance);
    	return "user/profile";
    }
    
    @PostMapping("/deposit")
    public String deposit(@ModelAttribute DepositForm depositForm, Principal principal) {
    	String email = principal.getName();
    	
    	User user = userService.findUserByEmail(email);
    	
    	Long depositAmount = depositForm.getDepositAmount();
    	Long currentBalance = user.getBalance();
    	Long newBalance = currentBalance + depositAmount;
    	
    	user.setBalance(newBalance);
    	
    	userService.updateUser(user);
    	return "redirect:/user/profile"; 
    }
    
    @PostMapping("/withdraw")
    public String withdraw(@ModelAttribute WithdrawForm withdrawForm, Principal principal) {
    	String email = principal.getName();
    	
    	User user = userService.findUserByEmail(email);
    	
    	Long withdrawalAmount = withdrawForm.getWithdrawalAmount();
    	Long currentBalance = user.getBalance();
    	Long newBalance = currentBalance - withdrawalAmount;
    	
    	user.setBalance(newBalance);
    	
    	userService.updateUser(user);
    	return "redirect:/user/profile"; 
    }
}
