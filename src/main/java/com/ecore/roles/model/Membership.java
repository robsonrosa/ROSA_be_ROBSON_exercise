package com.ecore.roles.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"team_id", "user_id"}))
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Membership implements Serializable {

    private static final long serialVersionUID = -6933764204165149761L;

    @Id
    @GeneratedValue(generator = "membership-uuid-generator")
    @GenericGenerator(name = "membership-uuid-generator", strategy = "uuid2")
    @Type(type = "uuid-char")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    @EqualsAndHashCode.Include
    private Role role;

    @Column(name = "user_id", nullable = false)
    @Type(type = "uuid-char")
    @EqualsAndHashCode.Include
    private UUID userId;

    @Column(name = "team_id", nullable = false)
    @Type(type = "uuid-char")
    @EqualsAndHashCode.Include
    private UUID teamId;

}
