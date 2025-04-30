package com.web.mighigankoreancommunity.config;

import com.web.mighigankoreancommunity.interceptor.restaurant.RestaurantContextInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final RestaurantContextInterceptor restaurantContextInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(restaurantContextInterceptor)
                .addPathPatterns("/**");
    }
}
