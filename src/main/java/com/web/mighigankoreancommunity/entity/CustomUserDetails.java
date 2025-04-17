package com.web.mighigankoreancommunity.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private final Owner owner;

    public CustomUserDetails(Owner owner) {
        this.owner = owner;
    }

    public Owner getOwner() {
        return owner;
    }

//     Role_USER, ROLE_ADMIN, ROLE_OWNER, ROLE
@Override
public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(owner.getMemberRole().name()));
}

    @Override
    public String getPassword() {
        return owner.getOwnerPassword();
    }

    @Override
    public String getUsername() {
        return owner.getOwnerEmail();
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
