package mary.uspet;


import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

public class Utils {

    public static void setFontYarin(Context context, TextView... textViews) {
        for (TextView textView : textViews) {
            textView.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/yarin.otf"));
        }
    }

    public static void setFontNoteworthyBold(Context context, TextView... textViews) {
        for (TextView textView : textViews) {
            textView.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/noteworthybold.otf"));
        }
    }

    public static void animateFadeIn(View view, long duration) {
        view.setAlpha(0.0f);
        view.animate()
                .setDuration(duration)
                .alpha(1.0f);
        if (view.getVisibility() == View.INVISIBLE ||
                view.getVisibility() == View.GONE) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void animateFadeOut(final View view, long duration) {
        view.setAlpha(1.0f);
        view.animate()
                .setDuration(1200)
                .alpha(0.0f)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        if (view.getVisibility() == View.VISIBLE) {
                            view.setVisibility(View.INVISIBLE);
                        }

                    }
                });
    }

    public static void animateScaleCloser(View view, long duration) {
        float x = view.getScaleX();
        float y = view.getScaleY();
        view.animate()
                .scaleX(x + 0.1f)
                .scaleY(y + 0.1f)
                .setDuration(duration);
    }

}
