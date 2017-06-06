package org.hackinghealth.cheesecloth.dao;

import io.realm.RealmObject;

/**
 * Created by miantorno on 6/5/17.
 */

public class CategoryAssociation extends RealmObject {

    public Category category;
    public Double weight = 1.0;

    public CategoryAssociation() {
    }

    public CategoryAssociation(Category category, Double weight) {
        this.category = category;
        this.weight = weight;
    }

    public Category getCategory() {
        return category;
    }

    public CategoryAssociation setCategory(Category category) {
        this.category = category;
        return this;
    }

    public Double getWeight() {
        return weight;
    }

    public CategoryAssociation setWeight(Double weight) {
        this.weight = weight;
        return this;
    }

    @Override
    public String toString() {
        return "CategoryAssociation{" +
                "category=" + category +
                ", weight=" + weight +
                '}';
    }
}
