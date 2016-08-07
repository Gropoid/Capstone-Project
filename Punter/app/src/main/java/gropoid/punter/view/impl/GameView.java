package gropoid.punter.view.impl;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import gropoid.punter.R;
import gropoid.punter.domain.Game;


public class GameView extends CardView {
    @BindView(R.id.background)
    ImageView background;
    @BindView(R.id.scrim)
    ImageView scrim;
    @BindView(R.id.game_title)
    TextView gameTitle;

    public GameView(Context context) {
        super(context);
        inflate(context);
    }

    private void inflate(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.game_view, this);
        ButterKnife.bind(this);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void bind(Game game) {
        gameTitle.setText(game.getName());
        Glide.with(getContext()).load(game.getImageFile())
                .placeholder(R.drawable.static_tv)
                .into(background);
    }
}
