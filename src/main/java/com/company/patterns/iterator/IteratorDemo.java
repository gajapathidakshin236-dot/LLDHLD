package com.company.patterns.iterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * ITERATOR — walk a collection WITHOUT exposing how it stores things.
 *
 * You use this constantly: every for-each loop compiles to
 * iterator()/hasNext()/next(). Implementing Iterable yourself is the point
 * here: the Playlist below hides that songs live in an ArrayList; it could
 * switch to a linked list or a DB cursor and NO CLIENT would change.
 *
 * Bonus fact from 02: Collection.iterator() is a textbook FACTORY METHOD —
 * each collection subclass decides which concrete iterator to build.
 */
public class IteratorDemo {
    public static void main(String[] args) {
        Playlist playlist = new Playlist();
        playlist.add("Bohemian Rhapsody");
        playlist.add("Hotel California");
        playlist.add("Stairway to Heaven");

        for (String song : playlist) {              // works ONLY because Iterable
            System.out.println("playing: " + song);
        }

        // Manual protocol (what for-each does under the hood):
        Iterator<String> it = playlist.iterator();
        while (it.hasNext()) {
            System.out.println("manual: " + it.next());
        }
    }
}

class Playlist implements Iterable<String> {
    private final List<String> songs = new ArrayList<>();   // HIDDEN detail

    void add(String song) { songs.add(song); }

    @Override
    public Iterator<String> iterator() {                    // factory method!
        return new PlaylistIterator();
    }

    /** Inner class so it can see the private list; clients never name it. */
    private class PlaylistIterator implements Iterator<String> {
        private int cursor = 0;

        @Override public boolean hasNext() { return cursor < songs.size(); }

        @Override public String next() {
            if (!hasNext()) throw new NoSuchElementException();
            return songs.get(cursor++);
        }
    }
}
