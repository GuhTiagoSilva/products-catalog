package com.gustavo.dscatalog.tests.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gustavo.dscatalog.entities.Category;
import com.gustavo.dscatalog.entities.Product;
import com.gustavo.dscatalog.repositories.ProductRepository;
import com.gustavo.dscatalog.tests.factory.ProductFactory;

@DataJpaTest // we use this to run tests for repository. With this annotation, it will load
				// only the components of JPA
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository repository;

	private long existingId;

	private long nonExistingId;

	private long countTotalProducts;

	private long countPCGamerProducts;

	private PageRequest pageRequest;

	@BeforeEach
	void setUp() {
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalProducts = 25L;
		countPCGamerProducts = 21L;
		pageRequest = PageRequest.of(0, 10);
	}

	@Test
	public void findShouldNotReturnProductsWhenCategoryNotExists() {
		List<Category> categories = new ArrayList<>();
		categories.add(new Category(nonExistingId, null));
		Page<Product> result = repository.find(categories, "", pageRequest);
		Assertions.assertTrue(result.isEmpty());
	}

	@Test
	public void findShouldReturnProductsWhenFilterByCategoryIsNotEmptyAndCategoryExists() {

		List<Category> categories = new ArrayList<>();
		categories.add(new Category(existingId, null));
		Page<Product> result = repository.find(categories, "", pageRequest);
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(result.getTotalElements(), 1);
	}

	@Test
	public void findShouldReturnProductsWhenNameExists() {
		String pcGamer = "PC Gamer";
		Page<Product> result = repository.find(null, pcGamer, pageRequest);
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(result.getTotalElements(), countPCGamerProducts);
	}

	@Test
	public void findShouldReturnProductsWhenNameExistsIgnoringCase() {
		String pcGamer = "pc gamer";
		Page<Product> result = repository.find(null, pcGamer, pageRequest);
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(result.getTotalElements(), countPCGamerProducts);
	}

	@Test
	public void findShouldReturnAllProductsWhenNameIsEmpty() {
		String productName = "";
		Page<Product> result = repository.find(null, productName, pageRequest);
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(result.getTotalElements(), countTotalProducts);
	}

	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		repository.deleteById(existingId);
		Optional<Product> result = repository.findById(existingId);
		Assertions.assertFalse(result.isPresent());
	}

	@Test
	public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {
		Product product = ProductFactory.createProduct();
		product.setId(null);
		product = repository.save(product);
		Optional<Product> result = repository.findById(product.getId());

		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts + 1, product.getId());
		Assertions.assertTrue(result.isPresent());
		Assertions.assertSame(result.get(), product);
	}

	@Test
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			repository.deleteById(nonExistingId);
		});
	}

}
