package com.gustavo.dscatalog.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.gustavo.dscatalog.dto.CategoryDTO;
import com.gustavo.dscatalog.services.CategoryService;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {

	@Autowired
	private CategoryService categoryService;
	
	@GetMapping
	public ResponseEntity<List<CategoryDTO>> findAll(){
		List<CategoryDTO>categoryList = categoryService.findAll();
		return ResponseEntity.ok().body(categoryList);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<CategoryDTO>findById(@PathVariable Long id){
		CategoryDTO categoryDTO = categoryService.findById(id);
		return ResponseEntity.ok().body(categoryDTO);
	}
	
	@PostMapping
	public ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO categoryDTO){
		categoryDTO = categoryService.insert(categoryDTO);
		
		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(categoryDTO.getId())
				.toUri(); // inserting an object in the header. Whith the code 201, we need to pass in the location header, the resource created

		return ResponseEntity.created(uri).body(categoryDTO);
	}
	
	
	
}