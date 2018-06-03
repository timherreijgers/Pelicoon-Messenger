package nl.avans.pelicoonmessenger.base.models;

import java.util.ArrayList;
import java.util.List;

public class Lobby {
    private final String name;
    private final List<User> users = new ArrayList<>();
    private final List<Message> messages = new ArrayList<>();

    public Lobby(String name) {
        this.name = name;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public void removeMessage(Message message) {
        messages.remove(message);
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Message> getMessages() {
        return messages;
    }
}
