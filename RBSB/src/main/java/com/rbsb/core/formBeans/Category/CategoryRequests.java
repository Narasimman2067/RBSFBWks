package com.rbsb.core.formBeans.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequests {
	private String category;
	private String name;
	
	private String description;
	
	private String bgColor;
	
}
