package org.hackinghealth.cheesecloth;

import android.app.Application;

import org.hackinghealth.cheesecloth.algo.Classifier;
import org.hackinghealth.cheesecloth.algo.ClassifierKt;
import org.hackinghealth.cheesecloth.dao.SimpleRealmModule;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by miantorno on 6/5/17.
 */

public class CheeseClothApplication extends Application {

    private static CheeseClothApplication instance;
    private static RealmConfiguration sConfig = null;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Realm.init(getApplicationContext());
        Realm.setDefaultConfiguration(getRealmConfiguration());
        ClassifierKt.populate();
    }
    public static CheeseClothApplication getInstance() {
        return instance;
    }

    public static RealmConfiguration getRealmConfiguration() {
        if (sConfig == null) {
            sConfig = new RealmConfiguration.Builder()
                    .modules(new SimpleRealmModule())
                    .name("cheesecloth.realm")
                    .build();
        }
        return sConfig;
    }
}
