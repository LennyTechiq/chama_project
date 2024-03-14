package com.lenstech.chamafullstackproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lenstech.chamafullstackproject.model.CycleCount;

@Repository
public interface CycleRepository extends JpaRepository<CycleCount, Long>{
	CycleCount findByGroupName(String groupName);
}
