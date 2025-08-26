package hua.dt.ds.work2025.propertyrental.repositories;

import hua.dt.ds.work2025.propertyrental.entities.Property;
import hua.dt.ds.work2025.propertyrental.entities.PropertyForRent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PropertyForRentRepository extends JpaRepository<PropertyForRent, Long>  {

    Optional<PropertyForRent> findByProperty(Property property);

}
