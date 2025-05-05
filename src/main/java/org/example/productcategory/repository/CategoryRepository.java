package org.example.productcategory.repository;

import org.example.productcategory.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  Page<Category> findAll(Pageable pageable);

  boolean existsByParentId(Long parentId);

  boolean existsByName(String name);

}