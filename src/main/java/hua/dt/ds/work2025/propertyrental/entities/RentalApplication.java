package hua.dt.ds.work2025.propertyrental.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "rentalApplications")
public class RentalApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Ιd")
    private long id;

    @ManyToOne
    @JoinColumn(name = "propertyForRentId", nullable = false)
    private PropertyForRent propertyForRent;

    @ManyToOne
    @JoinColumn(name = "tenantId", nullable = false)
    private User tenant;

    @Enumerated(EnumType.STRING)
    private RentalApplicationStatus status;


    @Column
    private LocalDateTime applicationDate;

    @Column
    private LocalDateTime decisionDate;

    @Column
    @Size(max = 300)
    private String rentalNotes;

    public User getTenant() {
        return tenant;
    }

    public void setTenant(User tenant) {
        this.tenant = tenant;
    }

    public PropertyForRent getPropertyForRent() {
        return propertyForRent;
    }

    public void setPropertyForRent(PropertyForRent property) {
        this.propertyForRent = property;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public RentalApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(RentalApplicationStatus status) {
        this.status = status;
    }

    public LocalDateTime getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDateTime applicationDate) {
        this.applicationDate = applicationDate;
    }

    public LocalDateTime getDecisionDate() {
        return decisionDate;
    }

    public void setDecisionDate(LocalDateTime decisionDate) {
        this.decisionDate = decisionDate;
    }

    public String getRentalNotes() {
        return rentalNotes;
    }

    public void setRentalNotes(String rentalNotes) {
        this.rentalNotes = rentalNotes;
    }

    public String getStatusText() {
        if (status == null) return "Άγνωστο";
        switch (status) {
            case PENDING: return "Σε Εκκρεμότητα";
            case APPROVED: return "Αποδοχή";
            case REJECTED: return "Απόρριψη";
            default: return status.name();
        }
    }


}
