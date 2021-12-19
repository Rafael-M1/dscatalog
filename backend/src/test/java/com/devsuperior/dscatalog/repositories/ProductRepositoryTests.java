package com.devsuperior.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository repository;
	
	private Long nonExistingId;
	private Long existingId;
	private Long countTotalProducts;
	
	@BeforeEach
	void setUp() throws Exception{
		nonExistingId = 1000L;
		existingId = 1L;
		countTotalProducts = 25L;
	}
	
	@Test
	public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
		Product product = Factory.createProduct();
		product.setId(null);
		
		product = repository.save(product);
		
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(product.getId(), countTotalProducts + 1);
	}
	@Test
	public void deleteShouldDeleteObjectWhenIdExists () {		
		repository.deleteById(existingId);
		Optional<Product> result = repository.findById(existingId);

		Assertions.assertFalse(result.isPresent());
	}
	
	@Test
	public void findByIdShouldReturnNonEmptyOptionalProductWhenIdExists () {		
		Optional<Product> result = repository.findById(existingId);

		Assertions.assertTrue(result.isPresent());
	}
	@Test
	public void findByIdShouldReturnEmptyOptionalProductWhenIdDoesNotExists () {		
		Optional<Product> result = repository.findById(nonExistingId);

		Assertions.assertTrue(result.isEmpty());
	}
	
	@Test
	public void deleteShouldThrowEmptyResultDataAccessWhenIdDoesNotExists () {
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			repository.deleteById(nonExistingId);
		});
	}
}
