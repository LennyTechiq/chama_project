package com.lenstech.chamafullstackproject.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lenstech.chamafullstackproject.dto.UserDto;
import com.lenstech.chamafullstackproject.model.M_Group;
import com.lenstech.chamafullstackproject.model.Role;
import com.lenstech.chamafullstackproject.model.State;
import com.lenstech.chamafullstackproject.model.User;
import com.lenstech.chamafullstackproject.repository.RoleRepository;
import com.lenstech.chamafullstackproject.repository.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;  

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private AlgorithmCycleService algorithmCycleService;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           AlgorithmCycleService algorithmCycleService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.algorithmCycleService = algorithmCycleService;
    }

	@Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setState(State.Not_Cycled);
        user.setGroup(M_Group.NA);
        //user.setSub_amount(300);

        Role role = roleRepository.findByName("USER");
        if(role == null){
            role = checkRoleExist();
        }
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map((user) -> mapToUserDto(user))
                .collect(Collectors.toList());
    }

    private UserDto mapToUserDto(User user){
        UserDto userDto = new UserDto();
        String[] str = user.getUsername().split(" ");
        userDto.setFirstName(str[0]);
        userDto.setLastName(str[1]);
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setState(user.getState());
        userDto.setBalance(user.getBalance());
        userDto.setSub_amount(user.getSub_amount());
        userDto.setGroup(user.getGroup());
        return userDto;
    }

    private Role checkRoleExist(){
        Role role = new Role();
        role.setName("USER");
        return roleRepository.save(role);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }
    
    @Override
    public void editMember(UserDto userDto) {
        User user = new User();
        user.setGroup(userDto.getGroup());
        //user.setEmail(userDto.getEmail());
        userRepository.save(user);
    }

	@Override
	public UserDto findById(Long id) {
		Optional<User> user = userRepository.findById(id);
		UserDto userDto = new UserDto();
		String[] str = user.get().getUsername().split(" ");
        userDto.setFirstName(str[0]);
        userDto.setLastName(str[1]);
        userDto.setEmail(user.get().getEmail());
		
		return userDto;
	}

	@Override
	public void delete(Long id) {
		userRepository.deleteById(id);
	}

	@Override
	public void changeUserState(User user) {
		User updatedUser = userRepository.findByEmail(user.getEmail());
		updatedUser.setState(State.Cycled);
		algorithmCycleService.addMember(updatedUser);
		userRepository.save(updatedUser);
	}

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public List<User> findByActive(boolean b) {
		return userRepository.findByActive(false);
	}

	@Override
	public List<UserDto> findGroup1() {
		List<User> users = userRepository.findAll();
		List<User> group_1_users = new ArrayList<>();
		
		for(User member : users) {
			if(member.getGroup().equals(M_Group.Group_1)) {
				group_1_users.add(member);
			}
		}
		
		return group_1_users.stream()
                .map((user) -> mapToUserDto(user))
                .collect(Collectors.toList());
	}
	
	@Override
	public List<UserDto> findGroup2() {
		List<User> users = userRepository.findAll();
		List<User> group_2_users = new ArrayList<>();
		
		for(User member : users) {
			if(member.getGroup().equals(M_Group.Group_2)) {
				group_2_users.add(member);
			}
		}
		
		return group_2_users.stream()
                .map((user) -> mapToUserDto(user))
                .collect(Collectors.toList());
	}

	@Override
	public void addToGroup1(User userDto) {
		User user = userRepository.findByEmail(userDto.getEmail());
		user.setGroup(M_Group.Group_1);
		userRepository.save(user);
	}
}