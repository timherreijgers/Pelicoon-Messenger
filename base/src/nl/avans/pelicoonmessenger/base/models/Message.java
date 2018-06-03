package nl.avans.pelicoonmessenger.base.models;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {
    private LocalDateTime timestamp;
    private Lobby lobby;
    private User user;
    private String message;

    Message(Builder builder) {
        timestamp = builder.timestamp;
        lobby = builder.lobby;
        user = builder.user;
        message = builder.message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Lobby getLobby() {
        return lobby;
    }

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "timestamp=" + timestamp +
                ", lobby=" + lobby +
                ", user=" + user +
                ", message='" + message + '\'' +
                '}';
    }

    public static class Builder {
        LocalDateTime timestamp;
        Lobby lobby;
        User user;
        String message;

        public Builder() {

        }

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder timestamp() {
            return timestamp(LocalDateTime.now());
        }

        public Builder lobby(Lobby lobby) {
            this.lobby = lobby;
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
