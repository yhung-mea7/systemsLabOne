package neumont.donavon.tommy.LabOne.services;

import neumont.donavon.tommy.LabOne.models.User;
import neumont.donavon.tommy.LabOne.repositories.UserJPARepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServices {

    private final UserJPARepository userRepo;
    private final PasswordEncoder encoder;

     UserServices(UserJPARepository userRepo, PasswordEncoder encoder)
    {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    public void createUser(User user)
    {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepo.save(user);
    }
}
