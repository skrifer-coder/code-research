package skrifer.github.com.http.response;

import lombok.Data;

import java.util.List;


@Data
public class KEDAJDAPIBaseResponse {
    private int success;
    private int error_code;
    private String description;
    private int total_count;
}
