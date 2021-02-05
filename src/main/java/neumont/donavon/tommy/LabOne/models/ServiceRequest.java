package neumont.donavon.tommy.LabOne.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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
}
