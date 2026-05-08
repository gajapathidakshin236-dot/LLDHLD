package com.company.prototype;

import java.util.HashMap;
import java.util.Map;

public final class PrototypeRegistry {
    private final Map<String, Shape> prototypes = new HashMap<>();

    public void addItem(String id, Shape prototype) {
        prototypes.put(id, prototype);
    }

    public Shape getById(String id) {
        Shape prototype = prototypes.get(id);
        return prototype == null ? null : prototype.copy();
    }

    public Shape getByColor(String color) {
        for (Shape prototype : prototypes.values()) {
            if (color != null && color.equalsIgnoreCase(prototype.getColor())) {
                return prototype.copy();
            }
        }
        return null;
    }
}

