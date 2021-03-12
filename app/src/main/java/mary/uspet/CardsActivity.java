package mary.uspet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import link.fls.swipestack.SwipeStack;

import static mary.uspet.MainActivity.SHARED_PREFERENCES_FILE;
import static mary.uspet.Utils.animateFadeIn;
import static mary.uspet.Utils.setFontYarin;

public class CardsActivity extends AppCompatActivity {

    public static final String IS_REPLAY_VIEW_VISIBLE_KEY = "isReplayViewVisible";
    private static final int TIME_INTERVAL_BETWEEN_BACK_PRESS = 2000;
    public HashMap<String, String[]> achivkiHashMap;
    private SharedPreferences sharedPreferences;
    private Manager manager;
    private Toast exitToast;
    private long mBackPressed;
    private Animation rotation;

    private ImageView directionSign;
    private ImageView replaySign;
    private TextView noMoreCardsText;
    private TextView replayText;
    private SwipeStack swipeStack;
    private RelativeLayout counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_cards);

        manager = Manager.getManager();
        manager.initRealm(this);

        directionSign = findViewById(R.id.directionSign);
        replaySign = findViewById(R.id.replaySign);
        noMoreCardsText = findViewById(R.id.noMoreCardsText);
        replayText = findViewById(R.id.replayText);
        swipeStack = findViewById(R.id.swipeStack);
        counter = findViewById(R.id.counter);

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        boolean isReplayViewVisible = sharedPreferences.getBoolean(IS_REPLAY_VIEW_VISIBLE_KEY, false);
        showHideReplayView(isReplayViewVisible);

        setFontYarin(this, noMoreCardsText, replayText);

        updateCounterIfNeeded();

        ImageView notYetButton = findViewById(R.id.notYetButton);
        notYetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeStack.swipeTopViewToLeft();
            }
        });

        ImageView doneButton = findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeStack.swipeTopViewToRight();
            }
        });

        replaySign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rotation.cancel();
                resetProgressAndRestart();
            }
        });

        final ArrayList<Card> cards = new ArrayList<>();
        String[] cardsArray = getResources().getStringArray(R.array.cardsArray);
        ArrayList<String> shownCardIds = manager.loadShownCardIds();

        for (int i = 0; i < cardsArray.length; i = i + 2) {
            if (shownCardIds.contains(cardsArray[i + 1])) continue;
            Card card = new Card();
            card.id = cardsArray[i + 1];
            card.text = cardsArray[i];
            card.imageId = getResources().getIdentifier(card.id, "drawable", getPackageName());
            cards.add(card);
        }
        swipeStack.setAdapter(new SwipeStackAdapter(cards));

        achivkiHashMap = new HashMap<>();
        String[] achivkiArray = getResources().getStringArray(R.array.achivkiArray);
        for (int i = 1; i < achivkiArray.length; i = i + 2) {
            int arrayId = this.getResources().getIdentifier(achivkiArray[i], "array", this.getPackageName());
            achivkiHashMap.put(achivkiArray[i], getResources().getStringArray(arrayId));
        }

        //Disable stack swiping
        findViewById(R.id.swipeStackOverlay).setOnClickListener(null);
        swipeStack.setListener(new SwipeStack.SwipeStackListener() {
            @Override
            public void onViewSwipedToLeft(int position) {
                Card swipedCard = cards.get(position);
                manager.saveShownCard(swipedCard.id, false);
            }

            @Override
            public void onViewSwipedToRight(int position) {
                Card swipedCard = cards.get(position);
                manager.saveShownCard(swipedCard.id, true);
                updateAchivki(swipedCard.id);
                updateCounterIfNeeded();
            }

            @Override
            public void onStackEmpty() {
                sharedPreferences.edit()
                        .putBoolean(IS_REPLAY_VIEW_VISIBLE_KEY, true)
                        .apply();
                animateShowReplayView();
            }
        });
    }

    public void onClickAchivkiIcon(View v) {
        Intent intent = new Intent(CardsActivity.this, AchivkiActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.down_transition, R.anim.up_transition);
    }

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL_BETWEEN_BACK_PRESS > System.currentTimeMillis()) {
            exitToast.cancel();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("ExitApp", true);
            startActivity(intent);
            finish();
        } else {
            exitToast = Toast.makeText(getBaseContext(), "Click BACK again to exit", Toast.LENGTH_SHORT);
            exitToast.show();
        }
        mBackPressed = System.currentTimeMillis();
    }

    private void updateCounterIfNeeded() {
        int count = manager.countNotShownCompletedAchivki();
        if (count < 1) {
            hideCounter();
        } else {
            showCounter(count);
        }
    }

    private void hideCounter() {
        counter.setVisibility(View.INVISIBLE);
    }

    private void showCounter(int count) {
        TextView countTextView = findViewById(R.id.counterTextView);
        counter.setVisibility(View.VISIBLE);
        if (!countTextView.getText().equals(String.valueOf(count))) {
            findViewById(R.id.achivkiIconButton).startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake_anim));
        }
        countTextView.setText(String.valueOf(count));
    }

    private void updateAchivki(String doneCardId) {
        for (String[] cardIdsForAchivka : achivkiHashMap.values()) {
            if (manager.containsId(cardIdsForAchivka, doneCardId)) {  // id новой карточки содержится в массиве для ачивки
                if (manager.isAchivkaCompleted(cardIdsForAchivka)) {  // все карточки массива ачивки отмечены - ачивка получена
                    String achivkaId = manager.getKeyToValue(achivkiHashMap, cardIdsForAchivka);  // находим название ачивки
                    manager.saveCompletedAchivka(achivkaId, false);
                }
            }
        }
    }

    private void resetProgressAndRestart() {
        sharedPreferences.edit().remove(IS_REPLAY_VIEW_VISIBLE_KEY).apply();
        manager.removeAllFromRealm();
        finish();
        Intent intent = new Intent(CardsActivity.this, CardsActivity.this.getClass());
        startActivity(intent);
    }

    private void showHideReplayView(boolean shouldShowReplay) {
        directionSign.setVisibility(shouldShowReplay ? View.GONE : View.VISIBLE);
        noMoreCardsText.setVisibility(shouldShowReplay ? View.VISIBLE : View.GONE);
        replayText.setVisibility(shouldShowReplay ? View.VISIBLE : View.GONE);
        replaySign.setVisibility(shouldShowReplay ? View.VISIBLE : View.GONE);
        if (shouldShowReplay) {
            animateReplaySign(0);
        } else {
            animateFadeIn(swipeStack, 500);
        }
    }

    private void animateShowReplayView() {
        directionSign.setVisibility(View.GONE);
        swipeStack.setVisibility(View.GONE);
        animateFadeIn(noMoreCardsText, 700);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animateFadeIn(replayText, 700);
                animateReplaySign(700);
            }
        }, 700);
    }

    private void animateReplaySign(long delay) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animateFadeIn(replaySign, 200);
                rotation = AnimationUtils.loadAnimation(CardsActivity.this, R.anim.rotate);
                rotation.setFillAfter(true);
                replaySign.startAnimation(rotation);
            }
        }, delay);
    }
}