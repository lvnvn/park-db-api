package db.parkdbapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ThreadModel {
    String author;
    String created;
    String forum;
    Integer id;
    String message;
    String slug;
    String title;
    Integer votes;

    public ThreadModel(@JsonProperty("author") String author,
                       @JsonProperty("created") String created,
                       @JsonProperty("forum") String forum,
                       @JsonProperty("id") Integer id,
                       @JsonProperty("message") String message,
                       @JsonProperty("slug") String slug,
                       @JsonProperty("title") String title,
                       @JsonProperty("votes") Integer votes){
        this.author = author;
        this.created = created;
        this.forum = forum;
        this.id = id;
        this.message = message;
        this.slug = slug;
        this.title = title;
        this.votes = votes;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
