package com.gustavo.dscatalog.tests.factory;

import java.time.Instant;

import com.gustavo.dscatalog.dto.ProductDTO;
import com.gustavo.dscatalog.entities.Category;
import com.gustavo.dscatalog.entities.Product;

public class ProductFactory {

	public static Product createProduct() {
		Product product = new Product(1L, "Phone", "Good Phone", 500.0, "http://www.google.com.br", Instant.parse("2021-10-20T03:00:00Z"));
		product.getCategories().add(new Category(1L, null));
		return product;
	}
	
	public static ProductDTO createProductDTO() {
		Product product = createProduct();
		return new ProductDTO(product, product.getCategories());
	}
	
	public static ProductDTO createProductDTO(Long id) {
		ProductDTO dto = createProductDTO();
		dto.setId(id);
		return dto;
	}
	
}
