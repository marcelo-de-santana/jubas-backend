package com.jubasbackend.domain.repository;

import com.jubasbackend.domain.projection.CategorySpecialtyProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jubasbackend.domain.entity.Category;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Short> {

    @Query(nativeQuery = true, value = """
                SELECT
                    tb_category.id                  AS categoryId,
                    tb_category.name                AS categoryName,
                    bin_to_uuid(tb_specialty.id)    AS specialtyId,
                    tb_specialty.name               AS specialtyName,
                    tb_specialty.price              AS specialtyPrice,
                    tb_specialty.time_duration      AS specialtyTimeDuration 
                FROM
                    tb_category
                JOIN
                    tb_specialty
                ON
                    tb_category.id = tb_specialty.category_id
                ORDER BY
                    tb_category.id;
            """)
    List<CategorySpecialtyProjection> findAllCategoriesWithSpecialties();

}
