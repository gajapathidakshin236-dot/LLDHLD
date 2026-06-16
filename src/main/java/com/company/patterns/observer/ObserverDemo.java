/**
 * OBSERVER PATTERN
 *
 * Intent: Define a one-to-many dependency so that when one object (the
 *         Subject) changes state, all its dependents (Observers) are
 *         notified automatically. Like YouTube subscribers.
 *
 * Two roles:
 *   Subject  — maintains a list of observers, notifies them on change
 *   Observer — registers with the subject, reacts to notifications
 *
 * Thread-safety note: this demo uses CopyOnWriteArrayList, which is safe
 * for "many notifications, occasional subscribe/unsubscribe" — exactly the
 * Observer profile. A plain ArrayList would risk ConcurrentModificationException
 * if someone unsubscribes during notification.
 *
 * Run: javac ObserverDemo.java && java ObserverDemo
 */
package com.company.patterns.observer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ObserverDemo {
    public static void main(String[] args) {
        WeatherStation station = new WeatherStation();

        Observer phone = new PhoneDisplay();
        Observer web   = new WebDisplay();

        station.subscribe(phone);
        station.subscribe(web);

        System.out.println("-- Temperature set to 28 --");
        station.setTemperature(28);    // both displays react

        System.out.println("\n-- Web unsubscribes, temp set to 31 --");
        station.unsubscribe(web);
        station.setTemperature(31);    // only phone reacts

        System.out.println("\n-- TV subscribes, temp set to 19 --");
        station.subscribe(new TVDisplay());
        station.setTemperature(19);    // phone + TV react (OCP: no Subject change needed)
    }
}

// Observers implement this; the Subject calls update() on each.
interface Observer {
    void update(int temperature);
}

// The Subject (publisher).
class WeatherStation {
    private final List<Observer> observers = new CopyOnWriteArrayList<>();
    private int temperature;

    public void subscribe(Observer o)   { observers.add(o); }
    public void unsubscribe(Observer o) { observers.remove(o); }

    public void setTemperature(int t) {
        this.temperature = t;
        notifyObservers();
    }

    private void notifyObservers() {
        for (Observer o : observers) {
            o.update(temperature);
        }
    }
}

// Concrete observers — each reacts in its own way.
class PhoneDisplay implements Observer {
    public void update(int t) { System.out.println("Phone display: " + t + " C"); }
}
class WebDisplay implements Observer {
    public void update(int t) { System.out.println("Web display: " + t + " C"); }
}
class TVDisplay implements Observer {
    public void update(int t) { System.out.println("TV display: " + t + " C"); }
}
