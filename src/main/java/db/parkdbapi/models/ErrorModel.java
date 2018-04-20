package db.parkdbapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorModel {
    String message;

    public ErrorModel(@JsonProperty("message") String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
