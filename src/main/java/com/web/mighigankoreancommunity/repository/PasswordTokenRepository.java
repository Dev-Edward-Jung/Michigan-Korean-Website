package com.web.mighigankoreancommunity.repository;

import com.web.mighigankoreancommunity.entity.Employee;
import com.web.mighigankoreancommunity.entity.Owner;
import com.web.mighigankoreancommunity.entity.PasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordTokenRepository extends JpaRepository<PasswordToken, Long> {
    Optional<PasswordToken> findByToken(String token);

    Optional<PasswordToken> findByOwner(Owner owner);

    Optional<PasswordToken> findByEmployee(Employee employee);


}
