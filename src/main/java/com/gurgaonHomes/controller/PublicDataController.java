package com.gurgaonHomes.controller;

import com.gurgaonHomes.entity.ServiceEntity;
import com.gurgaonHomes.entity.SiteSettingsEntity;
import com.gurgaonHomes.entity.TestimonialEntity;
import com.gurgaonHomes.repository.ServiceRepo;
import com.gurgaonHomes.repository.SiteSettingsRepo;
import com.gurgaonHomes.repository.TestimonialRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public")
@CrossOrigin(origins = "*")
public class PublicDataController {

  @Autowired
  private TestimonialRepo testimonialRepo;

  @Autowired
  private ServiceRepo serviceRepo;

  @Autowired
  private SiteSettingsRepo siteSettingsRepo;

  @GetMapping("/testimonials")
  public List<TestimonialEntity> getTestimonials() {
    return testimonialRepo.findAll();
  }

  @GetMapping("/services")
  public List<ServiceEntity> getServices() {
    return serviceRepo.findAll();
  }

  @GetMapping("/settings")
  public SiteSettingsEntity getSettings() {
    List<SiteSettingsEntity> settings = siteSettingsRepo.findAll();
    return settings.isEmpty() ? null : settings.get(0);
  }
}
