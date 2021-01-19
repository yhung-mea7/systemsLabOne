package neumont.donavon.tommy.LabOne.repositories;

import neumont.donavon.tommy.LabOne.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJPARepository extends JpaRepository<User, String> {
    User findByEmail(String email);
}
