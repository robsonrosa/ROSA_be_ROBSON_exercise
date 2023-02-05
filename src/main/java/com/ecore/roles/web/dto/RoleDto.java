package com.ecore.roles.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@EqualsAndHashCode
public class RoleDto implements Serializable {

    private static final long serialVersionUID = -6495601927119593664L;

    @JsonProperty
    private UUID id;
    @JsonProperty
    @NotBlank
    private String name;

}
