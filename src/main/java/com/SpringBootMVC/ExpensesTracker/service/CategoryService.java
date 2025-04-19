package com.SpringBootMVC.ExpensesTracker.service;
import java.util.List;

import com.SpringBootMVC.ExpensesTracker.entity.Category;

public interface CategoryService {
    Category findCategoryByName(String name);
    Category findCategoryById(int id);

    List<Category> getAllCategories();  
}
