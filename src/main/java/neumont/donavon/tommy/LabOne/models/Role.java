package neumont.donavon.tommy.LabOne.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Role extends RepresentationModel<Role> implements Serializable {

    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter @Setter
    private long id;

    @Column(nullable = false, unique = true)
    @Getter @Setter
    private String name;

    @ManyToMany(mappedBy = "userRoles", fetch = FetchType.EAGER)
    @JsonIgnore
    @Getter @Setter
    private Set<User> users = new HashSet<>();

    public Role(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
