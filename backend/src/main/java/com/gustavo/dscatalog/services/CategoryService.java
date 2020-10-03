package com.gustavo.dscatalog.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gustavo.dscatalog.dto.CategoryDTO;
import com.gustavo.dscatalog.repositories.CategoryRepository;

@Service // with this annotation, spring boot will manage the instances of this
			// CategoryService, not the developer
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Transactional(readOnly = true)//avoiding the locking in the database
	public List<CategoryDTO> findAll() {
		
		return 
				categoryRepository
				.findAll()
				.stream()
				.map(x-> new CategoryDTO(x))
				.collect(Collectors.toList());
		
	}

}
