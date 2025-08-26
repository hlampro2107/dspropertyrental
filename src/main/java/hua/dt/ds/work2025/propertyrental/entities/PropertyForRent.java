package hua.dt.ds.work2025.propertyrental.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "properties_for_rent")
public class PropertyForRent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "propertyId", referencedColumnName = "id")
    private Property property;

    @Column(nullable = false)
    private LocalDate listingDate;

    @Column
    @Size(max = 300)
    private String propertyNotes;

    @OneToMany(mappedBy = "propertyForRent",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<RentalApplication> rentalApplications = new ArrayList<>();

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getListingDate() {
        return listingDate;
    }

    public void setListingDate(LocalDate listingDate) {
        this.listingDate = listingDate;
    }

    public String getPropertyNotes() {
        return propertyNotes;
    }

    public void setPropertyNotes(String propertyNotes) {
        this.propertyNotes = propertyNotes;
    }

    public List<RentalApplication> getRentalApplications() {
        return rentalApplications;
    }

}
