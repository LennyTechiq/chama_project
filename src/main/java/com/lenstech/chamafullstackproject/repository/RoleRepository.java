package com.lenstech.chamafullstackproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lenstech.chamafullstackproject.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);

}