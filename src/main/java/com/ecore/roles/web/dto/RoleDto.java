package com.ecore.roles.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@EqualsAndHashCode
public class RoleDto {

    @JsonProperty
    private UUID id;
    @JsonProperty
    @NotBlank
    private String name;

}
