package com.rbsb.core.service.Category;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.rbsb.core.formBeans.Category.CategoryRequests;
import com.rbsb.core.formBeans.Category.CategoryResponse;
import com.rbsb.core.model.Category.CategoryEntity;
import com.rbsb.core.repository.category.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;
	
	@Override
	public CategoryResponse add(CategoryRequests request) {
		CategoryEntity newCategory=convertToEntity(request);
		newCategory=categoryRepository.save(newCategory);
		return convertToResponse(newCategory);
		
	}

	private CategoryResponse convertToResponse(CategoryEntity newCategory) {
		
		return CategoryResponse.builder()
				.category(newCategory.getCategory())
				.categoryId(newCategory.getCategoryid())
				.name(newCategory.getName())
				.bgColor(newCategory.getBgColor())
				.imgUrl(newCategory.getImgUrl())
				.description(newCategory.getDescription())
				.createdATimestamp(newCategory.getCreatedAt())
				.updatedATimestamp(newCategory.getCreatedAt())
				.build()
				;
		
	}

	private CategoryEntity convertToEntity(CategoryRequests request) {
		
		return CategoryEntity.builder()
				.categoryid(UUID.randomUUID().toString())
				.category(request.getCategory())
				.name(request.getName())
				.description(request.getDescription())
				.bgColor(request.getBgColor())
				.build();
		
	}

	@Override
	public List<CategoryResponse> getAllCategory() {
	List<CategoryResponse> response= categoryRepository.findAll().stream().map(categoryEntity->convertToResponse(categoryEntity)).collect(Collectors.toList());
	return response;
	}

}
