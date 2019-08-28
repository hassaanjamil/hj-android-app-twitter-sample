package com.hassanjamil.htwitter_sample_application.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.hassanjamil.htwitter_sample_application.R;
import com.hassanjamil.htwitter_sample_application.helper.TwitterHelper;

public class MainActivity extends AppCompatActivity implements TwitterHelper.TwitterCallback {

    private final String TAG = MainActivity.class.getSimpleName();

    private TwitterHelper helper;
    private ImageView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        helper = new TwitterHelper(this, this);
        // initialize with API KEY and SECRET KEY to enable TwitterLoginButton
        helper.init("4KXMADJSoMZN2zpekOehuF9Ro",
                "gTG2tgqKVFXFDKJHYssM75cxohhDzV1oCvcIEZYV3AfwTI0tUz");

        setContentView(R.layout.activity_main);

        onCreateUI();
    }

    private void onCreateUI() {
        findViewById(R.id.cvUser).setVisibility(View.GONE);
        findViewById(R.id.tvUserProfileImageUrl).setVisibility(View.GONE);
        logout = findViewById(R.id.logout);
        logout.setVisibility(View.GONE);

        // initialize TwitterLoginButton's function
        helper.init(findViewById(R.id.twitterLoginButton));
        // initialize custom TwitterLoginButton's function
        helper.init(findViewById(R.id.myTwitterLoginButton));

        logout.setOnClickListener(view -> logout());
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
        updateUI(user);
    }

    @Override
    public void onFailure(Exception e) {
        e.printStackTrace();
    }

    /**
     * Method set the information received on layout also your can see the received information in
     * Logcat (Debug/Verbose) view
     *
     * @param user {@link FirebaseUser}
     */
    private void updateUI(FirebaseUser user) {

        findViewById(R.id.cvUser).setVisibility(View.VISIBLE);
        findViewById(R.id.tvUserProfileImageUrl).setVisibility(View.VISIBLE);
        logout.setVisibility(View.VISIBLE);

        TextView tvName = findViewById(R.id.tvUserName);
        TextView tvEmail = findViewById(R.id.tvUserEmail);
        TextView tvUid = findViewById(R.id.tvUserUid);
        TextView tvProfileImageUrl = findViewById(R.id.tvUserProfileImageUrl);
        ImageView ivUser = findViewById(R.id.ivUser);

        UserInfo userInfo = user.getProviderData().get(1);
        String strInfo = "";

        String htmlUid = "<b>Uid: </b> <a href=\"https://twitter.com/intent/user?user_id="
                + userInfo.getUid() + "\">" + userInfo.getUid() + "</a>";
        strInfo += htmlUid;
        setHtmlTextViewForString(tvUid, htmlUid);
        strInfo += "<b>DisplayName:</b> " + userInfo.getDisplayName() + "<br/>";
        tvName.setText(userInfo.getDisplayName());
        strInfo += "<b>Email:</b> " + userInfo.getEmail() + "<br/>";
        tvEmail.setText(userInfo.getEmail());

        if (userInfo.getPhotoUrl() != null) {
            // if you want to get full size profile image url or otherwise change the logic of string
            // replacement as required
            String photoUrl = userInfo.getPhotoUrl().toString();
            if (photoUrl.contains("_mini"))
                photoUrl = photoUrl.replace("_mini", "");
            if (photoUrl.contains("_normal"))
                photoUrl = photoUrl.replace("_normal", "");
            if (photoUrl.contains("_bigger"))
                photoUrl = photoUrl.replace("_bigger", "");
            String strHtml = "<b>PhotoUrl:</b> <a href=\"" + photoUrl + "\">" + photoUrl + "</a>";
            strInfo += strHtml;
            Log.d(TAG + " - onSuccess", strInfo);

            setHtmlTextViewForString(tvProfileImageUrl, strHtml);

            Glide.with(this).load(photoUrl).into(ivUser);
        }
    }

    private void setHtmlTextViewForString(TextView textView, String htmlString) {
        textView.setText(Html.fromHtml(htmlString));
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * Method ensure logout the current user session
     */
    private void logout() {
        helper.logout();

        resetUI();
    }

    /**
     * Method resets the UI and invoked from {@link MainActivity#logout}
     */
    private void resetUI() {
        ((TextView) findViewById(R.id.tvUserName)).setText("");
        ((TextView) findViewById(R.id.tvUserEmail)).setText("");
        ((TextView) findViewById(R.id.tvUserUid)).setText("");
        ((ImageView) findViewById(R.id.ivUser)).setImageResource(android.R.color.transparent);

        onCreateUI();
    }
}
