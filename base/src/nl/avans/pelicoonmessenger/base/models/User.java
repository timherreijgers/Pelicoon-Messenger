package nl.avans.pelicoonmessenger.base.models;

import java.io.Serializable;

public class User implements Serializable, Comparable<User> {
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }

    @Override
    public int compareTo(User o) {
        return username.compareTo(o.username);
    }

    public static class Builder {
        int id;
        String username;

        public Builder() {
            id = (int) Math.round(Math.random() * 10000);
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
