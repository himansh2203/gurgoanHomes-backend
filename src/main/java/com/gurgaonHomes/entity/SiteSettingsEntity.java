package com.gurgaonHomes.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SiteSettingsEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // About Section
  private String aboutTitle;
  private String aboutSubtitle;
  @Lob
  @Column(columnDefinition = "TEXT")
  private String aboutDescription;

  // Contact Info
  private String companyAddress;
  private String companyPhone;
  private String companyEmail;

  // Stats
  private String statTrustedDeals; // e.g. "100%"
  private Integer statAwards; // e.g. 5
  private Integer statHappyClients; // e.g. 500
  private String statVerifiedProps; // e.g. "100%"
}
