package neumont.donavon.tommy.LabOne.resources;

import neumont.donavon.tommy.LabOne.controllers.RoleController;
import neumont.donavon.tommy.LabOne.models.Role;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class RoleResource extends Role {

    public RoleResource(final Role role) {
        super(role.getId(), role.getName());
        add(linkTo(RoleController.class).withRel("roles"));
        add(linkTo(methodOn(RoleController.class).getRoleById(role.getId())).withSelfRel());

    }
}
