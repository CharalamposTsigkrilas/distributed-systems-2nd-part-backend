package ds.familydoctor.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.*;

@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = "username"),
    @UniqueConstraint(columnNames = "email")
})
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(min = 5, max = 40)
  private String username;

  @NotBlank
  @Size(min = 8)
  private String password;

  @NotBlank
  @Email
  private String email;

  @NotBlank
  private String full_name;

  @NotBlank
  @Pattern(regexp = "\\d{10}")
  private String phone_number;

  @NotBlank
  private String home_address;

  @NotBlank
  private String country;

  @NotBlank
  private String continent;

  @NotBlank
  private String prefecture;

  @NotBlank
  private String city;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  private Citizen citizenProfile;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  private Doctor doctorProfile;
}
