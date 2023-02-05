package com.ecore.roles.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Role implements Serializable {

    private static final long serialVersionUID = 5462707772967932533L;

    @Id
    @GeneratedValue(generator = "role-uuid-generator")
    @GenericGenerator(name = "role-uuid-generator", strategy = "uuid2")
    @Type(type = "uuid-char")
    private UUID id;

    @EqualsAndHashCode.Include
    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Membership> memberships;

}
