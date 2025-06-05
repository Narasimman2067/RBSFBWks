package com.rbsb.core.controller.Category;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
	public CategoryResponse addCategory(@RequestPart("category") String category,@RequestPart("file") MultipartFile file) {
		
		ObjectMapper objectMapper=new ObjectMapper();
		CategoryRequests request=null;
		try {
			request=objectMapper.readValue(category,CategoryRequests.class);
			return categoryService.add(request,file);
		} catch (JsonProcessingException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid Json Data"+e.getMessage());
		}
	}
	
	@GetMapping("/getAllCategory")
	@ResponseStatus(HttpStatus.OK)
	public List<CategoryResponse> getAllCategoryItems(){
		return categoryService.getAllCategory();
		
	}
	
	@PutMapping("/editCategory/{categoryid}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public CategoryResponse updateCategoryItems(@RequestBody CategoryRequests req,@PathVariable String categoryid) {
		
		try {
			return categoryService.updateCategory(req,categoryid);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Category Id is Not Found");
			
			}
		
		
	}
	
	@DeleteMapping("/deleteCategory/{categoryId}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteCategory(@PathVariable String categoryId) {
		try {
			categoryService.deleteCategory(categoryId);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getLocalizedMessage());
		}	
		
	}
	
	
}
