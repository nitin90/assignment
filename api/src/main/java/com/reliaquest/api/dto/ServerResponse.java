package com.reliaquest.api.dto;

import java.util.List;

public record ServerResponse<T>(T data, String status) {
}
