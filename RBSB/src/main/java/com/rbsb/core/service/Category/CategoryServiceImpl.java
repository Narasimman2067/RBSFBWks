package com.rbsb.core.service.Category;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rbsb.core.formBeans.Category.CategoryRequests;
import com.rbsb.core.formBeans.Category.CategoryResponse;
import com.rbsb.core.model.Category.CategoryEntity;
import com.rbsb.core.repository.category.CategoryRepository;
import com.rbsb.core.service.GoogleDrive.GoogleDriveUploaderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;
	private final GoogleDriveUploaderService googleDriveService;
	
	@Override
	public CategoryResponse add(CategoryRequests request,MultipartFile file) {
		CategoryEntity newCategory=convertToEntity(request);
		String imageUrl=googleDriveService.uploadFile(file);
		newCategory.setImgUrl(imageUrl);
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
				.updatedATimestamp(newCategory.getUpdatedAt())
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

	@Override
	public void deleteCategory(String categoryId) {
		
		CategoryEntity existingCategory=categoryRepository.findByCategoryId(categoryId);
		if(existingCategory !=null) {
		categoryRepository.delete(existingCategory);
	}else {
		throw new RuntimeException("Category Id i snot Found" + categoryId);
	}
		
		
	}

	@Override
	public CategoryResponse updateCategory(CategoryRequests req, String categoryId) {
		
		CategoryEntity existingCategory = categoryRepository.findByCategoryId(categoryId);
		 if (existingCategory == null) {
			 
		        throw new RuntimeException("Category ID not found: " + categoryId);
		    }

		    // Update fields
		    existingCategory.setCategory(req.getCategory());
		    existingCategory.setName(req.getName());
		    existingCategory.setDescription(req.getDescription());
		    existingCategory.setBgColor(req.getBgColor());
		    CategoryEntity updatedCategory = categoryRepository.save(existingCategory);

		    return convertToResponse(updatedCategory);
	}

	

}
