package com.web.mighigankoreancommunity.service.owner;


import com.web.mighigankoreancommunity.dto.OwnerDTO;
import com.web.mighigankoreancommunity.entity.Owner;
import com.web.mighigankoreancommunity.repository.owner.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OwnerService {
    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;

//    Register Method
    public Long saveOwner(Owner owner) {
        String rawPassword = owner.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        owner.setOwnerPassword(encodedPassword);
        ownerRepository.save(owner);
        return owner.getId();
    }

//    check Email
    public boolean getMemberByEmail(String email) {
        return ownerRepository.existsByOwnerEmail(email);
    }


    public OwnerDTO memberToDTO(Owner owner) {
        OwnerDTO dto = new OwnerDTO(
                owner.getId(),
                owner.getOwnerName(),
                owner.getOwnerEmail()
        );
        return dto;
    }






}
