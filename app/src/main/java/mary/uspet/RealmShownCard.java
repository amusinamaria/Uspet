package mary.uspet;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmShownCard extends RealmObject {

    @PrimaryKey
    private String cardId;
    private boolean isDone;

    public RealmShownCard() {
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

}
