package com.company.builder;

public class Main {
    public static void main(String[] args) {
        Director director = new Director();

        CarBuilder carBuilder = new CarBuilder();
        director.constructSportsCar(carBuilder);
        Car car = carBuilder.getResult();
        car.setFuel(55);

        System.out.println("Built: " + car.getType());
        System.out.println(car.getTripComputer().showFuelLevel());

        CarManualBuilder manualBuilder = new CarManualBuilder();
        director.constructSportsCar(manualBuilder);
        Manual manual = manualBuilder.getResult();

        System.out.println();
        System.out.println(manual.print());
    }
}

