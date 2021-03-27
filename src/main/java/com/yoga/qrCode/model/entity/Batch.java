package com.yoga.qrCode.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Table(name = "batch")
@Getter
@Setter
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class Batch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "batchId")
    private String batchId;
    @Column(name = "name")
    private String name;
}
