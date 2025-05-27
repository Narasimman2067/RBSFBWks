package com.rbsb.core.service.Category;

import java.util.List;

import com.rbsb.core.formBeans.Category.CategoryRequests;
import com.rbsb.core.formBeans.Category.CategoryResponse;

public interface CategoryService {
	
	CategoryResponse add(CategoryRequests request);

	List<CategoryResponse> getAllCategory();

}
