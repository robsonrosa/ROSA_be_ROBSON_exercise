package com.ecore.roles.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Builder
@Getter
@Setter
@Schema(description = "A description of an error")
public class ErrorResponse {

    @Schema(description = "The http status of the error", example = "500")
    private final int status;

    @Schema(description = "The description of the error", example = "Houston, we have a problem")
    private final String error;

}
