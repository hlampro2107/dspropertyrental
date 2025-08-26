package hua.dt.ds.work2025.propertyrental.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "properties")
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String city;

    @Column
    private String postalCode;

    @Column(nullable = false)
    private double size;

    @Column(nullable = false)
    private double rentalPrice;

    @Column
    private int yearOfManufacture;

    @Column
    private int	floor;

    @Column
    private int	bedrooms;

    @Column
    private boolean	parking;

    @Column
    private boolean autonomousHeating;

    @Enumerated(EnumType.STRING)
    private PropertyStatus status;

    @OneToOne(mappedBy = "property", cascade = CascadeType.ALL)
    private PropertyForRent propertyForRent;

    @ManyToOne
    @JoinColumn(name = "ownerId")
    private User owner;


    public Property() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public double getRentalPrice() {
        return rentalPrice;
    }

    public void setRentalPrice(double rentalPrice) {
        this.rentalPrice = rentalPrice;
    }

    public PropertyForRent getPropertyForRent() {
        return propertyForRent;
    }

    public void setPropertyForRent(PropertyForRent propertyForRent) {
        this.propertyForRent = propertyForRent;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public int getYearOfManufacture() {
        return yearOfManufacture;
    }

    public void setYearOfManufacture(int yearOfManufacture) {
        this.yearOfManufacture = yearOfManufacture;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(int bedrooms) {
        this.bedrooms = bedrooms;
    }

    public boolean isParking() {
        return parking;
    }

    public void setParking(boolean parking) {
        this.parking = parking;
    }

    public boolean isAutonomousHeating() {
        return autonomousHeating;
    }

    public void setAutonomousHeating(boolean autonomousΗeating) {
        this.autonomousHeating = autonomousΗeating;
    }

    public PropertyStatus getStatus() {
        return status;
    }

    public void setStatus(PropertyStatus status) {
        this.status = status;
    }

    public String getStatusText() {
        if (status == null) return "Άγνωστο";
        switch (status) {
            case AVAILABLE: return "Διαθέσιμο";
            case RENTED: return "Ενοικιάστηκε";
            default: return status.name();
        }
    }
}
