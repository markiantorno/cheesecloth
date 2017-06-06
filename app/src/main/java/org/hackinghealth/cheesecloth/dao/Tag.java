package org.hackinghealth.cheesecloth.dao;

import io.realm.RealmObject;

/**
 * Created by miantorno on 6/5/17.
 */

public class Tag extends RealmObject {

    public String text;
    public Double weight;

    public Tag() {

    }

    public Tag(String text, Double weight) {
        this.text = text;
        this.weight = weight;
    }

    public String getText() {
        return text;
    }

    public Tag setText(String text) {
        this.text = text;
        return this;
    }

    public Double getWeight() {
        return weight;
    }

    public Tag setWeight(Double weight) {
        this.weight = weight;
        return this;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "text='" + text + '\'' +
                ", weight=" + weight +
                '}';
    }
}
