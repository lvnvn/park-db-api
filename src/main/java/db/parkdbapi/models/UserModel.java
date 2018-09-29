package db.parkdbapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused")
public class UserModel {
    String nickname;
    String fullname;
    String email;
    Integer age;
    Integer id;

    public UserModel(@JsonProperty("nickname") String nickname,
                     @JsonProperty("fullname") String fullname,
                     @JsonProperty("email") String email,
                     @JsonProperty("age") Integer age,
                     @JsonProperty("id") Integer id) {
        this.nickname = nickname;
        this.fullname = fullname;
        this.email = email;
        this.age = age;
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
