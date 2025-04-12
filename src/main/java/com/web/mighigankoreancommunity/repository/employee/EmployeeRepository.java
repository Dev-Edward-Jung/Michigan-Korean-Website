package com.web.mighigankoreancommunity.repository.employee;

import com.web.mighigankoreancommunity.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    public Employee findEmployeeByInvitation_Token(String invitationToken);

}
