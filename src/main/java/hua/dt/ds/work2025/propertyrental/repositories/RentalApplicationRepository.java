package hua.dt.ds.work2025.propertyrental.repositories;

import hua.dt.ds.work2025.propertyrental.entities.RentalApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalApplicationRepository extends JpaRepository<RentalApplication, Long> {

    List<RentalApplication> findByTenantId(Long tenantId);

    void deleteByPropertyForRentId(Long propertyForRentId);

    void deleteByTenantId(Long tenantId);


}
