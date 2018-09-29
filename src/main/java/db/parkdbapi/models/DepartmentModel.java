package db.parkdbapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused")
public class DepartmentModel {
    Integer id;
    String name;
    Integer books;

    public DepartmentModel(@JsonProperty("id") Integer id,
                      @JsonProperty("name") String name,
                      @JsonProperty("books") Integer books){
        this.id = id;
        this.name = name;
        this.books = books;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBooks() {
        return books;
    }

    public void setBooks(Integer books) {
        this.books = books;
    }
}
