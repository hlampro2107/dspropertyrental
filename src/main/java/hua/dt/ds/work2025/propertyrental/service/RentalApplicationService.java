package hua.dt.ds.work2025.propertyrental.service;

import hua.dt.ds.work2025.propertyrental.entities.*;
import hua.dt.ds.work2025.propertyrental.repositories.PropertyForRentRepository;
import hua.dt.ds.work2025.propertyrental.repositories.RentalApplicationRepository;
import hua.dt.ds.work2025.propertyrental.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RentalApplicationService {
    private RentalApplicationRepository rentalApplicationRepository;
    private PropertyForRentRepository propertyForRentRepository;
    private UserRepository userRepository;
    private PropertyForRentService propertyForrentService;
    private UserService userService;


   public RentalApplicationService(RentalApplicationRepository rentalApplicationRepository,
                                    PropertyForRentService propertyForRentService, UserService userService) {
        this.rentalApplicationRepository = rentalApplicationRepository;
        this.propertyForrentService  = propertyForRentService;
        this.userService = userService;
    }

    // Υποβολή αίτησης
    public RentalApplication createApplication(Long propertyForRentId, Long tenantId, String note) {
        PropertyForRent propertyForRent = propertyForrentService.getPropertiesForRent()
                .stream()
                .filter(p->p.getId().equals(propertyForRentId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("PropertyForRent not found"));

        User tenant = userService.getUserById(tenantId).orElseThrow(() -> new RuntimeException("Tenant not found"));

        RentalApplication application = new RentalApplication();
        application.setPropertyForRent(propertyForRent);
        application.setTenant(tenant);
        application.setStatus(RentalApplicationStatus.PENDING);
        application.setApplicationDate(LocalDateTime.now());
        application.setRentalNotes(note);

        return rentalApplicationRepository.save(application);
    }

    public RentalApplication getRentalApplication(long id) { return  rentalApplicationRepository.findById(id).get();}

    public List<RentalApplication> getApplicationsByUserId(Long userId) { return rentalApplicationRepository.findByTenantId(userId); }

    public void deleteRentalApplication(long id) { rentalApplicationRepository.deleteById(id); }

    public void saveRentalApplication(RentalApplication rentalApplication) {
        rentalApplicationRepository.save(rentalApplication);
    }

    public List<Long> getAppliedPropertyIdsByUserId(Long userId) {
        return rentalApplicationRepository.findByTenantId(userId)
                .stream()
                .map(app -> app.getPropertyForRent().getId())
                .toList();
    }

}
