package com.rbsb.core.repository.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rbsb.core.model.Category.CategoryEntity;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity,Long> {

	@Query("select c from CategoryEntity c where categoryid=:categoryId")
	CategoryEntity findByCategoryId(@Param("categoryId") String categoryId);

	

}
