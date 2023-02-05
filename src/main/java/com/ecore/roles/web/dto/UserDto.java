package com.ecore.roles.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class UserDto implements Serializable {

    private static final long serialVersionUID = 1340568139714227444L;

    @JsonProperty
    private UUID id;
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String firstName;

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lastName;

    @JsonProperty
    private String displayName;

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String avatarUrl;

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String location;

}
