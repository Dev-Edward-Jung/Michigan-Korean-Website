package com.web.mighigankoreancommunity.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.web.mighigankoreancommunity.domain.MemberType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String memberName;

    @Column(nullable = false, unique = true)
    private String memberEmail;

    @Column(nullable = false)
    private String memberPassword;

    private String memberAddress;

    private String memberPhone;

    private MemberType memberType;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Restaurant> restaurantList = new ArrayList<>();



    // memberEmail
    @Override
    public String getUsername() {
        return memberEmail;
    }

    // Password
    @Override
    public String getPassword() {
        return memberPassword;
    }

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