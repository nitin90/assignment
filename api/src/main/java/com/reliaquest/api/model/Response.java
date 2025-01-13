package com.reliaquest.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Response<T>(String status, String statusMessage, T employees) {

    public static Response<List<String>> emptyResponse() {
        return new Response<>("SUCCESS", null, new ArrayList<String>());
    }
}
