package com.web.mighigankoreancommunity.repository.employee;

import com.web.mighigankoreancommunity.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    public Optional<Employee> findEmployeeByInvitation_Token(String invitationToken);

    public Optional<Employee> findEmployeeByEmail(String email);

    @Query("SELECT e FROM Employee e " +
            "LEFT JOIN FETCH e.restaurantEmployeeList rel " +
            "LEFT JOIN FETCH rel.restaurant r " +
            "WHERE e.email = :email")
    Optional<Employee> findByEmailWithRestaurants(@Param("email") String email);


    Optional<Employee> findByPasswordToken_Token(String token);

    public boolean existsByEmail(String email);
}
