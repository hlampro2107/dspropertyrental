package hua.dt.ds.work2025.propertyrental.service;


import hua.dt.ds.work2025.propertyrental.entities.Property;
import hua.dt.ds.work2025.propertyrental.entities.PropertyForRent;
import hua.dt.ds.work2025.propertyrental.entities.PropertyStatus;
import hua.dt.ds.work2025.propertyrental.entities.User;
import hua.dt.ds.work2025.propertyrental.repositories.PropertyForRentRepository;
import hua.dt.ds.work2025.propertyrental.repositories.PropertyRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PropertyService  {
    private final PropertyRepository propertyRepository;

    public PropertyService(PropertyRepository propertyRepository, PropertyForRentRepository propertyForRentRepository) {
        this.propertyRepository = propertyRepository;
    }

    @Transactional
    public List<Property> getProperties() {
        return propertyRepository.findAll();
    }

    @Transactional
    public Property getProperty(Long id) {
       return propertyRepository.findById(id).get();
    }

    @Transactional
    public void saveProperty(Property property) {
        property.setStatus(PropertyStatus.AVAILABLE);
        propertyRepository.save(property);
    }

    @Transactional
    public void deleteProperty(Long id) {
        propertyRepository.deleteById(id);
    }

}
