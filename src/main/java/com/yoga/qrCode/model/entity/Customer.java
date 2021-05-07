package com.yoga.qrCode.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Table(name = "customer")
@Getter
@Setter
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "userId")
    private String userId;
    @Column(name = "emailId")
    private String emailId;
    @Column(name = "setupCompleted")
    private boolean setupCompleted;
    @Column(name = "password")
    private String password;
    @Column(name = "qrCode")
    private String qrCode;
}
