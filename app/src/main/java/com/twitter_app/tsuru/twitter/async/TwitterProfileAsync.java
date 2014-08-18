package com.twitter_app.tsuru.twitter.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.twitter_app.tsuru.twitter.R;
import com.twitter_app.tsuru.twitter.TwitterProfileGet;
import com.twitter_app.tsuru.twitter.TwitterUtils;

import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by tsuru on 2014/08/08.
 */
public class TwitterProfileAsync extends AsyncTask<Void, Void, Void> {
    public Twitter twitter;
    public Context context;
    public TwitterProfileGet myProfile;
    public ProgressDialog progressDialog;


    public TwitterProfileAsync(Context context, TwitterProfileGet myProfile){
        super();
        this.context = context;
        this.myProfile = myProfile;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        twitter = TwitterUtils.getTwitterInstance(context);

        try {
            twitter=TwitterUtils.getTwitterInstance(context);
            myProfile.screenName = twitter.getScreenName();
            myProfile.name = twitter.verifyCredentials().getName();
            myProfile.profileExplain = twitter.verifyCredentials().getDescription();
            myProfile.imageUrl=twitter.users().verifyCredentials().getProfileImageURL();
            myProfile.followNum = String.valueOf(twitter.users().verifyCredentials().getFriendsCount());
            myProfile.followerNum = String.valueOf(twitter.verifyCredentials().getFollowersCount());
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return null;
    }
    // メインスレッドで実行する処理
    @Override
    protected void onPostExecute(Void result) {
    }
}


