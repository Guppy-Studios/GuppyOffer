package com.tenshiku.guppyoffer.model;

public class Reward {
    private final String id;
    private final String name;
    private final String command;
    private final int chance;

    public Reward(String id, String name, String command, int chance) {
        this.id = id;
        this.name = name;
        this.command = command;
        this.chance = chance;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCommand() {
        return command;
    }

    public int getChance() {
        return chance;
    }
}