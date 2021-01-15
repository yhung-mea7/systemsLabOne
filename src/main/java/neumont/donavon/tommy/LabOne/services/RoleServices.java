package neumont.donavon.tommy.LabOne.services;

import neumont.donavon.tommy.LabOne.exceptions.ResourceNotFoundException;
import neumont.donavon.tommy.LabOne.models.Role;
import neumont.donavon.tommy.LabOne.repositories.RoleJPARepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServices {

    private final RoleJPARepository roleRepo;

    RoleServices(RoleJPARepository roleRepo)
    {
        this.roleRepo = roleRepo;
    }

    public void createRole(Role role)
    {
        roleRepo.save(role);
    }

    public List<Role> getAllRoles()
    {
        return roleRepo.findAll();
    }

    public Role getRoleById(long id)
    {
        return roleRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException(id));
    }
}
