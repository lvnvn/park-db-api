package db.parkdbapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostModel {
    String author;
    String created;
    String forum;
    Integer id;
    Boolean edited;
    String message;
    Integer parent;
    Integer thread;

    public PostModel(@JsonProperty("author") String author,
                     @JsonProperty("created") String created,
                     @JsonProperty("forum") String forum,
                     @JsonProperty("id") Integer id,
                     @JsonProperty("edited") Boolean edited,
                     @JsonProperty("message") String message,
                     @JsonProperty("parent") Integer parent,
                     @JsonProperty("thread") Integer thread) {
        this.author = author;
        this.created = created;
        this.forum = forum;
        this.id = id;
        this.edited = edited;
        this.message = message;
        this.parent = parent;
        this.thread = thread;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getForum() {
        return forum;
    }

    public void setForum(String forum) {
        this.forum = forum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getEdited() {
        return edited;
    }

    public void setEdited(Boolean edited) {
        this.edited = edited;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public Integer getThread() {
        return thread;
    }

    public void setThread(Integer thread) {
        this.thread = thread;
    }
}
