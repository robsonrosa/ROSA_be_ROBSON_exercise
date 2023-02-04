package com.ecore.roles.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@Entity
public class Role {

    @Id
    @GeneratedValue(generator = "role-uuid-generator")
    @GenericGenerator(name = "role-uuid-generator", strategy = "uuid2")
    @Type(type = "uuid-char")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

}
