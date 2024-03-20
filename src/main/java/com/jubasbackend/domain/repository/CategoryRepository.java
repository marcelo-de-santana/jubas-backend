package com.jubasbackend.domain.repository;

import com.jubasbackend.domain.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Short> {
    boolean existsByName(String name);
}
