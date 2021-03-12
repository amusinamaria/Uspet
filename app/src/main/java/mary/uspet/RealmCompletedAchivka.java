package mary.uspet;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmCompletedAchivka extends RealmObject {

    @PrimaryKey
    private String achivkaId;
    private boolean isShown;

    public RealmCompletedAchivka() {
        isShown = false;
    }

    public String getAchivkaId() {
        return achivkaId;
    }

    public void setAchivkaId(String achivkaId) {
        this.achivkaId = achivkaId;
    }

    public boolean isShown() {
        return isShown;
    }

    public void setShown(boolean shown) {
        isShown = shown;
    }
}
