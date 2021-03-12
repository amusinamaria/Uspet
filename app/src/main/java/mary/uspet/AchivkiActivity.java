package mary.uspet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;

import static mary.uspet.Utils.setFontYarin;

public class AchivkiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_achivki);

        setFontYarin(this, (TextView) findViewById(R.id.noAchivki));

        Manager manager = Manager.getManager();

        manager.setAllCompletedAchivkiShown();
        RecyclerView achivkiRecyclerView = findViewById(R.id.achivkiRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        achivkiRecyclerView.setLayoutManager(linearLayoutManager);

        ArrayList<String> completedAchivkiIds = manager.loadCompletedAchivkiIds();
        ArrayList<Achivka> achivki = new ArrayList<>();
        for (String completedAchivkaId : completedAchivkiIds) {
            Achivka achivka = new Achivka();
            int titleResId = getResources().getIdentifier(completedAchivkaId, "string", getPackageName());
            achivka.title = getResources().getString(titleResId);
            achivka.imageId = getResources().getIdentifier("mary.uspet:drawable/" + completedAchivkaId, null, null);
            achivka.doneTextArrayId = getResources().getIdentifier(completedAchivkaId + "done", "array", this.getPackageName());
            achivki.add(achivka);
        }
        achivkiRecyclerView.setAdapter(new AchivkiAdapter(achivki, this));
        findViewById(R.id.noAchivki).setVisibility(achivki.size() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onBackPressed() {
        onClickBackToCards(new View(this));
    }

    public void onClickBackToCards(View v) {
        Intent intent = new Intent(AchivkiActivity.this, CardsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.up_transition_back, R.anim.down_transition_back);
    }
}