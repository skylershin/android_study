package com.example.munkyushin.okhttpexample.dto;

/**
 * Created by MunkyuShin on 3/12/16.
 */
public class Repository {
    private String name;

    @Override
    public String toString() {
        return "name: " + name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
