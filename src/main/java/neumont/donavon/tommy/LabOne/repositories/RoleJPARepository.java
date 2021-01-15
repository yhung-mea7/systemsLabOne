package neumont.donavon.tommy.LabOne.repositories;

import neumont.donavon.tommy.LabOne.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleJPARepository extends JpaRepository<Role, Long> {
}
