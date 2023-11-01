package com.lenstech.chamafullstackproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lenstech.chamafullstackproject.model.AlgorithmCycle;

@Repository
public interface AlgorithmCycleRepository extends JpaRepository<AlgorithmCycle, Long> {

	AlgorithmCycle save(Long id);
	
}