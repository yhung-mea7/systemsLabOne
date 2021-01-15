package neumont.donavon.tommy.LabOne.controllers;

import neumont.donavon.tommy.LabOne.models.User;
import neumont.donavon.tommy.LabOne.services.UserServices;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserServices userServices;

    UserController(UserServices userServices)
    {
        this.userServices = userServices;
    }

    @PostMapping
    @ResponseStatus(code= HttpStatus.CREATED)
    public void createUser(@RequestBody User user)
    {
        userServices.createUser(user);
    }
}
