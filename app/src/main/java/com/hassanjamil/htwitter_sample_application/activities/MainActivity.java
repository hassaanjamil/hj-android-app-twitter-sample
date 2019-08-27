package com.hassanjamil.htwitter_sample_application.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.hassanjamil.htwitter_sample_application.R;
import com.hassanjamil.htwitter_sample_application.helper.TwitterHelper;

public class MainActivity extends AppCompatActivity implements TwitterHelper.TwitterCallback {

    private final String TAG = MainActivity.class.getSimpleName();
    private TwitterHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        helper = new TwitterHelper(this, this);
        helper.init("YOUR-TWITTER_API_KEY",
                "YOUR_TWITTER_SECRET_KEY");

        setContentView(R.layout.activity_main);

        helper.init(findViewById(R.id.twitterLoginButton));
        helper.init(findViewById(R.id.myTwitterLoginButton));

        findViewById(R.id.logout).setVisibility(View.GONE);
        findViewById(R.id.logout).setOnClickListener(view -> {
            helper.logout();
            view.setVisibility(View.GONE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        helper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();

        helper.onStart();
    }

    @Override
    public void onSuccess(FirebaseUser user) {

        findViewById(R.id.logout).setVisibility(View.VISIBLE);

        String id = user.getUid();
        String name = user.getDisplayName();
        String email = user.getEmail();

        Toast.makeText(this, "@" + name + " (" + email + ") logged in!",
                Toast.LENGTH_LONG).show();

        if (user.getPhotoUrl() != null) {
            // if you want to get full size profile image url or otherwise change the logic of string
            // replacement as required
            String photoUrl = user.getPhotoUrl().toString();
            if (photoUrl.contains("_mini"))
                photoUrl = photoUrl.replace("_mini", "");
            if (photoUrl.contains("_normal"))
                photoUrl = photoUrl.replace("_normal", "");
            if (photoUrl.contains("_bigger"))
                photoUrl = photoUrl.replace("_bigger", "");
            Log.i(TAG + " - onSuccess", id + "-" + name + "-" + email + "-" + photoUrl);
        }
    }

    @Override
    public void onFailure(Exception e) {
        e.printStackTrace();
    }
}
