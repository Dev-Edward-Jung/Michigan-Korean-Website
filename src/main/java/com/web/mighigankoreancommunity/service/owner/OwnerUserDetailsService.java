package com.web.mighigankoreancommunity.service.owner;


import com.web.mighigankoreancommunity.entity.userDetails.CustomUserDetails;
import com.web.mighigankoreancommunity.entity.Owner;
import com.web.mighigankoreancommunity.repository.owner.OwnerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OwnerUserDetailsService implements UserDetailsService {

    private final OwnerRepository ownerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Owner 호출됨!");
        Owner owner = ownerRepository.findOwnerByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Owner not found."));
        return new CustomUserDetails(owner);
    }


    public boolean existsByEmail(String email) {
        return ownerRepository.existsByEmail(email);
    }
}