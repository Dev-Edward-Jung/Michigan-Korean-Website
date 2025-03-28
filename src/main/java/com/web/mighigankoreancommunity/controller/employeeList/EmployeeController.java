package com.web.mighigankoreancommunity.controller.employeeList;


import ch.qos.logback.core.model.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/page/employee")
@Controller
public class EmployeeController {
    @GetMapping("/list")
    public String employeeList() {
        return "restaurant/employee-list";
    }
}
