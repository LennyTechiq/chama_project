package com.lenstech.chamafullstackproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
	
	@Autowired
	JavaMailSender mailSender;
	
	@Value("$(spring.mail.username)")
	private String fromEmail;

	@GetMapping
	public String getHomePage() {
		return "index";
	}
	
	@GetMapping("/about")
	public String getAboutPage() {
		return "about";
	}
	
    @GetMapping("/login")
    public String login(){
        return "user/login";
    }
    
    @RequestMapping(value = "/sendEmail", method = RequestMethod.GET)
    @ResponseBody
    public String sendEmail() {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo("leonardrongoma3@gmail.com");
        mailMessage.setSubject("Hello, Spring Boot Email");
        mailMessage.setText("This is a test email sent from Spring Boot.");

        mailSender.send(mailMessage);

        return "Email sent successfully!";
    }
}
