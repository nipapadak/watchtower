package com.watchtower.storage.model;

//API Model that Maps the JSON Response to a Java Object
public class JokeApiModel {
    public JokeValue value;
    public static class JokeValue {
        public String joke;
    }
}
