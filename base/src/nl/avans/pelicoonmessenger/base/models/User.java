package nl.avans.pelicoonmessenger.base.models;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String username;

    User(Builder builder) {
        id = builder.id;
        username = builder.username;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public static class Builder {
        int id;
        String username;

        public Builder() {

        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
