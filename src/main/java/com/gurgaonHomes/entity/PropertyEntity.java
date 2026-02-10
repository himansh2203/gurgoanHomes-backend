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
public class PropertyEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(nullable = false)
  private String name;
  
  @Lob
  @Column(columnDefinition = "TEXT")
  private String description;
  
  @Lob
  @Column(columnDefinition = "TEXT")
  private String location;
  
  @Column
  private Double price;
  
  @Column
  private String propertyType;
  
  @Column(name = "image_url_1", columnDefinition = "VARCHAR(500)")
  private String imageUrl1;
  
  @Column(name = "image_url_2", columnDefinition = "VARCHAR(500)")
  private String imageUrl2;
  
  @Column(name = "image_url_3", columnDefinition = "VARCHAR(500)")
  private String imageUrl3;
  
  @Column(name = "image_url_4", columnDefinition = "VARCHAR(500)")
  private String imageUrl4;
  
  @Column(name = "video_url", columnDefinition = "VARCHAR(500)")
  private String videoUrl;
  
  @Column(name = "bedroom")
  private Long Bedroom;
  
  @Column(name = "bathroom")
  private Long Bathroom;
  
  @Column(name = "property_measurement")
  private String propertyMeasurment;
  
  @Column
  private String status;
}
