package com.wwdevelopers.codebook.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wwdevelopers.codebook.R;
import com.wwdevelopers.codebook.adapters.PlatformsAdapter;
import com.wwdevelopers.codebook.interfaces.ItemClickListener;
import com.wwdevelopers.codebook.models.Platform;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

    Activity mActivity;
    @BindView(R.id.platforms_recycler_view) RecyclerView platforms_recycler_view;
    @BindView(R.id.progressBar) ProgressBar progressBar;


    DatabaseReference databaseReference;
    ArrayList<Platform> platforms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Make sure this is before calling super.onCreate
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActivity = MainActivity.this;
        ButterKnife.bind(mActivity);

        databaseReference = FirebaseDatabase.getInstance().getReference();



        platforms = new ArrayList<>();
        getPublicationPlatforms();



    }

    private void getPublicationPlatforms() {

        showLoading();
        databaseReference.child("platforms").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Platform platform = snapshot.getValue(Platform.class);
                    platforms.add(platform);

                }

                showPlatforms();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                hideLoading();

            }
        });

    }

    private void showPlatforms() {

        PlatformsAdapter platformsAdapter = new PlatformsAdapter(mActivity, platforms);
        platformsAdapter.setClickListener(platformClickListener);
        platforms_recycler_view.setLayoutManager(new LinearLayoutManager(mActivity));
        platforms_recycler_view.setAdapter(platformsAdapter);
        hideLoading();

    }


    private void showLoading(){
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading(){
        progressBar.setVisibility(View.GONE);
    }

    ItemClickListener platformClickListener = new ItemClickListener() {
        @Override
        public void onClick(View view, int position) {

            Platform platform = platforms.get(position);
            Intent intent = new Intent(mActivity, WebViewActivity.class);
            intent.putExtra("platform", platform);
            startActivity(intent);

        }

    };



    @OnClick(R.id.open_bookmark_fab)
    public void goToBookmark(){

        Intent intent = new Intent(mActivity,BookmarkActivity.class);
        startActivity(intent);

    }





}
