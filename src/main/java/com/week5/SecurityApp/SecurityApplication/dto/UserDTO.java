package com.week5.SecurityApp.SecurityApplication.dto;

import com.week5.SecurityApp.SecurityApplication.entities.enums.SubscriptionPlan;
import lombok.Data;

/**
 * The type User dto.
 */
@Data
public class UserDTO {

    private Long id;
    private String email;
    private String password;
    private String name;
    private SubscriptionPlan subscriptionPlan;
}
