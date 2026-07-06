package com.company.patterns.templatemethod;

/**
 * TEMPLATE METHOD — base class fixes the algorithm's SKELETON (final method);
 * subclasses fill in the varying steps. Includes a HOOK method.
 * Matches notes/03's Beverage example.
 *
 * "The framework calls your code at the right time" = usually this pattern:
 * JdbcTemplate, AbstractList, HttpServlet.service() -> doGet/doPost, JUnit
 * lifecycle.
 */
public class TemplateMethodDemo {
    public static void main(String[] args) {
        System.out.println("--- tea ---");
        new Tea().prepare();

        System.out.println();
        System.out.println("--- coffee (customer declines condiments via hook) ---");
        new Coffee(false).prepare();
    }
}

abstract class Beverage {

    /** The TEMPLATE METHOD: final so subclasses cannot reorder the recipe. */
    public final void prepare() {
        boilWater();                       // shared, concrete
        brew();                            // varies -> abstract
        pourInCup();                       // shared
        if (customerWantsCondiments()) {   // HOOK: optional override
            addCondiments();               // varies -> abstract
        }
    }

    private void boilWater() { System.out.println("boiling water"); }
    private void pourInCup() { System.out.println("pouring into cup"); }

    protected abstract void brew();
    protected abstract void addCondiments();

    /** HOOK — concrete default; subclasses MAY override. */
    protected boolean customerWantsCondiments() { return true; }
}

class Tea extends Beverage {
    @Override protected void brew()          { System.out.println("steeping tea bag"); }
    @Override protected void addCondiments() { System.out.println("adding lemon"); }
}

class Coffee extends Beverage {
    private final boolean withCondiments;

    Coffee(boolean withCondiments) { this.withCondiments = withCondiments; }

    @Override protected void brew()          { System.out.println("dripping coffee through filter"); }
    @Override protected void addCondiments() { System.out.println("adding milk and sugar"); }
    @Override protected boolean customerWantsCondiments() { return withCondiments; }
}
