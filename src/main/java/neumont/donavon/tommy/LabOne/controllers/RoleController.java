package neumont.donavon.tommy.LabOne.controllers;

import neumont.donavon.tommy.LabOne.models.Role;
import neumont.donavon.tommy.LabOne.resources.RoleResource;
import neumont.donavon.tommy.LabOne.services.RoleServices;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/roles")
public class RoleController {

    private final RoleServices roleServices;

    RoleController(RoleServices roleServices)
    {
        this.roleServices = roleServices;
    }

    @PostMapping
    @ResponseStatus(code= HttpStatus.CREATED )
    public void createRole(@RequestBody Role role)
    {
        roleServices.createRole(role);
    }

    @GetMapping
    public HttpEntity<CollectionModel<RoleResource>> getAllRoles()
    {
        List<RoleResource> rawRoles = roleServices.getAllRoles()
                .stream()
                .map(RoleResource::new)
                .collect(Collectors.toList());
        CollectionModel<RoleResource> resources = CollectionModel.of(rawRoles, linkTo(methodOn(RoleController.class).getAllRoles()).withSelfRel());
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @GetMapping(path="/{id}")
    public HttpEntity<RoleResource> getRoleById(@PathVariable long id)
    {
        return new ResponseEntity<>(new RoleResource(roleServices.getRoleById(id)), HttpStatus.OK);
    }

}
