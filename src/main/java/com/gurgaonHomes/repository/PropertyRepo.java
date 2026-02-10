package com.gurgaonHomes.repository;

import com.gurgaonHomes.entity.PropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepo extends JpaRepository<PropertyEntity, Long> {
  
}
