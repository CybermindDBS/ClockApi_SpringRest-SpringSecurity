package com.example.springrest_refresher.restapi.clockapi.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL) // Includes only non-null values when converting from Object to JSON.
@Schema(name = "ApiResponseWrapper")
public class ApiResponseWrapper<T> {
    @Schema(example = "200")
    Integer statusCode;
    @Schema(description = "Returned data object")
    T result;
    @Schema(description = "List of errors")
    List<String> errors;

    public ApiResponseWrapper(Integer statusCode, T data, List<String> errors) {
        this.statusCode = statusCode;
        this.result = data;
        this.errors = errors;
    }
}
