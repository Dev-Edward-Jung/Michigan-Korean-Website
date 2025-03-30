package com.web.mighigankoreancommunity.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "rollType")
public class RoleType {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;

    private boolean defaultRole = false;

}
