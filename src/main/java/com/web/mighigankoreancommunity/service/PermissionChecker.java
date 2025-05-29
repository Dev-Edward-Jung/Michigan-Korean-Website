package com.web.mighigankoreancommunity.service;

import com.web.mighigankoreancommunity.entity.Restaurant;
import com.web.mighigankoreancommunity.entity.userDetails.CustomUserDetails;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
public class PermissionChecker {

    public boolean canAccess(Restaurant restaurant, CustomUserDetails user) {
        if (user.isOwner()) {
            return restaurant.getOwner().getId().equals(user.getOwner().getId());
        } else if (user.isEmployee()) {
            return user.getRestaurantEmployee() != null &&
                    user.getRestaurantEmployee().getRestaurant().getId().equals(restaurant.getId());
        }
        return false;
    }

    public void validateAccess(Restaurant restaurant, CustomUserDetails user) {
        if (!canAccess(restaurant, user)) {
            throw new AccessDeniedException("You do not have permission to access this restaurant.");
        }
    }



}
