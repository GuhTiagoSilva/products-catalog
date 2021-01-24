package com.gustavo.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gustavo.dscatalog.entities.Category;

@Repository // turning CategoryRepositoy a injective component.
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
