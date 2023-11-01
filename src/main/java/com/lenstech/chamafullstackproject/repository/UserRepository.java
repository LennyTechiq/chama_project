package com.lenstech.chamafullstackproject.repository;

import com.lenstech.chamafullstackproject.model.State;
import com.lenstech.chamafullstackproject.model.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);
	User findById(long id);
	
	List<User> findByState(State state);
	List<User> findByActive(boolean active);
}
