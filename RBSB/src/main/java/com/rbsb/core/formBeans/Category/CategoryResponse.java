package com.rbsb.core.formBeans.Category;

import java.sql.Timestamp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryResponse {
	private String category;
	private String categoryId;
	private String name;
	private String description;
	private String bgColor;
	private Timestamp createdATimestamp;
	private Timestamp updatedATimestamp;
	private String imgUrl;
}
