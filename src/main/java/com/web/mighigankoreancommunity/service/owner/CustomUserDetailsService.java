package com.web.mighigankoreancommunity.service.owner;


import com.web.mighigankoreancommunity.entity.CustomUserDetails;
import com.web.mighigankoreancommunity.entity.Owner;
import com.web.mighigankoreancommunity.repository.owner.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {


    private final OwnerRepository ownerRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String ownerEmail) throws UsernameNotFoundException {
        Owner owner = ownerRepository.findOwnerByOwnerEmail(ownerEmail);
        if (owner == null) {
            throw new UsernameNotFoundException("Owner not found with email: " + ownerEmail);
        }
        return new CustomUserDetails(owner);
    }
}