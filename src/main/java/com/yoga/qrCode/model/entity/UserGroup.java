package com.yoga.qrCode.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Table(name = "userGroup")
@Getter
@Setter
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class UserGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    @JoinColumn(name = "customerId")
    private Customer customer;
    @OneToOne
    @JoinColumn(name = "batchId")
    private Batch batch;
}
