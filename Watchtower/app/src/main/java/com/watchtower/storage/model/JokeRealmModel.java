package com.watchtower.storage.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

// A Simple Realm Db Object Model
public class JokeRealmModel extends RealmObject {

    public static final String PRIMARY_KEY = "key";
    public static final String DEFAULT_PRIMARY_KEY_VALUE = "default";

    @PrimaryKey
    private String key;

    @Required
    private String message;

    public String getKey() {
        return key;
    }

    public void setKey(String uuid) {
        this.key = uuid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
