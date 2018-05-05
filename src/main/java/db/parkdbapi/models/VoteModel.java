package db.parkdbapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VoteModel {
    String nickname;
    Integer thread;
    Integer voice;

    public VoteModel(@JsonProperty("nickname") String nickname,
                     @JsonProperty("thread") Integer thread,
                     @JsonProperty("voice") Integer voice) {
        this.nickname = nickname;
        this.thread = thread;
        this.voice = voice;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getThread() {
        return thread;
    }

    public void setThread(Integer thread) {
        this.thread = thread;
    }

    public Integer getVoice() {
        return voice;
    }

    public void setVoice(Integer voice) {
        this.voice = voice;
    }
}
