package com.ecore.roles.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "A user in the system")
public class UserDto implements Serializable {

    private static final long serialVersionUID = 1340568139714227444L;

    @JsonProperty
    @Schema(description = "The uuid of the user", example = "fd282131-d8aa-4819-b0c8-d9e0bfb1b75c")
    private UUID id;

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "The first name of the user", example = "Gianni")
    private String firstName;

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "The last name of the user", example = "Wehner")
    private String lastName;

    @JsonProperty
    @Schema(description = "The name used as identification", example = "gianniWehner")
    private String displayName;

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "The url of the user's avatar",
            example = "https://cdn.fakercloud.com/avatars/rude_128.jpg")
    private String avatarUrl;

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Where to find the user", example = "Brakusstad")
    private String location;

}
