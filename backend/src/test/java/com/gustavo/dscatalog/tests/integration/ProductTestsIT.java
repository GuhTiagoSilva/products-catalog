package com.gustavo.dscatalog.tests.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import com.gustavo.dscatalog.dto.ProductDTO;
import com.gustavo.dscatalog.services.ProductService;
import com.gustavo.dscatalog.services.exceptions.ResourceNotFoundException;

@SpringBootTest
@Transactional
public class ProductTestsIT {

	@Autowired
	private ProductService service;

	private long existingId;
	private long nonExistingId;
	private long countTotalProducts;
	private long countPCGamerProducts;
	private PageRequest pageRequest;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalProducts = 25L;
		countPCGamerProducts = 21L;
		pageRequest = PageRequest.of(0, 10);
	}

	@Test
	public void findAllPagedShouldReturnProductsWhenNameExistsIgnoringCase() {
		String pcGamer = "pc gamer";
		Page<ProductDTO> result = service.findAllPaged(pageRequest, pcGamer, 0L);
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(result.getTotalElements(), countPCGamerProducts);
	}

	@Test
	public void findAllPagedShouldReturnAllProductsWhenNameIsEmpty() {
		String productName = "";
		Page<ProductDTO> result = service.findAllPaged(pageRequest, productName, 0L);
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(result.getTotalElements(), countTotalProducts);
	}

	@Test
	public void findAllPagedShoulReturnNothingWhenNameDoesNotExist() {
		String name = "Camera";
		Page<ProductDTO> result = service.findAllPaged(pageRequest, name, 0L);
		Assertions.assertTrue(result.isEmpty());
	}

	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});
	}

	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});
	}
}
