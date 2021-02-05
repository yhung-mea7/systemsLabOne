package neumont.donavon.tommy.LabOne.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class User implements UserDetails {

    @Id
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    @Getter @Setter
    private String email;

    @Column(nullable = false)
    @Getter @Setter
    private String password;

    @Column(nullable = false)
    @Getter @Setter
    private String name;

    @Column(nullable = false)
    @Getter @Setter
    private String streetAddress;

    @Column(nullable = false)
    @Getter @Setter
    private String city;

    @Column(nullable = false)
    @Getter @Setter
    private String state;

    @Column(nullable = false)
    @Getter @Setter
    private int zipCode;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 1")
    @Getter @Setter
    private UserType userType;

    @Column(nullable = false)
    @ManyToMany
    @Getter @Setter
    private Set<Role> userRoles = new HashSet<>();


    @OneToMany(mappedBy = "homeOwner")
    @Getter @Setter
    private List<ServiceRequest> openRequest = new ArrayList<>();

    @OneToMany(mappedBy = "contractor")
    @Getter @Setter
    private List<ServiceRequest> requestsAccepted = new ArrayList<>();

    @Override
    @Transient
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.userRoles.stream()
                .map(s -> new GrantedAuthority() {
                    @Override
                    public String getAuthority() {
                        return s.getName();
                    }
                }).collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    @Transient
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @Transient
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @Transient
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @Transient
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }


}
