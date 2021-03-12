package mary.uspet;


import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmResults;

public class Manager {

    private static Manager manager;

    private Realm realm;


    public static Manager getManager() {
        if (manager == null) {
            manager = new Manager();
        }
        return manager;
    }

    public void initRealm(Context context) {
        Realm.init(context);
    }

    public Realm openRealm() {
        if (realm == null) {
            realm = Realm.getDefaultInstance();
        }
        return realm;
    }

    public void saveShownCard(String cardId, boolean isDone) {
        RealmShownCard realmShownCard = new RealmShownCard();
        realmShownCard.setCardId(cardId);
        realmShownCard.setDone(isDone);
        Realm realm = openRealm();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(realmShownCard);
        realm.commitTransaction();
    }

    public RealmResults<RealmShownCard> loadShownCards() {
        Realm realm = openRealm();
        return realm.where(RealmShownCard.class).findAll();
    }

    public ArrayList<String> loadShownCardIds() {
        RealmResults<RealmShownCard> shownCards = loadShownCards();
        ArrayList<String> cardIds = new ArrayList<>();
        for (RealmShownCard realmShownCard : shownCards) {
            cardIds.add(realmShownCard.getCardId());
        }
        return cardIds;
    }

    public ArrayList<String> loadDoneCardIds() {
        RealmResults<RealmShownCard> shownCards = loadShownCards();
        ArrayList<String> cardIds = new ArrayList<>();
        for (RealmShownCard realmShownCard : shownCards) {
            if (realmShownCard.isDone()) {
                cardIds.add(realmShownCard.getCardId());
            }
        }
        return cardIds;
    }

    public boolean isAchivkaCompleted(String[] cardIdsForAchivka) {
        ArrayList<String> doneCardIds = loadDoneCardIds();
        boolean contains = true;
        for (String cardId : cardIdsForAchivka) {
            if (!doneCardIds.contains(cardId)) {
                contains = false;
            }
        }
        return contains;
    }

    public void saveCompletedAchivka(String achivkaId, boolean isShown) {
        RealmCompletedAchivka realmCompletedAchivka = new RealmCompletedAchivka();
        realmCompletedAchivka.setAchivkaId(achivkaId);
        realmCompletedAchivka.setShown(isShown);
        Realm realm = openRealm();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(realmCompletedAchivka);
        realm.commitTransaction();
    }

    public void setAllCompletedAchivkiShown() {
        RealmResults<RealmCompletedAchivka> completedAchivki = loadCompletedAchivki();
        Realm realm = openRealm();
        realm.beginTransaction();
        for (RealmCompletedAchivka realmCompletedAchivka : completedAchivki) {
            realmCompletedAchivka.setShown(true);
            realm.copyToRealmOrUpdate(realmCompletedAchivka);
        }
        realm.commitTransaction();
    }

    public RealmResults<RealmCompletedAchivka> loadCompletedAchivki() {
        Realm realm = openRealm();
        return realm.where(RealmCompletedAchivka.class).findAll();
    }

    public ArrayList<String> loadCompletedAchivkiIds() {
        RealmResults<RealmCompletedAchivka> completedAchivki = loadCompletedAchivki();
        ArrayList<String> achivkaIds = new ArrayList<>();
        for (RealmCompletedAchivka realmCompletedAchivka : completedAchivki) {
            achivkaIds.add(realmCompletedAchivka.getAchivkaId());
        }
        return achivkaIds;
    }

    public ArrayList<String> loadCompletedAndShownAchivkaIds() {
        RealmResults<RealmCompletedAchivka> completedAchivki = loadCompletedAchivki();
        ArrayList<String> achivkaIds = new ArrayList<>();
        for (RealmCompletedAchivka realmCompletedAchivka : completedAchivki) {
            if (realmCompletedAchivka.isShown()) {
                achivkaIds.add(realmCompletedAchivka.getAchivkaId());
            }
        }
        return achivkaIds;
    }

    public void removeAllFromRealm() {
        Realm realm = openRealm();
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
    }

    public int countNotShownCompletedAchivki() {
        return loadCompletedAchivkiIds().size() - loadCompletedAndShownAchivkaIds().size();
    }

    public boolean containsId(final String[] array, final String v) {

        boolean result = false;

        for (String i : array) {
            if (i.equals(v)) {
                result = true;
                break;
            }
        }
        return result;
    }

    public String getKeyToValue(HashMap hashMap, String[] someValue) { //возвращает ключ по значению

        String key = null;
        HashMap<String, String[]> map = hashMap;
        Set<Map.Entry<String, String[]>> entrySet = map.entrySet();

        for (Map.Entry<String, String[]> pair : entrySet) {
            if (someValue.equals(pair.getValue())) {
                key = pair.getKey();
            }
        }
        return key;
    }
}
