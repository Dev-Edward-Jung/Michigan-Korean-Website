package com.web.mighigankoreancommunity.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.web.mighigankoreancommunity.domain.MemberRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.management.relation.Role;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "owner")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Owner implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ownerName;

    @Column(nullable = false, unique = true)
    private String ownerEmail;

    @Column(nullable = false)
    private String ownerPassword;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole = MemberRole.OWNER;

    @OneToOne(fetch = FetchType.LAZY)
    private PasswordToken passwordToken;


    @CreationTimestamp
    private LocalDateTime createdAt;

    // Save create update time automatically
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Restaurant> restaurantList = new ArrayList<>();



    // memberEmail
    @Override
    public String getUsername() {
        return ownerEmail;
    }

    // Password
    @Override
    public String getPassword() {
        return ownerPassword;
    }

    // Password



    // Authentification
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    // account expired
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Account locked
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Password expired
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Activation account
    @Override
    public boolean isEnabled() {
        return true;
    }


}