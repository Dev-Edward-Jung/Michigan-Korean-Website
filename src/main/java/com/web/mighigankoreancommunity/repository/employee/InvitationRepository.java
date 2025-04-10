package com.web.mighigankoreancommunity.repository.employee;

import com.web.mighigankoreancommunity.entity.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    Invitation findByToken(String token);
}
