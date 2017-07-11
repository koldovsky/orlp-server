package com.softserve.academy.spaced.repetition.controller;

import java.util.List;
import com.softserve.academy.spaced.repetition.DTO.DTO;
import com.softserve.academy.spaced.repetition.DTO.DTOBuilder;
import com.softserve.academy.spaced.repetition.DTO.impl.CategoryPublicDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.softserve.academy.spaced.repetition.domain.Category;
import com.softserve.academy.spaced.repetition.service.CategoryService;


@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/api/category")
    public ResponseEntity<List<CategoryPublicDTO>> getCategories() {
        try {
            List<Category> categoryList = categoryService.getAllCategory();
            List<CategoryPublicDTO> categories = DTOBuilder.buildDtoListForCollection(categoryList,CategoryPublicDTO.class);
            return new ResponseEntity<>(categories, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/api/category/{id}")
    public ResponseEntity<DTO<Category>> getCategoryById(@PathVariable Long id) {
        try {
            Category category = categoryService.getCategoryById(id);
            CategoryPublicDTO publicDTO = DTOBuilder.buildDtoForEntity(category, CategoryPublicDTO.class);
            return new ResponseEntity<>(publicDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/api/category/top/")
    public ResponseEntity<List<CategoryPublicDTO>> get4Categories() {
        try{
        List<Category> categoryList = categoryService.get4Category();
            List<CategoryPublicDTO> categories = DTOBuilder.buildDtoListForCollection(categoryList,CategoryPublicDTO.class);
            return new ResponseEntity<>(categories, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/api/admin/add/category")
    public ResponseEntity<DTO<Category>> addCategory(@RequestBody Category category) {
        try {
            category = categoryService.addCategory(category);
            CategoryPublicDTO categoryDTO = DTOBuilder.buildDtoForEntity(category, CategoryPublicDTO.class);
            return new ResponseEntity<>(categoryDTO, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/api/admin/add/category/{id}")
    public ResponseEntity<DTO<Category>> updateCategory(@RequestBody Category category, @PathVariable Long id) {
        try {
            category = categoryService.updateCategory(category, id);
            CategoryPublicDTO categoryDTO = DTOBuilder.buildDtoForEntity(category, CategoryPublicDTO.class);
            return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
