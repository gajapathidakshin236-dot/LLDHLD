package com.company.patterns.memento;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * MEMENTO — snapshot an object's state WITHOUT exposing its internals,
 * so it can be restored later. The undo mechanism when you want to restore
 * STATE (vs Command, which replays/reverses OPERATIONS).
 *
 * Roles:
 *   Originator — the object whose state we snapshot (Editor)
 *   Memento    — the opaque snapshot (EditorState: immutable, no setters)
 *   Caretaker  — stores mementos but CANNOT look inside (History)
 *
 * Command-undo vs Memento-undo:
 *   Command: store operations, undo by reversing each — cheap memory,
 *            needs every op to be reversible.
 *   Memento: store full snapshots — trivially correct, costs memory.
 */
public class MementoDemo {
    public static void main(String[] args) {
        Editor editor = new Editor();
        History history = new History();

        editor.type("Dear team,");
        history.save(editor.snapshot());

        editor.type(" the deadline moved");
        history.save(editor.snapshot());

        editor.type(" ...oops wrong paragraph");
        System.out.println("now:      '" + editor.text() + "'");

        editor.restore(history.pop());
        System.out.println("undo:     '" + editor.text() + "'");

        editor.restore(history.pop());
        System.out.println("undo x2:  '" + editor.text() + "'");
    }
}

/** ORIGINATOR */
class Editor {
    private StringBuilder content = new StringBuilder();

    void type(String s) { content.append(s); }
    String text()       { return content.toString(); }

    EditorState snapshot()            { return new EditorState(content.toString()); }
    void restore(EditorState state)   { content = new StringBuilder(state.content()); }
}

/** MEMENTO — immutable; only Editor understands what to do with it. */
final class EditorState {
    private final String content;

    EditorState(String content) { this.content = content; }

    String content() { return content; }
}

/** CARETAKER — a dumb stack of snapshots; never inspects them. */
class History {
    private final Deque<EditorState> states = new ArrayDeque<>();

    void save(EditorState s)   { states.push(s); }
    EditorState pop()          { return states.pop(); }
}
