package com.company.factorymethod;

/**
 * The PRODUCT (GoF role). The interface every concrete transport implements.
 * Client code (Logistics.planDelivery) only ever sees this type.
 */
public interface Transport {
    /** Deliver the cargo. Returns a tracking id. */
    String deliver();
}
