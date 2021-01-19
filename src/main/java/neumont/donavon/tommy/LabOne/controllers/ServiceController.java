package neumont.donavon.tommy.LabOne.controllers;

import neumont.donavon.tommy.LabOne.exceptions.BadServiceRequestStatusUpdateException;
import neumont.donavon.tommy.LabOne.models.ServiceRequest;
import neumont.donavon.tommy.LabOne.resources.ServiceRequestResource;
import neumont.donavon.tommy.LabOne.services.ServiceRequestServices;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/services")
public class ServiceController {

    private final ServiceRequestServices serviceUtils;

    ServiceController(ServiceRequestServices serviceUtils) {
        this.serviceUtils = serviceUtils;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PROVIDER')")
    public HttpEntity<CollectionModel<ServiceRequestResource>> getAllServices() {
        CollectionModel<ServiceRequestResource> resources = serviceUtils.parseToCollectionModel(serviceUtils.getAllOpenRequest());
        resources.add(linkTo(methodOn(ServiceController.class).getAllServices()).withSelfRel());
        return new ResponseEntity<>(resources, HttpStatus.OK);

    }

    @GetMapping(params = "street")
    @PreAuthorize("hasAuthority('PROVIDER')")
    public HttpEntity<CollectionModel<ServiceRequestResource>> searchByStreetAddress(@RequestParam String street) {
        CollectionModel<ServiceRequestResource> resources = serviceUtils.parseToCollectionModel(serviceUtils.searchByStreetAddress(street));
        resources.add(linkTo(methodOn(ServiceController.class).searchByStreetAddress(street)).withSelfRel());
        resources.add(linkTo(methodOn(ServiceController.class).getAllServices()).withRel("All-Services"));
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @GetMapping(params = "city")
    @PreAuthorize("hasAuthority('PROVIDER')")
    public HttpEntity<CollectionModel<ServiceRequestResource>> searchByCity(@RequestParam String city) {
        CollectionModel<ServiceRequestResource> resources = serviceUtils.parseToCollectionModel(serviceUtils.searchByCity(city));
        resources.add(linkTo(methodOn(ServiceController.class).getAllServices()).withRel("All-Services"));
        resources.add(linkTo(methodOn(ServiceController.class).searchByCity(city)).withSelfRel());
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @GetMapping(params = "zip")
    @PreAuthorize("hasAuthority('PROVIDER')")
    public HttpEntity<CollectionModel<ServiceRequestResource>> searchByZip(@RequestParam int zip) {
        CollectionModel<ServiceRequestResource> resources = serviceUtils.parseToCollectionModel(serviceUtils.searchByZip(zip));
        resources.add(linkTo(methodOn(ServiceController.class).getAllServices()).withRel("All-Services"));
        resources.add(linkTo(methodOn(ServiceController.class).searchByZip(zip)).withSelfRel());
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('PROVIDER')")
    public HttpEntity<ServiceRequestResource> getOneById(@PathVariable long id) {
        return new ResponseEntity<>(new ServiceRequestResource(serviceUtils.getServiceById(id)), HttpStatus.OK);
    }

    @PostMapping(path = "/{id}", params = "status")
    @PreAuthorize("hasAuthority('PROVIDER')")
    public HttpEntity<ServiceRequestResource> updateServiceStatus(@PathVariable long id, @RequestHeader("Authorization") String contractorId, @RequestParam String status) {
        if ("claim".equalsIgnoreCase(status) && serviceUtils.claimRequest(id, serviceUtils.parseUsername(contractorId))) {
            return new ResponseEntity<>(new ServiceRequestResource(serviceUtils.getServiceById(id)), HttpStatus.OK);

        } else if ("complete".equalsIgnoreCase(status) && serviceUtils.completeRequest(id, serviceUtils.parseUsername(contractorId))) {
            return new ResponseEntity<>(new ServiceRequestResource(serviceUtils.getServiceById(id)), HttpStatus.OK);

        }
        throw new BadServiceRequestStatusUpdateException();
    }


    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public void createServiceRequest(@RequestHeader("Authorization") String userInfo, @RequestBody ServiceRequest serviceRequest) {
        serviceUtils.createRequest(serviceUtils.parseUsername(userInfo), serviceRequest);
    }

    @PutMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public HttpEntity<?> updateAllProperties(@PathVariable long id, @RequestBody ServiceRequest serviceRequest) {
        serviceUtils.updateAllFields(id, serviceRequest);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public HttpEntity<?> update(@PathVariable long id, @RequestBody ServiceRequest serviceRequest) {
        serviceUtils.update(id, serviceRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public HttpEntity<?> deleteById(@PathVariable long id) {
        serviceUtils.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
