package com.hassanjamil.htwitter_sample_application.helper;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.TwitterAuthProvider;
import com.hassanjamil.htwitter_sample_application.R;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

/**
 * Initialize the {@link TwitterHelper} instance helper with
 * {@link TwitterHelper#init(String, String)} method before calling setContentView in on create
 * so that {@link com.twitter.sdk.android.core.identity.TwitterLoginButton} set to enable
 * state because of pre-initialization
 */
public class TwitterHelper {

    private Activity mActivity;
    private FirebaseAuth mAuth;
    private TwitterLoginButton mTLoginButton;
    private TwitterCallback mTwitterCallback;
    private final String TAG = TwitterHelper.class.getSimpleName();

    public TwitterHelper(Activity activity, TwitterCallback callback) {
        mActivity = activity;
        mAuth = FirebaseAuth.getInstance();
        mTwitterCallback = callback;
    }

    /**
     * Call the method before setContentView of Activity to make TwitterLoginButton enable
     */
    public void init(String consumerKey, String consumerSecret) {

        // To avoid SSL exception on lower Android APIs
        initializeSSLContext(mActivity);

        TwitterConfig config = new TwitterConfig.Builder(mActivity)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(consumerKey, consumerSecret))
                .debug(true)
                .build();
        Twitter.initialize(config);
    }

    /**
     * To avoid SSL exception on lower Android APIs at twitter initialization
     */
    private static void initializeSSLContext(Context mContext) {
        try {
            SSLContext.getInstance("TLSv1.2");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            ProviderInstaller.installIfNeeded(mContext.getApplicationContext());
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Call in onCreate method of activity after setContentView and Pass {@link TwitterLoginButton}
     * view reference to set and initialize {@link TwitterLoginButton}.
     *
     * @param button {@link TwitterLoginButton}
     */
    public void init(TwitterLoginButton button) {
        mTLoginButton = button;
        mTLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                handleTwitterSession(result.data);
            }

            @Override
            public void failure(TwitterException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * If you want user information at start of the activity
     */
    public void onStart() {
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null && mTwitterCallback != null) {
            mTwitterCallback.onSuccess(currentUser);
        }
    }

    private void handleTwitterSession(TwitterSession session) {
        Log.d(TAG, "handleTwitterSession:" + session);

        final MaterialDialog mProgressDialog = createProgressDialog(mActivity, null,
                mActivity.getString(R.string.please_wait), false, true);

        AuthCredential credential = TwitterAuthProvider.getCredential(
                session.getAuthToken().token,
                session.getAuthToken().secret);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(mActivity, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (mTwitterCallback != null)
                            mTwitterCallback.onSuccess(user);

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        if (mTwitterCallback != null)
                            mTwitterCallback.onFailure(task.getException());
                    }
                    mProgressDialog.hide();
                });
    }

    /**
     * Use this method to sign out user session from the application
     */
    public void logout() {
        mAuth.signOut();
        TwitterCore.getInstance().getSessionManager().clearActiveSession();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Activity result work for twitter
        mTLoginButton.onActivityResult(requestCode, resultCode, data);
    }

    public interface TwitterCallback {
        void onSuccess(FirebaseUser user);

        void onFailure(Exception e);
    }

    private MaterialDialog createProgressDialog(Context context, String title, String text,
                                                boolean cancelable, boolean show) {

        MaterialDialog.Builder progressDialogBuilder = new MaterialDialog.Builder(context);

        if (isValidString(title))
            progressDialogBuilder.title(title);

        if (isValidString(text))
            progressDialogBuilder.content(text);

        progressDialogBuilder.progress(true, 0);
        progressDialogBuilder.cancelable(cancelable);
        MaterialDialog materialDialog = progressDialogBuilder.build();

        /*if (StringUtils.isValidString(context.getString(R.string.cancel)) && listener != null)
            materialDialog.on(context.getString(R.string.cancel), listener);*/

        if (cancelable) {
            materialDialog.setOnCancelListener(DialogInterface::dismiss);
        }

        if (show && !materialDialog.isShowing())
            materialDialog.show();

        return materialDialog;
    }

    /**
     * Method is used for checking valid String.
     *
     * @return boolean true for valid, false for invalid
     */
    private boolean isValidString(String str) {
        return str != null && !(TextUtils.isEmpty(str.trim()) || str.trim().equals(""))
                && !str.trim().equals("null");

    }
}
