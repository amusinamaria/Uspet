package mary.uspet;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static mary.uspet.Utils.setFontNoteworthyBold;
import static mary.uspet.Utils.setFontYarin;

public class AchivkiAdapter extends RecyclerView.Adapter<AchivkiAdapter.ViewHolder> {

    private AchivkiActivity achivkiActivity;
    private ArrayList<Achivka> achivki;

    public AchivkiAdapter(ArrayList<Achivka> dataset, AchivkiActivity activity) {
        achivki = dataset;
        achivkiActivity = activity;
    }

    @Override
    public AchivkiAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.achivka_item, parent, false);
        return new AchivkiAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final AchivkiAdapter.ViewHolder holder, int position) {
        final Achivka achivka = achivki.get(position);

        Glide.with(holder.image.getContext())
                .load(achivka.imageId)
                .into(holder.image);

        holder.title.setText(achivka.title);
        setFontYarin(achivkiActivity, holder.title);
        //hardcode
        String[] doneTextArray = achivkiActivity.getResources().getStringArray(achivka.doneTextArrayId);
        holder.doneText1.setText(doneTextArray[0]);
        holder.doneText2.setText(doneTextArray[1]);
        setFontNoteworthyBold(achivkiActivity, holder.doneText1, holder.doneText2);

//        final DatabaseManager databaseManager = DatabaseManager.getManager();
//        ArrayList<Integer> checkedCardIds = databaseManager.loadCheckedCardIds();

//        if (databaseManager.isAchievkaCompleted(checkedCardIds, cardItemAchivka.cardIds)) {
//        holder.colorImage.setVisibility(View.VISIBLE);
//        }


//        ArrayList<Integer> checkedCardIds = databaseManager.loadCheckedCardIds();
//        if(checkedCardIds.contains(cardItem.cardId)){
//            holder.cardImageDone.setVisibility(View.VISIBLE);
//            holder.cardTickDone.setVisibility((View.VISIBLE));
//        }
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //User touch achivka:

//                String achievkaName = cardsActivity.whichAchievkaCompleted(cardItem.cardId);
//                if (!achievkaName.equals("")){
//                    //Add conditions for achievements
//                mainActivity.achievementView.setVisibility(View.VISIBLE);
////                }
//            }
//        });
//
//        holder.cardTickDone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // User touch yellow tick:
//                holder.cardImageDone.setVisibility(View.INVISIBLE);
//                holder.cardTickDone.setVisibility((View.INVISIBLE));
//                cardItem.isChecked = false;
//                databaseManager.removeCheckedCard(cardItem.cardId);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return achivki.size();
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title;
        TextView doneText1;
        TextView doneText2;
        RelativeLayout achivkaLayout;


        ViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.achImage);
            title = v.findViewById(R.id.achTitle);
            doneText1 = v.findViewById(R.id.doneText1);
            doneText2 = v.findViewById(R.id.doneText2);
            achivkaLayout = v.findViewById(R.id.achivkaLayout);
        }
    }

}
