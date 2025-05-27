package com.rbsb.core.formBeans.Category;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CategoryRequests {
	private String category;
	private String name;
	
	private String description;
	
	private String bgColor;
	
}
