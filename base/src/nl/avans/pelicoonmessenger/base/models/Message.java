package nl.avans.pelicoonmessenger.base.models;

import java.time.LocalDateTime;

public class Message {
    private int id;
    private LocalDateTime timestamp;
    private User user;
    private String message;

    Message(Builder builder) {
        id = builder.id;
        timestamp = builder.timestamp;
        user = builder.user;
        message = builder.message;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    public static class Builder {
        int id;
        LocalDateTime timestamp;
        User user;
        String message;

        public Builder() {

        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder timestamp() {
            this.timestamp = LocalDateTime.now();
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Message build() {
            return new Message(this);
        }
    }
}
