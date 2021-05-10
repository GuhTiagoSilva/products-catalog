package com.gustavo.dscatalog.tests.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gustavo.dscatalog.dto.ProductDTO;
import com.gustavo.dscatalog.entities.Product;
import com.gustavo.dscatalog.repositories.ProductRepository;
import com.gustavo.dscatalog.services.ProductService;
import com.gustavo.dscatalog.services.exceptions.DatabaseException;
import com.gustavo.dscatalog.services.exceptions.ResourceNotFoundException;
import com.gustavo.dscatalog.tests.factory.ProductFactory;

@ExtendWith(SpringExtension.class) // The whole context will not be loaded 
public class ProductServiceTests {
	
	@InjectMocks // We put this annotation above the main object (which we will test)
	private ProductService service;
	
	@Mock // The dependent object will be annotated with this annotation
	private ProductRepository repository;
	
	private Long existingId;
	
	private Long dependentId;
	
	private Long nonExistingId;
	
	private Product product;
	
	private ProductDTO productDTO;
	
	private PageImpl<Product> page;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		dependentId = 4L;
		nonExistingId = 1000L;
		product = ProductFactory.createProduct();
		page = new PageImpl<>(List.of(product));
		productDTO = ProductFactory.createProductDTO();
		
		// Simulating the behavior of our mock object (repository). If we do not do this, our test is uncompleted.
		Mockito.when(repository.find(ArgumentMatchers.any(), ArgumentMatchers.anyString(), ArgumentMatchers.any())).thenReturn(page);
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);
		Mockito.doNothing().when(repository).deleteById(existingId);
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		Mockito.when(repository.getOne(existingId)).thenReturn(product);
		Mockito.doThrow(EntityNotFoundException.class).when(repository).getOne(nonExistingId);
	}
	
	@Test
	public void findAllPagedShouldReturnPage() {
		Long categoryId = 0L;
		String name = "";
		PageRequest pageRequest = PageRequest.of(0, 10);
		
		Page<ProductDTO> result = service.findAllPaged(pageRequest, name, categoryId);
		Assertions.assertNotNull(result);
		Assertions.assertFalse(result.isEmpty());
		
		Mockito.verify(repository).find(null, name, pageRequest);
	}

	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdNotExists() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(nonExistingId, productDTO);
		});
		
	}
	
	@Test
	public void updateShouldReturnProductDTOWhenIdExists() {
		
		ProductDTO dto = service.update(existingId, productDTO);
		Assertions.assertNotNull(dto);
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdNotExists() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});
	}
	
	@Test
	public void findByIdShouldReturnProductDTOWhenIdExists() {
		Assertions.assertDoesNotThrow(() -> {
			service.findById(existingId);
		});
	}
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenIdIsAssociatedInOtherObject() {
		
		Assertions.assertThrows(DatabaseException.class, () -> {
			service.delete(dependentId);
		});
		
		Mockito.verify(repository).deleteById(dependentId);
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});
		
		Mockito.verify(repository).deleteById(nonExistingId);	
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});
		Mockito.verify(repository, Mockito.times(1)).deleteById(existingId); // Verifying if the deleteById of the Mock was called	
	}

}
