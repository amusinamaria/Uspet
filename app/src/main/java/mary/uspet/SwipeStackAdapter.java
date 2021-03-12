package mary.uspet;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static mary.uspet.Utils.setFontNoteworthyBold;

public class SwipeStackAdapter extends BaseAdapter {

    private ArrayList<Card> cards;

    public SwipeStackAdapter(ArrayList<Card> data) {
        cards = data;
    }

    @Override
    public int getCount() {
        return cards.size();
    }

    @Override
    public Object getItem(int position) {
        return cards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override

    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_item, viewGroup, false);
            holder.textViewCard = view.findViewById(R.id.cardText);
            holder.imageViewCard = view.findViewById(R.id.cardImage);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Card card = cards.get(position);
        if (card != null) {
            holder.textViewCard.setText(card.text);
            Glide.with(viewGroup.getContext())
                    .load(cards.get(position).imageId)
                    .into(holder.imageViewCard);
            setFontNoteworthyBold(viewGroup.getContext(), holder.textViewCard);
        } else {
            holder.textViewCard.setText("");
        }
        return view;

    }

    private class ViewHolder {

        TextView textViewCard;
        ImageView imageViewCard;

    }
}