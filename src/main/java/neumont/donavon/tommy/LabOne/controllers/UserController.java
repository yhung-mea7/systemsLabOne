package neumont.donavon.tommy.LabOne.controllers;

import neumont.donavon.tommy.LabOne.models.User;
import neumont.donavon.tommy.LabOne.services.ServiceRequestServices;
import neumont.donavon.tommy.LabOne.services.UserServices;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserServices userServices;
    private final ServiceRequestServices requestServices;

    UserController(UserServices userServices, ServiceRequestServices requestServices)
    {
        this.requestServices = requestServices;
        this.userServices = userServices;
    }



    @PostMapping
    @ResponseStatus(code= HttpStatus.CREATED)
    public void createUser(@RequestBody User user)
    {
        userServices.createUser(user);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER', 'PROVIDER')")
    @PutMapping(path="/{id}")
    @Transactional
    public HttpEntity<?> updateAllProperties(@PathVariable String id, @RequestBody User user)
    {
        userServices.updateAllFields(id, user);
        return ResponseEntity.noContent().build();

    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER', 'PROVIDER')")
    @PatchMapping(path="/{id}")
    @Transactional
    public HttpEntity<?> update(@PathVariable String id, @RequestBody User user)
    {
        userServices.update(id, user);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER', 'PROVIDER')")
    @DeleteMapping(path="/{id}")
    public HttpEntity<?> deleteUser(@PathVariable String id)
    {
        userServices.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('CUSTOMER') or hasAuthority('PROVIDER') or hasAuthority('ADMIN')")
    @PostMapping("/password-reset")
    public HttpEntity<?> resetPassword(@RequestHeader("Authorization") String userId){
        userServices.resetPassword(requestServices.parseUsername(userId));
        return ResponseEntity.noContent().build();
    }


}
