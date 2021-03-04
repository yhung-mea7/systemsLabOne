package neumont.donavon.tommy.LabOne.services;

import neumont.donavon.tommy.LabOne.exceptions.ResourceNotFoundException;
import neumont.donavon.tommy.LabOne.exceptions.UserAlreadyExistException;
import neumont.donavon.tommy.LabOne.models.Role;
import neumont.donavon.tommy.LabOne.models.User;
import neumont.donavon.tommy.LabOne.repositories.RoleJPARepository;
import neumont.donavon.tommy.LabOne.repositories.UserJPARepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServices {

    private final UserJPARepository userRepo;
    private final RoleJPARepository roleRepo;
    private final PasswordEncoder encoder;
    private final RabbitTemplate amqpTemplate;

     UserServices(UserJPARepository userRepo, RoleJPARepository roleRepo, PasswordEncoder encoder, RabbitTemplate amqpTemplate)
    {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.encoder = encoder;
        this.amqpTemplate = amqpTemplate;


    }

    public void createUser(User user)
    {
        if(userRepo.findByEmail(user.getEmail()) != null || userRepo.findById(user.getUsername()).isPresent()){
            throw new UserAlreadyExistException();
        }
        long roleId = user.getUserType().ordinal();
        Role r = roleRepo.findById(roleId).orElseThrow(() -> new ResourceNotFoundException(roleId));
        user.setPassword(encoder.encode(user.getPassword()));
        user.getUserRoles().add(r);
        userRepo.save(user);
    }

    public void updateAllFields(final String id, final User copy)
    {
        User u = userRepo.findById(id).orElseThrow(() -> new UsernameNotFoundException(id));
        u.setPassword(encoder.encode(copy.getPassword()));
        u.setStreetAddress(copy.getStreetAddress());
        u.setCity(copy.getCity());
        u.setZipCode(copy.getZipCode());
        u.setState(copy.getState());
        u.setName(copy.getName());
        u.setUserType(copy.getUserType());
        u.setUserRoles(copy.getUserRoles());
        userRepo.save(u);
        String[] payload = {u.getEmail(), copy.getPassword()};
        amqpTemplate.convertAndSend("emailExchange", "email.new", payload);

    }

    public void update(final String id, final User copy)
    {
        User u = userRepo.findById(id).orElseThrow(() -> new UsernameNotFoundException(id));
        if(copy.getPassword() != null && !copy.getPassword().trim().isEmpty())
        {
            u.setPassword(encoder.encode(copy.getPassword()));
            String[] payload = {u.getEmail(), copy.getPassword() };
            amqpTemplate.convertAndSend("emailExchange", "email.new", payload);
        }
        if(copy.getStreetAddress() != null && !copy.getStreetAddress().trim().isEmpty())
        {
            u.setStreetAddress(copy.getStreetAddress());
        }
        if(copy.getCity() != null && !copy.getCity().trim().isEmpty())
        {
            u.setCity(copy.getCity());
        }
        if(copy.getZipCode() != 0){
            u.setZipCode(copy.getZipCode());
        }
        if(copy.getName() != null && !copy.getName().trim().isEmpty()){
            u.setName(copy.getName());
        }
        if(copy.getUserType() != null)
        {
            u.setUserType(copy.getUserType());
        }
        if(copy.getUserRoles() != null && !copy.getUserRoles().isEmpty())
        {
            u.setUserRoles(copy.getUserRoles());
        }
        if(copy.getState() != null && !copy.getUsername().trim().isEmpty())
        {
            u.setState(copy.getState());
        }
        userRepo.save(u);

    }

    public void resetPassword(final String userId)
    {
        User u = userRepo.findById(userId).orElseThrow(ResourceNotFoundException::new);
        String generatedString = RandomStringUtils.randomAlphabetic(10);
        u.setPassword(encoder.encode(generatedString));
        userRepo.save(u);
        String[] payload = {u.getEmail(), generatedString};
        amqpTemplate.convertAndSend("emailExchange", "email.new", payload);

    }

    public void deleteUser(final String id)
    {
        userRepo.deleteById(id);
    }
}
