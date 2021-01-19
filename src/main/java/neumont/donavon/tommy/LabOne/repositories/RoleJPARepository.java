package neumont.donavon.tommy.LabOne.repositories;

import neumont.donavon.tommy.LabOne.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleJPARepository extends JpaRepository<Role, Long> {
}
