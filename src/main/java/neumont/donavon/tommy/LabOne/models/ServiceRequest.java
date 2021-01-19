package neumont.donavon.tommy.LabOne.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;

@Entity
public class ServiceRequest extends RepresentationModel<ServiceRequest> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


    @ManyToOne
    @JsonIgnore
    private User homeOwner;

    @ManyToOne
    @JsonIgnore
    private User contractor;

    @Column(nullable = false)
    private String dateOfService;

    @Column(nullable = false)
    private String timeOfService;

    @Column(nullable = false)
    private String streetAddress;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private int zipCode;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private RequestStatus requestStatus;

    public ServiceRequest(long id, User homeOwner, User contractor, String dateOfService, String streetAddress,
                          String city, int zipCode, RequestStatus requestStatus, String timeOfService, String state) {
        this.id = id;
        this.homeOwner = homeOwner;
        this.contractor = contractor;
        this.dateOfService = dateOfService;
        this.streetAddress = streetAddress;
        this.city = city;
        this.zipCode = zipCode;
        this.requestStatus = requestStatus;
        this.timeOfService = timeOfService;
        this.state = state;
    }

    public ServiceRequest() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getHomeOwner() {
        return homeOwner;
    }

    public void setHomeOwner(User homeOwner) {
        this.homeOwner = homeOwner;
    }

    public User getContractor() {
        return contractor;
    }

    public void setContractor(User contractor) {
        this.contractor = contractor;
    }

    public String getDateOfService() {
        return dateOfService;
    }

    public void setDateOfService(String dateOfService) {
        this.dateOfService = dateOfService;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getTimeOfService() {
        return timeOfService;
    }

    public void setTimeOfService(String timeOfService) {
        this.timeOfService = timeOfService;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
