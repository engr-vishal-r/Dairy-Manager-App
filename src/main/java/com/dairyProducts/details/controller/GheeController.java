package com.dairyProducts.details.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dairyProducts.details.entity.Ghee;
import com.dairyProducts.details.repository.GheeRepository;

@RestController
@RequestMapping("/ghee")
public class GheeController {
	@Autowired
	private GheeRepository gheeRepository;

	@PostMapping
	public Ghee addGhee(@RequestBody Ghee ghee) {
		return gheeRepository.save(ghee);
	}

	@GetMapping("/{id}")
	public Optional<Ghee> getGhee(@PathVariable Long id) {
		return gheeRepository.findById(id);
	}

	// Add other CRUD operations
}
