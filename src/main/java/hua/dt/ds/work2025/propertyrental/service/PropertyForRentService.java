package hua.dt.ds.work2025.propertyrental.service;

import hua.dt.ds.work2025.propertyrental.entities.*;
import hua.dt.ds.work2025.propertyrental.repositories.PropertyForRentRepository;
import hua.dt.ds.work2025.propertyrental.repositories.PropertyRepository;
import hua.dt.ds.work2025.propertyrental.repositories.RentalApplicationRepository;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PropertyForRentService {


   private final PropertyForRentRepository propertyForRentRepository;
   private final RentalApplicationRepository rentalApplicationRepository;
   private final PropertyRepository propertyRepository;

   public PropertyForRentService(PropertyForRentRepository propertyForRentRepository,
                                 RentalApplicationRepository rentalApplicationRepository,
                                 PropertyRepository propertyRepository) {
       this.propertyForRentRepository = propertyForRentRepository;
       this.rentalApplicationRepository = rentalApplicationRepository;
       this.propertyRepository = propertyRepository;

   }

   public List<PropertyForRent> getPropertiesForRent() {return propertyForRentRepository.findAll(); }

   @Transactional
   public void deletePropertyForRent(long id) {
       propertyForRentRepository.deleteById(id); }

   public List<PropertyForRent> getAvailablePropertiesForRent() {
        return propertyForRentRepository.findAll().stream()
                .filter(pfr -> pfr.getProperty().getStatus() == PropertyStatus.AVAILABLE)
                .toList();
   }

    public List<PropertyForRent> getPropertiesForOwner(User owner) {
        return propertyForRentRepository.findAll().stream()
                .filter(pfr -> pfr.getProperty() != null
                        && pfr.getProperty().getOwner() != null
                        && pfr.getProperty().getOwner().getId().equals(owner.getId()))
                .toList();
    }

   public PropertyForRent getPropertyForRent(long id) {
       return propertyForRentRepository.findById(id).get();
   }

   public void savePropertyForRent(PropertyForRent propertyForRent) {
       propertyForRentRepository.save(propertyForRent);
   }

    public PropertyForRent getPropertyForRentByProperty(Property property) {
        return propertyForRentRepository.findByProperty(property)
                .orElse(null);
    }

    public List<PropertyForRent> searchPropertiesForRent(
            String city,
            Integer minSquareMeters,
            Integer maxSquareMeters,
            Double minPrice,
            Double maxPrice,
            Integer minYear,
            Integer maxYear,
            Integer floor,
            Integer bedrooms,
            Boolean parking,
            Boolean autonomousHeating
    ) {
        List<Property> properties = propertyRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (city != null && !city.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("city")), "%" + city.toLowerCase() + "%"));
            }
            if (minSquareMeters != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("size"), minSquareMeters));
            }
            if (maxSquareMeters != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("size"), maxSquareMeters));
            }
            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("rentalPrice"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("rentalPrice"), maxPrice));
            }
            if (minYear != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("yearOfManufacture"), minYear));
            }
            if (maxYear != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("yearOfManufacture"), maxYear));
            }
            if (floor != null) {
                predicates.add(cb.equal(root.get("floor"), floor));
            }
            if (bedrooms != null) {
                predicates.add(cb.equal(root.get("bedrooms"), bedrooms));
            }
            if (parking != null) {
                predicates.add(cb.equal(root.get("parking"), parking));
            }
            if (autonomousHeating != null) {
                predicates.add(cb.equal(root.get("autonomousHeating"), autonomousHeating));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        });


        List<PropertyForRent> propertiesForRent = new ArrayList<>();
        propertiesForRent = properties.stream()
                .map(Property::getPropertyForRent)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        propertiesForRent = propertiesForRent.stream()
                .filter(p -> p.getRentalApplications().stream()
                        .noneMatch(app -> app.getStatus() == RentalApplicationStatus.APPROVED))
                .collect(Collectors.toList());


        return propertiesForRent;

    }

    @Transactional
    public void savePropertyForRentWithOwner(Property property, PropertyForRent propertyForRent, User owner) {
        property.setOwner(owner);
        property.setStatus(PropertyStatus.AVAILABLE);
        propertyForRent.setListingDate(LocalDate.now());
        propertyForRent.setProperty(property);
        propertyRepository.save(property);
        propertyForRentRepository.save(propertyForRent);
    }


    public void updatePropertyForRentWithOwner(Property property, PropertyForRent propertyForRent, User owner) {
        property.setOwner(owner);
        property.setStatus(PropertyStatus.AVAILABLE);

        PropertyForRent existingPropertyForRent = propertyForRentRepository.findByProperty(property)
                .orElseThrow(() -> new RuntimeException("PropertyForRent not found"));

        existingPropertyForRent.setListingDate(LocalDate.now());
        existingPropertyForRent.setPropertyNotes(propertyForRent.getPropertyNotes());
        existingPropertyForRent.setProperty(property);

        propertyRepository.save(property);
        propertyForRentRepository.save(existingPropertyForRent);
    }





}
