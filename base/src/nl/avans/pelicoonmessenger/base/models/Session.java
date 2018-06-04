package nl.avans.pelicoonmessenger.base.models;

import java.io.Serializable;

public class Session implements Serializable, Comparable<Session> {
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



    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", ip='" + ip + '\'' +
                ", user=" + user +
                '}';
    }

    @Override
    public int compareTo(Session o) {
        return user.compareTo(o.user);
    }

    public static class Builder {
        int id;
        String ip;
        User user;

        public Builder() {
            id = (int) Math.round(Math.random() * 10000);
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
