package neumont.donavon.tommy.LabOne.repositories;

import neumont.donavon.tommy.LabOne.models.ServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRequestJPARepository extends JpaRepository<ServiceRequest, Long > {

    @Query(
            "SELECT s FROM ServiceRequest s WHERE s.streetAddress LIKE :search AND s.requestStatus = 0"
    )
     List<ServiceRequest> searchByStreetAddress(String search);

    @Query(
            "SELECT s FROM ServiceRequest s WHERE s.city = :search AND s.requestStatus = 0"
    )
    List<ServiceRequest> searchByCity(String search);

    @Query(
            "SELECT s FROM ServiceRequest s WHERE s.zipCode = :search AND s.requestStatus = 0"
    )
    List<ServiceRequest> searchByZipCode(int search);



}
