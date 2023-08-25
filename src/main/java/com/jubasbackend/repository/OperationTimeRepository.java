package com.jubasbackend.repository;

import com.jubasbackend.entity.OperationTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OperationTimeRepository extends JpaRepository<OperationTime, UUID> {}
