package com.gurgaonHomes.controller;

import com.gurgaonHomes.entity.*;
import com.gurgaonHomes.repository.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*") // Adjust as needed
public class AdminController {

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private TestimonialRepo testimonialRepo;

    @Autowired
    private ServiceRepo serviceRepo;

    @Autowired
    private SiteSettingsRepo siteSettingsRepo;

    @PostConstruct
    public void init() {
        // Seed Admin
        if (adminRepo.count() == 0) {
            AdminEntity admin = new AdminEntity();
            admin.setPassword("admin123"); // Default password
            adminRepo.save(admin);
        }

        // Seed Testimonials
        if (testimonialRepo.count() == 0) {
            testimonialRepo.save(TestimonialEntity.builder()
                    .name("Emma Thompson")
                    .quote("Outstanding service! The team went above and beyond to ensure I found the perfect investment property.")
                    .meta("2BR Apartment • Golf Course Road")
                    .stars(5)
                    .build());
            testimonialRepo.save(TestimonialEntity.builder()
                    .name("David Chen")
                    .quote("Professional, efficient, and results-driven. Gurgaon Homes helped me secure an amazing property with excellent ROI.")
                    .meta("3BR Penthouse • DLF Phase 5")
                    .stars(5)
                    .build());
            testimonialRepo.save(TestimonialEntity.builder()
                    .name("Fatima Al Zahra")
                    .quote("Exceptional service throughout. Their expertise in luxury properties is unmatched.")
                    .meta("4BR Villa • Sector 56")
                    .stars(5)
                    .build());
        }

        // Seed Services
        if (serviceRepo.count() == 0) {
            serviceRepo.save(ServiceEntity.builder()
                    .title("Property Sales")
                    .description(
                            "Find your perfect home or investment property across Gurgaon’s most desirable neighbourhoods.")
                    .icon("🏠")
                    .points(Arrays.asList("Luxury Apartments", "Exclusive Villas", "Premium Penthouses"))
                    .build());
            serviceRepo.save(ServiceEntity.builder()
                    .title("Off-Plan Projects")
                    .description(
                            "Access curated off-plan opportunities with high ROI potential and flexible payment plans.")
                    .icon("🏗️")
                    .points(Arrays.asList("Flexible Payment Plans", "High ROI Potential", "Developer Partnerships"))
                    .build());
            serviceRepo.save(ServiceEntity.builder()
                    .title("Property Investment")
                    .description("Expert guidance on property investment, portfolio building and yield optimisation.")
                    .icon("📈")
                    .points(Arrays.asList("Market Analysis", "ROI Calculations", "Portfolio Management"))
                    .build());
        }

        // Seed Settings
        if (siteSettingsRepo.count() == 0) {
            siteSettingsRepo.save(SiteSettingsEntity.builder()
                    .aboutTitle("Gurgaon Homes")
                    .aboutSubtitle("Your Real Estate Partner")
                    .aboutDescription(
                            "Gurgaon Homes is a premier real estate agency based in Gurgaon, India, specializing in luxury properties and exceptional service. With our deep knowledge of the Gurgaon real estate market, we connect discerning clients with exceptional properties in the most prestigious locations.")
                    .companyAddress(
                            "C-1527, Vyapar Kendra Rd, Block C, Sushant Lok Phase I, Sector 43, Gurugram, Haryana 122009")
                    .companyPhone("+91 97180 69177")
                    .companyEmail("gurgaonhomes@gmail.com")
                    .statTrustedDeals("100%")
                    .statAwards(5)
                    .statHappyClients(500)
                    .statVerifiedProps("100%")
                    .build());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String password = request.get("password");
        Optional<AdminEntity> admin = adminRepo.findByPassword(password);

        Map<String, Object> response = new HashMap<>();
        if (admin.isPresent()) {
            response.put("success", true);
            response.put("message", "Login successful");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Incorrect password");
            return ResponseEntity.status(401).body(response);
        }
    }
}
