package neumont.donavon.tommy.LabOne.resources;

import neumont.donavon.tommy.LabOne.controllers.ServiceController;
import neumont.donavon.tommy.LabOne.models.ServiceRequest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ServiceRequestResource extends ServiceRequest {


   public ServiceRequestResource(final ServiceRequest serviceRequest)
   {
       super(serviceRequest.getId(), serviceRequest.getHomeOwner(), serviceRequest.getContractor(), serviceRequest.getDateOfService(), serviceRequest.getStreetAddress(),
               serviceRequest.getCity(), serviceRequest.getZipCode(), serviceRequest.getRequestStatus(), serviceRequest.getTimeOfService(), serviceRequest.getState());
       add(linkTo(methodOn(ServiceController.class).getAllServices()).withRel("All-Services"));
       add(linkTo(methodOn(ServiceController.class).getOneById(serviceRequest.getId())).withSelfRel());

   }


}
