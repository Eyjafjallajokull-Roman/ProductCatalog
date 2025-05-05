package org.example.productcategory.repository;

import org.example.productcategory.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
  Page<Product> findAll(Pageable pageable);

  @Query("SELECT COUNT(p.id) FROM Product p WHERE p.category.id = :categoryId")
  int countProductsByCategoryId(@Param("categoryId") Long categoryId);

  @Query("SELECT p FROM Product p WHERE p.deleted = false")
  Page<Product> findAllNotDeleted(Pageable pageable);

  @Query("SELECT p FROM Product p WHERE p.id = :id AND p.deleted = false")
  Optional<Product> findByIdNotDeleted(@Param("id") Long id);
}