package org.hackinghealth.cheesecloth.dao;

import io.realm.annotations.RealmModule;

/**
 * Created by miantorno on 6/5/17.
 */
@RealmModule(classes = {Sender.class, Message.class, Tag.class, Category.class, CategoryAssociation.class})
public class SimpleRealmModule {}