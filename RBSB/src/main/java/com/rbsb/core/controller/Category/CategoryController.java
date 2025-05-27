package com.rbsb.core.controller.Category;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.rbsb.core.formBeans.Category.CategoryRequests;
import com.rbsb.core.formBeans.Category.CategoryResponse;
import com.rbsb.core.service.Category.CategoryService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("categories")
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryService categoryService;
	
	
	@PostMapping("/add")
	@ResponseStatus(HttpStatus.CREATED)
	public CategoryResponse addCategory(@RequestBody CategoryRequests request) {
		return categoryService.add(request);
		
	}
	
	@GetMapping("/getAllCategory")
	@ResponseStatus(HttpStatus.OK)
	public List<CategoryResponse> getAllCategoryItems(){
		return categoryService.getAllCategory();
		
	}
}
