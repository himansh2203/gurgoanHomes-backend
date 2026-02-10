package com.gurgaonHomes.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gurgaonHomes.entity.PropertyEntity;
import com.gurgaonHomes.service.PropertyService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/properties")
@CrossOrigin(origins = "*")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(PropertyController.class);

//    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//        public ResponseEntity<?> createProperty(
//            @RequestParam(value = "name", required = false) String name,
//            @RequestParam(value = "description", required = false) String description,
//            @RequestParam(value = "location", required = false) String location,
//            @RequestParam(value = "price", required = false) String price,
//            @RequestParam(value = "propertyType", required = false) String propertyType,
//            @RequestParam(value = "Bedroom", required = false) String bedroom,
//            @RequestParam(value = "Bathroom", required = false) String bathroom,
//            @RequestParam(value = "propertyMeasurment", required = false) String propertyMeasurment,
//            @RequestParam(value = "status", required = false) String status,
//            HttpServletRequest request) throws IOException {
//
//        try {
//            logger.info("createProperty request received");
//
//            // If container provided MultipartHttpServletRequest, use it; otherwise parse raw request via Apache FileUpload
//            boolean parsedByContainer = false;
//            if (request instanceof org.springframework.web.multipart.MultipartHttpServletRequest) {
//                parsedByContainer = true;
//                logger.info("Container parsed multipart request. Keys: {}",
//                        ((org.springframework.web.multipart.MultipartHttpServletRequest) request).getFileMap().keySet());
//            }
//
//            PropertyEntity property = PropertyEntity.builder()
//                    .name(name)
//                    .description(description)
//                    .location(location)
//                    .price(parsePrice(price))
//                    .propertyType(propertyType)
//                    .Bedroom(parseCount(bedroom))
//                    .Bathroom(parseCount(bathroom))
//                    .propertyMeasurment(propertyMeasurment)
//                    .status(status)
//                    .build();
//
//            MultipartFile[] images = new MultipartFile[4];
//            MultipartFile video = null;
//
//            if (parsedByContainer) {
//                org.springframework.web.multipart.MultipartHttpServletRequest mreq =
//                        (org.springframework.web.multipart.MultipartHttpServletRequest) request;
//                for (int i = 0; i < 4; i++) {
//                    MultipartFile f = mreq.getFile("image" + (i + 1));
//                    if (f != null && !f.isEmpty()) images[i] = f;
//                }
//                MultipartFile v = mreq.getFile("video");
//                if (v != null && !v.isEmpty()) video = v;
//            } else {
//                // parse with Apache Commons FileUpload
//                DiskFileItemFactory factory = new DiskFileItemFactory();
//                ServletFileUpload upload = new ServletFileUpload(factory);
//                upload.setSizeMax(200L * 1024 * 1024);
//                upload.setFileSizeMax(50L * 1024 * 1024);
//                upload.setFileCountMax(-1);
//                List<FileItem> items = new ArrayList<>();
//                try {
//                    items = upload.parseRequest(request);
//                } catch (FileUploadException fue) {
//                    throw new IOException(fue);
//                }
//
//                // populate fields from parsed form fields and files
//                for (FileItem item : items) {
//                    if (item.isFormField()) {
//                        String field = item.getFieldName();
//                        String val = item.getString();
//                        switch (field) {
//                            case "name": property.setName(val); break;
//                            case "description": property.setDescription(val); break;
//                            case "location": property.setLocation(val); break;
//                            case "price": property.setPrice(parsePrice(val)); break;
//                            case "propertyType": property.setPropertyType(val); break;
//                            case "Bedroom": property.setBedroom(parseCount(val)); break;
//                            case "Bathroom": property.setBathroom(parseCount(val)); break;
//                            case "propertyMeasurment": property.setPropertyMeasurment(val); break;
//                            case "status": property.setStatus(val); break;
//                            default: break;
//                        }
//                    } else {
//                        String field = item.getFieldName();
//                        // wrap FileItem into MultipartFile
//                        MultipartFile mf = new MultipartFile() {
//                            @Override public String getName() { return field; }
//                            @Override public String getOriginalFilename() { return item.getName(); }
//                            @Override public String getContentType() { return item.getContentType(); }
//                            @Override public boolean isEmpty() { return item.getSize() == 0; }
//                            @Override public long getSize() { return item.getSize(); }
//                            @Override public byte[] getBytes() throws IOException { return item.get(); }
//                            @Override public InputStream getInputStream() throws IOException { return item.getInputStream(); }
//                            @Override public void transferTo(File dest) throws IOException, IllegalStateException {
//                                try { item.write(dest); } catch (Exception e) { throw new IOException(e); }
//                            }
//                        };
//
//                        if (field != null && field.startsWith("image")) {
//                            try {
//                                int idx = Integer.parseInt(field.replaceAll("[^0-9]", "")) - 1;
//                                if (idx >= 0 && idx < 4) images[idx] = mf;
//                            } catch (NumberFormatException ignored) {}
//                        } else if ("video".equals(field)) {
//                            video = mf;
//                        }
//                    }
//                }
//            }
//
//            PropertyEntity saved = propertyService.saveProperty(property, images, video);
//            return ResponseEntity.ok(saved);
//        } catch (Exception ex) {
//            logger.error("Error creating property", ex);
//            Map<String, String> body = new HashMap<>();
//            body.put("error", ex.getMessage());
//            return ResponseEntity.status(500).body(body);
//        }
//    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProperty(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "price", required = false) String price,
            @RequestParam(value = "propertyType", required = false) String propertyType,
            @RequestParam(value = "Bedroom", required = false) String bedroom,
            @RequestParam(value = "Bathroom", required = false) String bathroom,
            @RequestParam(value = "propertyMeasurment", required = false) String propertyMeasurment,
            @RequestParam(value = "status", required = false) String status,

            @RequestParam(value = "image1", required = false) MultipartFile image1,
            @RequestParam(value = "image2", required = false) MultipartFile image2,
            @RequestParam(value = "image3", required = false) MultipartFile image3,
            @RequestParam(value = "image4", required = false) MultipartFile image4,
            @RequestParam(value = "video", required = false) MultipartFile video
    ) {

        try {
            System.out.println("🔥 Property POST API Called");

            PropertyEntity property = PropertyEntity.builder()
                    .name(name)
                    .description(description)
                    .location(location)
                    .price(parsePrice(price))
                    .propertyType(propertyType)
                    .Bedroom(parseCount(bedroom))
                    .Bathroom(parseCount(bathroom))
                    .propertyMeasurment(propertyMeasurment)
                    .status(status)
                    .build();

            MultipartFile[] images = new MultipartFile[]{image1, image2, image3, image4};

            PropertyEntity saved = propertyService.saveProperty(property, images, video);

            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }



    @GetMapping
    public ResponseEntity<List<PropertyEntity>> getAllProperties() {
        return ResponseEntity.ok(propertyService.getAllProperties());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropertyEntity> getPropertyById(@PathVariable Long id) {
        PropertyEntity property = propertyService.getPropertyById(id);
        if (property != null) {
            return ResponseEntity.ok(property);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable Long id) {
        propertyService.deleteProperty(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<?> updateProperty(
            @PathVariable Long id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "price", required = false) String price,
            @RequestParam(value = "propertyType", required = false) String propertyType,
            @RequestParam(value = "Bedroom", required = false) String bedroom,
            @RequestParam(value = "Bathroom", required = false) String bathroom,
            @RequestParam(value = "propertyMeasurment", required = false) String propertyMeasurment,
            @RequestParam(value = "status", required = false) String status,
            HttpServletRequest request) throws IOException {

        try {
            logger.info("updateProperty request received for id: {}", id);
            if (request instanceof org.springframework.web.multipart.MultipartHttpServletRequest) {
                logger.info("Files received: {}",
                        ((org.springframework.web.multipart.MultipartHttpServletRequest) request).getFileMap().keySet());
            }

            PropertyEntity property = PropertyEntity.builder()
                    .name(name)
                    .description(description)
                    .location(location)
                    .price(parsePrice(price))
                    .propertyType(propertyType)
                    .Bedroom(parseCount(bedroom))
                    .Bathroom(parseCount(bathroom))
                    .propertyMeasurment(propertyMeasurment)
                    .status(status)
                    .build();

            // Extract files from multipart request dynamically
            MultipartFile[] images = new MultipartFile[4];
            MultipartFile video = null;
            if (request instanceof org.springframework.web.multipart.MultipartHttpServletRequest) {
                org.springframework.web.multipart.MultipartHttpServletRequest mreq =
                        (org.springframework.web.multipart.MultipartHttpServletRequest) request;
                logger.info("Files received (update): {}", mreq.getFileMap().keySet());
                for (int i = 0; i < 4; i++) {
                    MultipartFile f = mreq.getFile("image" + (i + 1));
                    if (f != null && !f.isEmpty()) images[i] = f;
                }
                MultipartFile v = mreq.getFile("video");
                if (v != null && !v.isEmpty()) video = v;
            } else {
                logger.warn("Request is not a MultipartHttpServletRequest (update); no files found");
            }

            PropertyEntity updated = propertyService.updateProperty(id, property, images, video);
            return ResponseEntity.ok(updated);
        } catch (Exception ex) {
            logger.error("Error updating property id: {}", id, ex);
            Map<String, String> body = new HashMap<>();
            body.put("error", ex.getMessage());
            return ResponseEntity.status(500).body(body);
        }
    }

    private Double parsePrice(String price) {
        try {
            return (price != null && !price.isEmpty()) ? Double.parseDouble(price) : null;
        } catch (Exception e) {
            return null;
        }
    }

    private Long parseCount(String count) {
        try {
            return (count != null && !count.isEmpty()) ? Long.parseLong(count) : null;
        } catch (Exception e) {
            return null;
        }
    }
}
