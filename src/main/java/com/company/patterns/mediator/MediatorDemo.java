package com.company.patterns.mediator;

import java.util.ArrayList;
import java.util.List;

/**
 * MEDIATOR — colleagues talk to a central hub instead of to each other.
 *
 * Without it: N users messaging each other = every user holds references to
 * every other user (N^2 coupling). With it: every user knows ONE object, the
 * ChatRoom; the room does the routing.
 *
 * Difference vs Observer: observer broadcasts one-way ("something happened");
 * mediator coordinates two-way conversations and ROUTING RULES between peers.
 * Difference vs Facade: facade simplifies a subsystem for outside clients;
 * mediator manages traffic AMONG the objects themselves.
 */
public class MediatorDemo {
    public static void main(String[] args) {
        ChatRoom room = new ChatRoom();
        User alice = new User("alice", room);
        User bob   = new User("bob", room);
        User carol = new User("carol", room);
        room.join(alice); room.join(bob); room.join(carol);

        alice.send("hi everyone");           // broadcast
        bob.sendTo("carol", "lunch?");       // direct — still via the room
    }
}

interface ChatMediator {
    void broadcast(String from, String message);
    void direct(String from, String to, String message);
}

class ChatRoom implements ChatMediator {
    private final List<User> users = new ArrayList<>();

    void join(User u) { users.add(u); }

    @Override
    public void broadcast(String from, String message) {
        for (User u : users) {
            if (!u.name().equals(from)) u.receive(from, message);
        }
    }

    @Override
    public void direct(String from, String to, String message) {
        for (User u : users) {
            if (u.name().equals(to)) { u.receive(from + " (dm)", message); return; }
        }
        System.out.println("room: no such user " + to);
    }
}

class User {
    private final String name;
    private final ChatMediator room;     // the ONLY reference a user holds

    User(String name, ChatMediator room) { this.name = name; this.room = room; }

    String name() { return name; }

    void send(String msg)                { room.broadcast(name, msg); }
    void sendTo(String to, String msg)   { room.direct(name, to, msg); }
    void receive(String from, String msg){ System.out.println(name + " <- " + from + ": " + msg); }
}
