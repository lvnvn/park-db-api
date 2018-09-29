package db.parkdbapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BookModel {
    String author;
    String publisher;
    String title;
    String department;
    Integer id;

    public BookModel(@JsonProperty("author") String author,
                     @JsonProperty("publisher") String publisher,
                     @JsonProperty("title") String title,
                     @JsonProperty("department") String department,
                     @JsonProperty("id") Integer id){
        this.author = author;
        this.publisher = publisher;
        this.title = title;
        this.department = department;
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartement(String department) {
        this.department = department;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
