package com.ecore.roles.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"team_id", "user_id"}))
public class Membership {

    @Id
    @GeneratedValue(generator = "membership-uuid-generator")
    @GenericGenerator(name = "membership-uuid-generator", strategy = "uuid2")
    @Type(type = "uuid-char")
    private UUID id;

    @OneToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "team_id", nullable = false)
    private UUID teamId;

}
