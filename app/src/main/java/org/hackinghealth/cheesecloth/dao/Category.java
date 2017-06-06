package org.hackinghealth.cheesecloth.dao;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by miantorno on 6/5/17.
 */

public class Category extends RealmObject {

    public String name;
    public RealmList<Tag> tags;
    public RealmList<Tag> senders;

    public Category() {

    }

    public Category(String name, ArrayList<Tag> tags, ArrayList<Tag> senders) {
        this.name = name;
        setTags(tags);
        setSenders(senders);
    }

    public String getName() {
        return name;
    }

    public Category setName(String name) {
        this.name = name;
        return this;
    }

    public RealmList<Tag> getTags() {
        return tags;
    }

    public Category setTags(ArrayList<Tag> tags) {
        Realm realm = CheeseClothDatabaseHelper.getRealm();
        RealmList<Tag> list1 = new RealmList();
        list1.addAll(tags);
        realm.beginTransaction();
        realm.copyToRealm(list1); // This will do a deep copy of everything
        realm.commitTransaction();
        this.tags = list1;
        return this;
    }

    public RealmList<Tag> getSenders() {
        return senders;
    }

    public Category setSenders(ArrayList<Tag> senders) {
        Realm realm = CheeseClothDatabaseHelper.getRealm();
        RealmList<Tag> list1 = new RealmList();
        list1.addAll(senders);
        realm.beginTransaction();
        realm.copyToRealm(list1); // This will do a deep copy of everything
        realm.commitTransaction();
        this.senders = list1;
        return this;
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", tags=" + tags +
                ", senders=" + senders +
                '}';
    }
}
