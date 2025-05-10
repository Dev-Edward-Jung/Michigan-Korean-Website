package com.web.mighigankoreancommunity.repository.owner;


import com.web.mighigankoreancommunity.entity.Owner;
import com.web.mighigankoreancommunity.entity.PasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {

//    find Member By Id
    Optional<Owner> findOwnerById(Long id);

//    Searching by Email
    boolean existsByEmail(String email);

    Optional<Owner> findOwnerByEmail(String email);


    Optional<Owner> findOwnerByPasswordToken_Token(String token);


}
