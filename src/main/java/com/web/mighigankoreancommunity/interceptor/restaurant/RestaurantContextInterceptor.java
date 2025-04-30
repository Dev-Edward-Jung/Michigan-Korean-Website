package com.web.mighigankoreancommunity.interceptor.restaurant;

import com.web.mighigankoreancommunity.entity.RestaurantEmployee;
import com.web.mighigankoreancommunity.entity.userDetails.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Component
@RequiredArgsConstructor
public class RestaurantContextInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String restaurantIdParam = request.getParameter("restaurantId");
        if (restaurantIdParam == null) return true;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails)) return true;

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        if (!userDetails.isEmployee()) return true;

        Long restaurantId = Long.valueOf(restaurantIdParam);

        // ✅ employee 내부의 restaurantEmployeeList에서 찾음
        RestaurantEmployee rel = userDetails.getEmployee().getRestaurantEmployeeList().stream()
                .filter(re -> re.getRestaurant().getId().equals(restaurantId))
                .findFirst()
                .orElse(null);

        userDetails.setRestaurantEmployee(rel);
        userDetails.setCurrentRestaurantId(restaurantId);

        return true;
    }

}
