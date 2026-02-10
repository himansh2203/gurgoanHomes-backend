package com.gurgaonHomes.service;

import com.gurgaonHomes.entity.PropertyEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PropertyService {
    PropertyEntity saveProperty(PropertyEntity property, MultipartFile[] images, MultipartFile video)
            throws IOException;

    List<PropertyEntity> getAllProperties();

    PropertyEntity getPropertyById(Long id);

    void deleteProperty(Long id);

    PropertyEntity updateProperty(Long id, PropertyEntity property, MultipartFile[] images, MultipartFile video)
            throws IOException;
}
