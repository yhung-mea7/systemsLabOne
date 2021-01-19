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

    public void createRole(final Role role)
    {
        roleRepo.save(role);
    }

    public List<Role> getAllRoles()
    {
        return roleRepo.findAll();
    }

    public Role getRoleById(final long id)
    {
        return roleRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException(id));
    }
    public void deepUpdate(final long id, final Role copy)
    {
        Role r = roleRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        r.setName(copy.getName());
        r.setUsers(copy.getUsers());
        roleRepo.save(r);
    }
    public void update(final long id, final Role copy)
    {
        Role r = roleRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        if(copy.getName() != null && !copy.getName().trim().isEmpty())
        {
            r.setName(copy.getName());
        }
        if(copy.getUsers() != null && !copy.getUsers().isEmpty())
        {
            r.setUsers(copy.getUsers());
        }
        roleRepo.save(r);
    }
    public void deleteRole(final long id)
    {
        roleRepo.deleteById(id);
    }
}
