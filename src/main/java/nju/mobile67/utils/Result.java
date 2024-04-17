package nju.mobile67.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private T data;
    private String message;
    private boolean success;

    // getters, setters, constructors...

    public static <T> Result<T> success(T data) {
        return new Result<>(data, "success", true);
    }

    public static <T> Result<T> fail(String message) {
        return new Result<>(null, message, false);
    }
}

