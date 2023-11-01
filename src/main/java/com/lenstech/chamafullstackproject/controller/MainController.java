package com.lenstech.chamafullstackproject.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

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
    
	@PostMapping("/contact")
	public String submitContact(HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String content = request.getParameter("message");
		
		String mailSubject = name + " has sent a message";
		String mailContent = "<p><b>Sender Name:</b> " + name + "</p>";
		mailContent += "<p><b>Sender E-mail:</b> " + email + "</p>";
		mailContent += "<p><b>Content:</b> " + content + "</p>";
		
		simpleMailMessage.setFrom(fromEmail);
		simpleMailMessage.setSubject(mailSubject);
		simpleMailMessage.setText(mailContent);
		simpleMailMessage.setTo("leonardrongoma3@gmail.com");
		
		mailSender.send(simpleMailMessage);
		
		return "message";
	}
}
