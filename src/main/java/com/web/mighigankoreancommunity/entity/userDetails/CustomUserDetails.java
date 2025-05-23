package com.web.mighigankoreancommunity.entity.userDetails;

import com.web.mighigankoreancommunity.domain.MemberRole;
import com.web.mighigankoreancommunity.entity.Employee;
import com.web.mighigankoreancommunity.entity.Owner;
import com.web.mighigankoreancommunity.entity.RestaurantEmployee;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Member;
import java.util.Collection;
import java.util.Collections;
import java.util.List;



@Setter
@Getter
public class CustomUserDetails implements UserDetails {

    private Owner owner;
    private Employee employee;
    private RestaurantEmployee restaurantEmployee;

    public Long currentRestaurantId;

    public MemberRole getCurrentMemberRole() {
        if (restaurantEmployee != null) {
            return restaurantEmployee.getMemberRole();
        } else{
            return MemberRole.OWNER;
        }
    }


    public CustomUserDetails(Owner owner) {
        this.owner = owner;
    }

    public CustomUserDetails(Employee employee) {
        this.employee = employee;
    }

    public boolean isOwner() {
        return owner != null;
    }

    public boolean isEmployee() {
        return employee != null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        MemberRole role = getCurrentMemberRole();
        return List.of(new SimpleGrantedAuthority( role.name()));
    }

    @Override
    public String getPassword() {
        return isOwner() ? owner.getPassword() : employee.getPassword();
    }

    @Override
    public String getUsername() {
        return isOwner() ? owner.getEmail() : employee.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
