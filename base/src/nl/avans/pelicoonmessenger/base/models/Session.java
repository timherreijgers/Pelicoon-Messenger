package nl.avans.pelicoonmessenger.base.models;

import java.io.Serializable;

public class Session implements Serializable {
    private int id;
    private String ip;
    private User user;

    Session(Builder builder) {
        id = builder.id;
        ip = builder.ip;
        user = builder.user;
    }

    public int getId() {
        return id;
    }

    public String getIp() {
        return ip;
    }

    public User getUser() {
        return user;
    }

    public static class Builder {
        int id;
        String ip;
        User user;

        public Builder() {

        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder ip(String ip) {
            this.ip = ip;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Session build() {
            return new Session(this);
        }
    }
}
