package com.jubasbackend.domain.repository;

import com.jubasbackend.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Short> {
    boolean existsByName(String name);
}
