package com.lenstech.chamafullstackproject.service;

import java.util.List;

import com.lenstech.chamafullstackproject.dto.UserDto;
import com.lenstech.chamafullstackproject.model.User;

public interface UserService {
    void saveUser(UserDto userDto);
    
    User findUserByEmail(String email);
    
    UserDto findById(Long id);

    List<UserDto> findAllUsers();
    
    List<UserDto> findGroup1();
    
    List<UserDto> findGroup2();
    
    void delete(Long id);

	User updateUser(User user);
	
	void editMember(UserDto userDto);
	
	void changeUserState(User user);

	List<User> findAll();

	List<User> findByActive(boolean b);

	void addToGroup1(User user);
}