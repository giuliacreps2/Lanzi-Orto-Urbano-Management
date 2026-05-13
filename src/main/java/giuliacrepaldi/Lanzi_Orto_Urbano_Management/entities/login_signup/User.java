package giuliacrepaldi.Lanzi_Orto_Urbano_Management.entities.login_signup;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import giuliacrepaldi.Lanzi_Orto_Urbano_Management.enums.StatusB2b;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString


@Entity
@Table(name = "users")
@JsonIgnoreProperties({"accountNonExpired", "accountNonLocked", "authorities", "credentialsNonExpired", "enabled"})
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;

    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    @JsonIgnore
    private boolean isActive;
    @Column(nullable = false)
    private boolean isEmailVerified;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    @Column(name = "deleted_at", nullable = true)
    private LocalDateTime deletedAt;
    private boolean enabled;
    @Column(nullable = false)
    private boolean privacyAccepted = false;

    private LocalDateTime privacyAcceptedAt;

    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private B2bProfile b2bProfile;

    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private B2cProfile b2cProfile;


    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String email, String password, Boolean isActive, Boolean isEmailVerified, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.email = email;
        this.password = password;
        this.isActive = isActive;
        this.isEmailVerified = isEmailVerified;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        if (this.b2bProfile != null) {
            return StatusB2b.APPROVED.equals(this.b2bProfile.getStatusB2b());
        }
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isActive && this.isEmailVerified;
    }
}
