package mary.uspet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import static mary.uspet.Utils.animateFadeIn;
import static mary.uspet.Utils.animateFadeOut;
import static mary.uspet.Utils.animateScaleCloser;
import static mary.uspet.Utils.setFontYarin;

public class MainActivity extends AppCompatActivity {

    public static final String SHARED_PREFERENCES_FILE = "Settings.data";
    public static final String INTRO_HAS_BEEN_SHOWN_KEY = "introHasBeenShown";

    SharedPreferences sharedPreferences;
    ImageView backgroundImage;
    TextView introText1;
    TextView introText2;
    TextView introText3;
    TextView introText4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getIntent().getBooleanExtra("ExitApp", false)) {
            finish();
            return;
        }

        backgroundImage = findViewById(R.id.backgroundRoadMain);
        introText1 = findViewById(R.id.intro1);
        introText2 = findViewById(R.id.intro2);
        introText3 = findViewById(R.id.intro3);
        introText4 = findViewById(R.id.intro4);

        showIntroIfNotYetShown();

        setFontYarin(this, introText1, introText2, introText3, introText4);
    }

    private void showIntroIfNotYetShown() {
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        boolean introHasBeenShown = sharedPreferences.getBoolean(INTRO_HAS_BEEN_SHOWN_KEY, false);

        if (introHasBeenShown) {
            startGame();
        } else {
            sharedPreferences.edit()
                    .putBoolean(INTRO_HAS_BEEN_SHOWN_KEY, true)
                    .apply();
            showIntroText1();
        }
    }

    private void showIntroText1() {
        findViewById(R.id.backgroundRoadMain).setOnClickListener(null);
        animateScaleCloser(backgroundImage, 10500);
        animateFadeIn(introText1, 2000);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showIntroText2();
            }
        }, 3000);
    }

    private void showIntroText2() {
        animateFadeOut(introText1, 1500);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showIntroText3();
            }
        }, 3000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animateFadeIn(introText2, 2000);
            }
        }, 2000);
    }

    private void showIntroText3() {
        animateFadeIn(introText3, 1700);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showIntroText4();
            }
        }, 1700);
    }

    private void showIntroText4() {
        animateFadeOut(introText2, 1500);
        animateFadeOut(introText3, 1500);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animateFadeIn(introText4, 1000);
            }
        }, 1500);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideIntro();
            }
        }, 3000);
    }

    private void hideIntro() {
        animateFadeOut(introText4, 1000);
        animateFadeOut(backgroundImage, 1500);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.backgroundRoadMain).setScaleX(1.0f);
                findViewById(R.id.backgroundRoadMain).setScaleY(1.0f);
                startGame();
            }
        }, 500);
    }

    private void startGame() {
        Intent intent = new Intent(MainActivity.this, CardsActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
