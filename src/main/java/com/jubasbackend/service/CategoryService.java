package com.jubasbackend.service;

import com.jubasbackend.domain.entity.Category;
import com.jubasbackend.domain.repository.CategoryRepository;
import com.jubasbackend.dto.request.RequestCategoryDTO;
import com.jubasbackend.dto.response.ResponseCategorySpecialtyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CategoryService {
    
    @Autowired
    private CategoryRepository repository;

    protected Category findCategoryById(Short id) {
        return repository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Category doesn't exist."));
    }

    public List<Category> findAll(){
        return repository.findAll();
    }

    public List<ResponseCategorySpecialtyDTO> findAllWithSpecialty() {
        var list = repository.findAllCategoriesWithSpecialties();
        var response = new ArrayList<ResponseCategorySpecialtyDTO>();

        if (list.isEmpty()) {
            return response;
        }

        String lastCategory = null;
        ResponseCategorySpecialtyDTO categorySpecialty = null;

        for (var item : list) {
            boolean isLast = list.indexOf(item) == list.size() - 1;

            if (categorySpecialty == null) {
                categorySpecialty = new ResponseCategorySpecialtyDTO(item);

            } else if (item.getCategoryName().equals(lastCategory)) {
                categorySpecialty.addSpecialty(item);

                if (isLast) {
                    response.add(categorySpecialty);
                }

            } else {
                response.add(categorySpecialty);
                categorySpecialty = new ResponseCategorySpecialtyDTO(item);

                if (isLast) {
                    response.add(categorySpecialty);
                }
            }

            lastCategory = item.getCategoryName();
        }

        return response;
    }

    public Category create(RequestCategoryDTO category) {
        Category newCategory = new Category(category);
        return repository.save(newCategory);
    }

    public Category update(Short id, RequestCategoryDTO category) {
        Category categoryToUpdate = new Category(category);
        categoryToUpdate.setId(findCategoryById(id).getId());
        return repository.save(categoryToUpdate);
    }

    public Category delete(Short id) {
        Category categoryToDelete = findCategoryById(id);
        repository.delete(categoryToDelete);
        return categoryToDelete;
    }
}
