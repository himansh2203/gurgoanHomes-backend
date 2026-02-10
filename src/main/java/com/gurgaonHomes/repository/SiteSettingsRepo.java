package com.gurgaonHomes.repository;

import com.gurgaonHomes.entity.SiteSettingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteSettingsRepo extends JpaRepository<SiteSettingsEntity, Long> {
}
