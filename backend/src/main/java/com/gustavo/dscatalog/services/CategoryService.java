package com.gustavo.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gustavo.dscatalog.dto.CategoryDTO;
import com.gustavo.dscatalog.entities.Category;
import com.gustavo.dscatalog.repositories.CategoryRepository;
import com.gustavo.dscatalog.services.exceptions.DatabaseException;
import com.gustavo.dscatalog.services.exceptions.ResourceNotFoundException;

@Service // with this annotation, spring boot will manage the instances of this
			// CategoryService, not the developer
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Transactional(readOnly = true) // avoiding the locking in the database
	public Page<CategoryDTO> findAllPaged(PageRequest pageRequest) {
		Page<Category> list = categoryRepository.findAll(pageRequest);
		return list.map(x-> new CategoryDTO(x));
	}

	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category> obj = categoryRepository.findById(id);
		Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		// getting data from Optional
		return new CategoryDTO(entity);
	}

	@Transactional
	public CategoryDTO insert(CategoryDTO categoryDTO) {
		Category category = new Category();
		category.setName(categoryDTO.getName());
		category = categoryRepository.save(category);
		return new CategoryDTO(category);
	}

	@Transactional
	public CategoryDTO update(Long id, CategoryDTO categoryDTO) {
		try {
			Category category = categoryRepository.getOne(id);

			/*
			 * we are using this instead of findById, because findById go to the database.
			 * To avoid go to the database twice (one for find and other for save), we use
			 * getOne method to load a temporary object in memory with the id that the is
			 * being passed. The performance of our code is much better than go to the
			 * database find a record.
			 */

			category.setName(categoryDTO.getName());
			category = categoryRepository.save(category);

			return new CategoryDTO(category);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found: " + id);
		}
	}

	
	public void delete(Long id) {
		try {
			categoryRepository.deleteById(id);
		}catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found: " + id);
		}catch(DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
	}

}
