package mary.uspet;


public class Card {

    //for Statistics
//    public static final String GOOD = "good";
//    public static final String BAD = "bad";
//    public static final String STRANGE = "strange";

    String id;
    String text;
    //    String type;
    int imageId;
    boolean isShown;
    boolean isDone;

    public Card() {
        isShown = false;
        isDone = false;
    }
}