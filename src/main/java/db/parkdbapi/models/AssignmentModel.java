package db.parkdbapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AssignementModel {
    Integer book;
    Integer user;
    String expiration;

    public AssignementModel(@JsonProperty("book") Integer book,
                     @JsonProperty("user") Integer user,
                     @JsonProperty("expiration") String expiration) {
        this.book = book;
        this.user = user;
        this.expiration = expiration;
    }

    public Integer getBook() {
        return book;
    }

    public void setBook(Integer book) {
        this.book = book;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }
}

