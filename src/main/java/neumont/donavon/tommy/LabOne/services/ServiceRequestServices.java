package neumont.donavon.tommy.LabOne.services;

import neumont.donavon.tommy.LabOne.exceptions.ResourceNotFoundException;
import neumont.donavon.tommy.LabOne.models.RequestStatus;
import neumont.donavon.tommy.LabOne.models.ServiceRequest;
import neumont.donavon.tommy.LabOne.models.User;
import neumont.donavon.tommy.LabOne.repositories.ServiceRequestJPARepository;
import neumont.donavon.tommy.LabOne.repositories.UserJPARepository;
import neumont.donavon.tommy.LabOne.resources.ServiceRequestResource;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceRequestServices {

    private final ServiceRequestJPARepository serviceRepo;
    private final UserJPARepository userRepo;
    private final RabbitTemplate amqpTemplate;

    ServiceRequestServices(ServiceRequestJPARepository serviceRepo, UserJPARepository userRepo, RabbitTemplate amqpTemplate)
    {
        this.serviceRepo = serviceRepo;
        this.userRepo = userRepo;
        this.amqpTemplate = amqpTemplate;
    }

    public List<ServiceRequest> findAll()
    {
        return serviceRepo.findAll();
    }

    public void createRequest(final String userId, ServiceRequest request)
    {
        User u = userRepo.findById(userId).orElseThrow(ResourceNotFoundException::new);
        request.setHomeOwner(u);
        request.setStreetAddress(u.getStreetAddress());
        request.setCity(u.getCity());
        request.setZipCode(u.getZipCode());
        request.setState(u.getState());
        request.setRequestStatus(RequestStatus.OPEN);
        serviceRepo.save(request);
        String[] payload = {u.getEmail(), "Service Request Created", String.format("Your service request at %s has been successfully posted", u.getStreetAddress())};
        amqpTemplate.convertAndSend("emailExchange", "service.new", payload);
    }

    public List<ServiceRequest> getAllOpenRequest()
    {
        return serviceRepo.findAll()
                .stream()
                .filter(serviceRequest -> serviceRequest.getRequestStatus() == RequestStatus.OPEN)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "servicerequest", key = "#id")
    public ServiceRequest getServiceById(final long id)
    {
        return serviceRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @Cacheable(value = "servicerequest", key = "#streetAddress")
    public List<ServiceRequest> searchByStreetAddress(final String streetAddress)
    {
        return serviceRepo.searchByStreetAddress(streetAddress);
    }

    @Cacheable(value = "servicerequest", key = "#city")
    public List<ServiceRequest> searchByCity(final String city)
    {
        return serviceRepo.searchByCity(city);
    }

    @Cacheable(value = "servicerequest", key = "#zip")
    public List<ServiceRequest> searchByZip(final int zip)
    {
        return serviceRepo.searchByZipCode(zip);
    }

    @CachePut(value = "servicerequest", key = "#id")
    public void updateAllFields(final long id, final ServiceRequest serviceRequest)
    {
        ServiceRequest og = serviceRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        og.setHomeOwner(serviceRequest.getHomeOwner());
        og.setContractor(serviceRequest.getContractor());
        og.setStreetAddress(serviceRequest.getStreetAddress());
        og.setCity(serviceRequest.getCity());
        og.setState(serviceRequest.getState());
        og.setZipCode(serviceRequest.getZipCode());
        og.setDateOfService(serviceRequest.getDateOfService());
        og.setRequestStatus(serviceRequest.getRequestStatus());
        serviceRepo.save(og);
    }

    @CachePut(value = "servicerequest", key = "#id")
    public void update(final long id, final ServiceRequest updates)
    {
        ServiceRequest og = serviceRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        if(updates.getContractor() != null)
        {
            og.setContractor(updates.getContractor());
        }
        if(updates.getHomeOwner() != null)
        {
            og.setHomeOwner(updates.getHomeOwner());
        }
        if(updates.getStreetAddress() != null && !updates.getStreetAddress().trim().isEmpty())
        {
            og.setStreetAddress(updates.getStreetAddress());
        }
        if(updates.getCity() != null && !updates.getCity().trim().isEmpty())
        {
            og.setCity(updates.getCity());
        }
        if(updates.getZipCode() != 0)
        {
            og.setZipCode(updates.getZipCode());
        }
        if(updates.getDateOfService() != null)
        {
            og.setDateOfService(updates.getDateOfService());
        }
        if(updates.getRequestStatus() != null)
        {
            og.setRequestStatus(updates.getRequestStatus());
        }
        if(updates.getState() != null && !updates.getState().trim().isEmpty())
        {
            og.setState(updates.getState());
        }

        serviceRepo.save(og);
    }

    @CachePut(value = "servicerequest", key = "#id")
    public boolean claimRequest(final long id, final String serviceProviderID)
    {
        ServiceRequest request = serviceRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        User serviceProvider = userRepo.findById(serviceProviderID).orElseThrow(ResourceNotFoundException::new);
        if(request.getRequestStatus() == RequestStatus.OPEN)
        {
            request.setContractor(serviceProvider);
            request.setRequestStatus(RequestStatus.ACCEPTED);
            serviceRepo.save(request);
            String emailHeader = String.format("%s %s", request.getHomeOwner().getEmail(), serviceProvider.getEmail());
            String[] payload = {emailHeader, "Service Accepted", String.format("The service at %s has been claimed", request.getStreetAddress())};
            amqpTemplate.convertAndSend("emailExchange", "service.accept", payload);
            return true;
        }
        return false;
    }

    @CachePut(value = "servicerequest", key = "#id")
    public boolean completeRequest(final long id, final String serviceProviderID)
    {
        ServiceRequest request = serviceRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        User serviceProvider = userRepo.findById(serviceProviderID).orElseThrow(ResourceNotFoundException::new);
        if(request.getRequestStatus() == RequestStatus.ACCEPTED && request.getContractor().getUsername().equals(serviceProvider.getUsername()))
        {
            request.setRequestStatus(RequestStatus.COMPLETED);
            serviceRepo.save(request);
            String emailHeader = String.format("%s %s", request.getHomeOwner().getEmail(), serviceProvider.getEmail());
            String[] payload = {emailHeader, "Service Completed", String.format("The service at %s has been Completed", request.getStreetAddress())};
            amqpTemplate.convertAndSend("emailExchange", "service.complete", payload);
            return true;
        }
        return false;
    }

    @CacheEvict(value = "servicerequest", key = "#id")
    public void deleteById(final long id)
    {
        serviceRepo.deleteById(id);
    }

    public String parseUsername(final String authHeader)
    {
        if(authHeader != null && authHeader.startsWith("Basic"))
        {
            String baseCreds = authHeader.substring(5).trim();
            String decodedAuth = new String(Base64.decodeBase64(baseCreds), StandardCharsets.UTF_8);
            return decodedAuth.substring(0, decodedAuth.indexOf(":"));
        }
        return "";
    }

    public CollectionModel<ServiceRequestResource> parseToCollectionModel(final List<ServiceRequest> list)
    {
        List<ServiceRequestResource> rawList = list
                .stream()
                .map(ServiceRequestResource::new)
                .collect(Collectors.toList());
        return CollectionModel.of(rawList);
    }



}
