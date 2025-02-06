package com.tenshiku.guppyoffer.model;

import org.bukkit.Material;

public class Offering {
    private final Material material;

    public Offering(Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }
}
