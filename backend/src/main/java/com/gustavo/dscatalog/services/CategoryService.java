package com.gustavo.dscatalog.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gustavo.dscatalog.entities.Category;
import com.gustavo.dscatalog.repositories.CategoryRepository;

@Service // with this annotation, spring boot will manage the instances of this
			// CategoryService, not the developer
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	public List<Category> findAll() {
		return categoryRepository.findAll();
	}

}
