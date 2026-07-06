package com.company.patterns.facade;

/**
 * FACADE — ONE simple entry point in front of a messy subsystem.
 *
 * The subsystem classes still exist and are still usable directly (facade
 * doesn't forbid anything — it just offers the common path as one call).
 *
 * Difference vs Adapter: Adapter CHANGES an interface to fit what the client
 * expects; Facade SIMPLIFIES many interfaces into one. Adapter = fit,
 * Facade = shrink-wrap.
 */
public class FacadeDemo {
    public static void main(String[] args) {
        HomeTheaterFacade theater = new HomeTheaterFacade(
                new Amplifier(), new Projector(), new Lights());

        theater.watchMovie("Interstellar");   // ONE call instead of six
        System.out.println();
        theater.endMovie();
    }
}

/* --- The subsystem: each class is fine alone, but using them together
       requires knowing the right order and settings. --- */

class Amplifier {
    void on()               { System.out.println("Amp: on"); }
    void off()              { System.out.println("Amp: off"); }
    void setVolume(int v)   { System.out.println("Amp: volume " + v); }
}

class Projector {
    void on()               { System.out.println("Projector: on"); }
    void off()              { System.out.println("Projector: off"); }
    void wideScreenMode()   { System.out.println("Projector: 16:9 widescreen"); }
}

class Lights {
    void dim(int percent)   { System.out.println("Lights: dim to " + percent + "%"); }
    void full()             { System.out.println("Lights: full brightness"); }
}

/** The FACADE. Knows the choreography so the client doesn't have to. */
class HomeTheaterFacade {
    private final Amplifier amp;
    private final Projector projector;
    private final Lights lights;

    HomeTheaterFacade(Amplifier amp, Projector projector, Lights lights) {
        this.amp = amp;
        this.projector = projector;
        this.lights = lights;
    }

    void watchMovie(String movie) {
        System.out.println("== starting: " + movie + " ==");
        lights.dim(10);
        projector.on();
        projector.wideScreenMode();
        amp.on();
        amp.setVolume(7);
        System.out.println("== enjoy the movie ==");
    }

    void endMovie() {
        System.out.println("== shutting down ==");
        amp.off();
        projector.off();
        lights.full();
    }
}
