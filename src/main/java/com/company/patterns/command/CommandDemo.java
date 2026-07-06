package com.company.patterns.command;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * COMMAND — turn a request into an OBJECT, so it can be queued, logged,
 * undone, or executed by someone who doesn't know what it does.
 *
 * Roles:
 *   Command          — interface: execute() (+ undo() if you support it)
 *   ConcreteCommand  — binds an action to a RECEIVER (the editor)
 *   Invoker          — runs commands, keeps history (the button/keymap)
 *   Receiver         — the object that actually does the work
 *
 * The undo stack is what makes this pattern earn its keep: because every
 * mutation is an object, "undo" = pop and reverse.
 */
public class CommandDemo {
    public static void main(String[] args) {
        TextEditor editor = new TextEditor();      // receiver
        CommandInvoker invoker = new CommandInvoker();

        invoker.run(new InsertTextCommand(editor, "Hello"));
        invoker.run(new InsertTextCommand(editor, ", world"));
        System.out.println("text: '" + editor.text() + "'");

        invoker.run(new DeleteLastCommand(editor, 7));
        System.out.println("text: '" + editor.text() + "'");

        invoker.undo();
        System.out.println("after undo: '" + editor.text() + "'");
        invoker.undo();
        System.out.println("after undo: '" + editor.text() + "'");
    }
}

/** The RECEIVER: dumb about commands, just does text operations. */
class TextEditor {
    private final StringBuilder sb = new StringBuilder();

    void insert(String s)        { sb.append(s); }
    String deleteLast(int n)     {                       // returns what it removed
        String removed = sb.substring(sb.length() - n);
        sb.delete(sb.length() - n, sb.length());
        return removed;
    }
    String text()                { return sb.toString(); }
}

interface Command {
    void execute();
    void undo();
}

class InsertTextCommand implements Command {
    private final TextEditor editor;
    private final String text;

    InsertTextCommand(TextEditor editor, String text) {
        this.editor = editor;
        this.text = text;
    }

    @Override public void execute() { editor.insert(text); }
    @Override public void undo()    { editor.deleteLast(text.length()); }
}

class DeleteLastCommand implements Command {
    private final TextEditor editor;
    private final int count;
    private String removed;              // remembered so undo can restore it

    DeleteLastCommand(TextEditor editor, int count) {
        this.editor = editor;
        this.count = count;
    }

    @Override public void execute() { removed = editor.deleteLast(count); }
    @Override public void undo()    { editor.insert(removed); }
}

/** The INVOKER: executes and remembers, knows nothing about text. */
class CommandInvoker {
    private final Deque<Command> history = new ArrayDeque<>();

    void run(Command c) {
        c.execute();
        history.push(c);
    }

    void undo() {
        if (!history.isEmpty()) history.pop().undo();
    }
}
