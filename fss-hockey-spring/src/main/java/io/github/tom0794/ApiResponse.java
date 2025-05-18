package io.github.tom0794;

import java.util.HashMap;

public class ApiResponse<T> {
    private int status;
    private T content;

    public ApiResponse(int status, T content) {
        this.status = status;
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    @Override
    public String toString() {
        HashMap<Object, Object> responseMap = new HashMap<>();
        responseMap.put("status", this.status);
        responseMap.put("content", this.content);
        return String.valueOf(responseMap);
    }
}
