package com.web.mighigankoreancommunity.repository.owner;


import com.web.mighigankoreancommunity.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {

//    find Member By Id
    Optional<Owner> findOwnerById(Long id);

//    Searching by Email
    boolean existsByOwnerEmail(String memberEmail);

//  login with Email
    Optional<Owner> findOwnerByOwnerEmail(String memberEmail);


}
