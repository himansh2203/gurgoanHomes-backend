package com.gurgaonHomes.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gurgaonHomes.entity.PropertyEntity;
import com.gurgaonHomes.repository.PropertyRepo;
import com.gurgaonHomes.service.PropertyService;

@Service
public class PropertyServiceImpl implements PropertyService {

    @Autowired
    private PropertyRepo propertyRepo;

    private final String uploadDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;

    // Allowed file types (use generic checks where possible)
    private static final String[] IMAGE_TYPES = {"image/jpeg", "image/png", "image/jpg", "image/webp", "image/avif"};
    private static final String[] VIDEO_TYPES = {"video/mp4", "video/mkv", "video/webm", "video/ogg", "video/quicktime"};

    // ================= SAVE PROPERTY ====================
    @Override
    public PropertyEntity saveProperty(PropertyEntity property, MultipartFile[] images, MultipartFile video) throws IOException {
        createUploadFolder();

        if (images != null) {
            for (int i = 0; i < images.length && i < 4; i++) {
                if (images[i] != null && !images[i].isEmpty()) {
                    validateImage(images[i]);
                    String url = saveFile(images[i]);
                    setImage(property, i, url);
                }
            }
        }

        if (video != null && !video.isEmpty()) {
            validateVideo(video);
            String url = saveFile(video);
            property.setVideoUrl(url);
        }

        return propertyRepo.save(property);
    }

    // ================= UPDATE PROPERTY ====================
    @Override
    public PropertyEntity updateProperty(Long id, PropertyEntity updated, MultipartFile[] images, MultipartFile video) throws IOException {

        PropertyEntity existing = propertyRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        // Update fields
        if (updated.getName() != null) existing.setName(updated.getName());
        if (updated.getDescription() != null) existing.setDescription(updated.getDescription());
        if (updated.getLocation() != null) existing.setLocation(updated.getLocation());
        if (updated.getPrice() != null) existing.setPrice(updated.getPrice());
        if (updated.getPropertyType() != null) existing.setPropertyType(updated.getPropertyType());
        if (updated.getBedroom() != null) existing.setBedroom(updated.getBedroom());
        if (updated.getBathroom() != null) existing.setBathroom(updated.getBathroom());
        if (updated.getPropertyMeasurment() != null) existing.setPropertyMeasurment(updated.getPropertyMeasurment());
        if (updated.getStatus() != null) existing.setStatus(updated.getStatus());

        // Update images
        if (images != null) {
            for (int i = 0; i < images.length && i < 4; i++) {
                if (images[i] != null && !images[i].isEmpty()) {
                    validateImage(images[i]);

                    // delete old image
                    deleteFile(getImage(existing, i));

                    String url = saveFile(images[i]);
                    setImage(existing, i, url);
                }
            }
        }

        // Update video
        if (video != null && !video.isEmpty()) {
            validateVideo(video);
            deleteFile(existing.getVideoUrl());
            existing.setVideoUrl(saveFile(video));
        }

        return propertyRepo.save(existing);
    }

    // ================= DELETE PROPERTY ====================
    @Override
    public void deleteProperty(Long id) {
        PropertyEntity property = propertyRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        // delete files
        deleteFile(property.getImageUrl1());
        deleteFile(property.getImageUrl2());
        deleteFile(property.getImageUrl3());
        deleteFile(property.getImageUrl4());
        deleteFile(property.getVideoUrl());

        propertyRepo.deleteById(id);
    }

    // ================= GET ====================
    @Override
    public List<PropertyEntity> getAllProperties() {
        return propertyRepo.findAll();
    }

    @Override
    public PropertyEntity getPropertyById(Long id) {
        return propertyRepo.findById(id).orElse(null);
    }

    // ================= UTIL METHODS ====================

    private void createUploadFolder() throws IOException {
        Files.createDirectories(Paths.get(uploadDir));
    }

    private String saveFile(MultipartFile file) throws IOException {
        String originalName = file.getOriginalFilename();
        if (originalName == null || originalName.isEmpty()) {
            throw new IOException("File name is empty");
        }

        String ext = "";
        int idx = originalName.lastIndexOf('.');
        if (idx >= 0) ext = originalName.substring(idx);

        String name = UUID.randomUUID() + ext;
        Path path = Paths.get(uploadDir).resolve(name);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        return "/uploads/" + name;
    }

    private void deleteFile(String url) {
        if (url == null) return;
        String fileName = url.replace("/uploads/", "");
        Path path = Paths.get(uploadDir + File.separator + fileName);
        try {
            Files.deleteIfExists(path);
        } catch (Exception ignored) {}
    }

    // Image setter
    private void setImage(PropertyEntity p, int index, String url) {
        switch (index) {
            case 0 -> p.setImageUrl1(url);
            case 1 -> p.setImageUrl2(url);
            case 2 -> p.setImageUrl3(url);
            case 3 -> p.setImageUrl4(url);
        }
    }

    private String getImage(PropertyEntity p, int index) {
        return switch (index) {
            case 0 -> p.getImageUrl1();
            case 1 -> p.getImageUrl2();
            case 2 -> p.getImageUrl3();
            case 3 -> p.getImageUrl4();
            default -> null;
        };
    }

    // ================= VALIDATION ====================

    private void validateImage(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("Invalid image type (no or wrong content type)");
        }

        if (file.getSize() > 5 * 1024 * 1024) throw new RuntimeException("Image too large (5MB max)");
    }

    private void validateVideo(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("video/")) {
            throw new RuntimeException("Invalid video type (no or wrong content type)");
        }

        if (file.getSize() > 50 * 1024 * 1024) throw new RuntimeException("Video too large (50MB max)");
    }
}
