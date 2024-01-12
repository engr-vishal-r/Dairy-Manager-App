package com.dairyProducts.details.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dairyProducts.details.entity.Curd;
import com.dairyProducts.details.repository.CurdRepository;

@RestController
@RequestMapping("/curd")
public class CurdController {
	@Autowired
	private CurdRepository curdRepository;

	@PostMapping
	public Curd addCurd(@RequestBody Curd curd) {
		return curdRepository.save(curd);
	}

	@GetMapping("/{id}")
	public Optional<Curd> getCurd(@PathVariable Long id) {
		return curdRepository.findById(id);
	}

	// Add other CRUD operations
}

