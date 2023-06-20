package com.example.petfriend;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petfriend.cards.Animal;
import com.example.petfriend.cards.TinderCard;
import com.example.petfriend.R;
import com.example.petfriend.api.APIConnector;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import java.util.List;

public class CardsActivity extends AppCompatActivity {

    String TAG = "CardsActivity";

    private SwipePlaceHolderView mSwipeView;
    private Context mContext;
    private static List<Animal> animalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        // reset the index tracker for favorites to 0
        TinderCard.setIndex(0);

        // reset user count values to update db
        User.counter = 0;
        User.likedCounter = 0;

        // initialize view and context
        mSwipeView = (SwipePlaceHolderView)findViewById(R.id.swipeView);
        mContext = getApplicationContext();

        // get screen size
        int bottomMargin = Utils.dpToPx(160);
        Point windowSize = Utils.getDisplaySize(getWindowManager());

        // add back button
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8000ff")));
        actionBar.setDisplayHomeAsUpEnabled(true);

        // adjust the swipeview to fit the size of window and other configurations
        mSwipeView.getBuilder()
                .setDisplayViewCount(5)
                .setIsUndoEnabled(true)
                .setHeightSwipeDistFactor(10)
                .setWidthSwipeDistFactor(5)
                .setSwipeDecor(new SwipeDecor()
                        .setViewWidth(windowSize.x)
                        .setViewHeight(windowSize.y - bottomMargin)
                        .setViewGravity(Gravity.TOP)
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeMaxChangeAngle(2f)
                        .setSwipeInMsgLayoutId(R.layout.tinder_swipe_in_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.tinder_swipe_out_msg_view));

        animalList = APIConnector.getAnimalList();
        Log.e("ERROR", animalList.toString());
        // for each dog in the list add the card to the view
        for(Animal animal : animalList) {
            mSwipeView.addView(new TinderCard(mContext, animal, mSwipeView));
        }
        Log.d(TAG, "Animals added to swipeview");

        // listeners for accept, reject, and undo buttons
        findViewById(R.id.rejectBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(false);
            }
        });

        findViewById(R.id.acceptBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(true);
            }
        });

        findViewById(R.id.undoBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.undoLastSwipe();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        this.startActivity(new Intent(this, MainActivity.class));
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Utils.updateDatabaseOnStop("Cards");
    }

    public static Animal getAnimalFromIndex(int index) {
        return animalList.get(index);
    }
}
