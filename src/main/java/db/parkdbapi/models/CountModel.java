package db.parkdbapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CountModel {
    String author;
    String title;
    Integer assignments_count;

    public CountModel(@JsonProperty("author") String author,
                     @JsonProperty("title") String title,
                     @JsonProperty("assignments_count") Integer assignments_count){
        this.author = author;
        this.title = title;
        this.assignments_count = assignments_count;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getAssignments() {
        return assignments_count;
    }

    public void setAssignments(Integer assignments_count) {
        this.assignments_count = assignments_count;
    }
}
