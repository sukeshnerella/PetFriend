package com.example.petfriend.cards;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.example.petfriend.R;
import com.example.petfriend.User;
import com.example.petfriend.main.fragments.FavoritesFragment;
import com.example.petfriend.CardsActivity;
import com.example.petfriend.Utils;
import com.example.petfriend.CardsActivity;
import com.example.petfriend.User;
import com.example.petfriend.Utils;
import com.example.petfriend.main.fragments.FavoritesFragment;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeHead;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;
import com.mindorks.placeholderview.annotations.swipe.SwipeView;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import com.example.petfriend.R;

@NonReusable
public class TinderCard extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tinder_card_view);
        ImageView profileImageView = findViewById(R.id.profileImageView);
        TextView nameAgeTxt = findViewById(R.id.nameAgeTxt);
        TextView locationNameTxt = findViewById(R.id.locationNameTxt);


    }
    private static Animal mAnimal;
    private static Context mContext;
    private static SwipePlaceHolderView mSwipeView;

    private User user;
    private static int index = 0;
    public TinderCard(Context context, Animal animal, SwipePlaceHolderView swipeView) {
        mContext = context;
        mAnimal = animal;
        mSwipeView = swipeView;
    }

    // called when the next card is up on display
    // starts a new thread and loads image, sets text, etc.
    @Resolve
    public void onResolved(){
        Thread thread = new Thread();
        thread.start();

        Glide.with(this).load(mAnimal.getImageUrl()).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(Utils.dpToPx(7), 0, RoundedCornersTransformation.CornerType.TOP))).into((ImageView) findViewById(R.id.profileImageView));
        ((TextView) findViewById(R.id.nameAgeTxt)).setText(mAnimal.getName() + ", " + mAnimal.getAge());
        ((TextView) findViewById(R.id.locationNameTxt)).setText(mAnimal.getLocation());
    }




    public void onSwipeHeadCard() {
        ImageView profileImageView = findViewById(R.id.profileImageView);
        Glide.with(mContext).load(mAnimal.getImageUrl()).into(profileImageView);
        findViewById(R.id.cardview).invalidate();
    }

    public void onClick(View profileImageView){
        Log.d("EVENT", "profileImageView click");
    }


    // when a card is swiped left
    @SwipeOut
    public void onSwipedOut(){
        Log.d("EVENT", "onSwipedOut");
        index++;
        User.incrementCounter();
    }

    @SwipeCancelState
    public void onSwipeCancelState(){
        Log.d("EVENT", "onSwipeCancelState");
    }

    // when a card is swiped right
    @SwipeIn
    public void onSwipeIn(){
        Log.d("EVENT", "onSwipedIn");
        FavoritesFragment.animalList.add(CardsActivity.getAnimalFromIndex(index));
        Log.d("TinderCard", FavoritesFragment.animalList.toString());
        index++;
        User.incrementCounter();
        User.incrementLikedCounter();
    }

    @SwipeInState
    public void onSwipeInState(){
        Log.d("EVENT", "onSwipeInState");
    }

    @SwipeOutState
    public void onSwipeOutState(){
        Log.d("EVENT", "onSwipeOutState");
    }

    public static void setIndex(int i) {
        index = i;
    }
    public static int getIndex() {
        return index;
    }
}
