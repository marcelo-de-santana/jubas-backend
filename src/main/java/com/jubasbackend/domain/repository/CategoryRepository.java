package com.jubasbackend.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jubasbackend.domain.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Short> {    
}
