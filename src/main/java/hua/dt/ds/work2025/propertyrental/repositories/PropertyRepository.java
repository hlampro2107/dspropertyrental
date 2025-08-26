package hua.dt.ds.work2025.propertyrental.repositories;

import hua.dt.ds.work2025.propertyrental.entities.Property;
import hua.dt.ds.work2025.propertyrental.entities.PropertyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long>, JpaSpecificationExecutor<Property> {


}
