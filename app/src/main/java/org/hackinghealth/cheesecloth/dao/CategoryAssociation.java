package org.hackinghealth.cheesecloth.dao;

import io.realm.RealmObject;

/**
 * Created by miantorno on 6/5/17.
 */

public class CategoryAssociation extends RealmObject {

    public Category category;
    public String cat_name;
    public Float weight = 1.0f;

    public CategoryAssociation() {
    }

    public CategoryAssociation(Category category, Float weight) {
        this.category = category;
        this.cat_name = category.name;
        this.weight = weight;
    }

    public Category getCategory() {
        return category;
    }

    public CategoryAssociation setCategory(Category category) {
        this.category = category;
        this.cat_name = category.name;
        return this;
    }

    public Float getWeight() {
        return weight;
    }

    public CategoryAssociation setWeight(Float weight) {
        this.weight = weight;
        return this;
    }

    public String getCat_name() {
        return cat_name;
    }

    public CategoryAssociation setCat_name(String cat_name) {
        this.cat_name = cat_name;
        return this;
    }

    @Override
    public String toString() {
        return "CategoryAssociation{" +
                "category=" + category +
                ", cat_name='" + cat_name + '\'' +
                ", weight=" + weight +
                '}';
    }
}
