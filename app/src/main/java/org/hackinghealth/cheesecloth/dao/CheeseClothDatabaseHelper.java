package org.hackinghealth.cheesecloth.dao;

import android.util.Log;

import org.hackinghealth.cheesecloth.CheeseClothApplication;

import io.realm.Realm;

/**
 * Created by miantorno on 6/5/17.
 */

public class CheeseClothDatabaseHelper {

    public static Realm getRealm() {

        Realm realm = Realm.getInstance(CheeseClothApplication.getRealmConfiguration());
        return realm;

    }

    public static void writeMessageToDB(final Message msg) {
        if (msg != null) {
            Realm localRealm = getRealm();
            localRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Log.d("WRITE", "Written to DB  " + msg.toString());
                    realm.copyToRealm(msg);
                }
            });
        }
    }

    public static void writeSenderToDb(final Sender sdr) {
        if (sdr != null) {
            Realm localRealm = getRealm();
            localRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Log.d("WRITE", "Written to DB  " + sdr.toString());
                    realm.copyToRealm(sdr);
                }
            });
        }
    }

    public static void writeCategoryAssociationToDb(final CategoryAssociation cat_ass) { //lol
        if (cat_ass != null) {
            Realm localRealm = getRealm();
            localRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Log.d("WRITE", "Written to DB  " + cat_ass.toString());
                    realm.copyToRealm(cat_ass);
                }
            });
        }
    }
}
