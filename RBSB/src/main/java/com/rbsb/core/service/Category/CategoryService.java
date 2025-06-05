package com.rbsb.core.service.Category;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.rbsb.core.formBeans.Category.CategoryRequests;
import com.rbsb.core.formBeans.Category.CategoryResponse;

public interface CategoryService {
	
	CategoryResponse add(CategoryRequests request,MultipartFile file);

	List<CategoryResponse> getAllCategory();

	void  deleteCategory(String CategoryId);

	CategoryResponse updateCategory(CategoryRequests req,String categoryId);

}
