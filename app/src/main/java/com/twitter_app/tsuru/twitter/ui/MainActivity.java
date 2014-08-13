

package com.twitter_app.tsuru.twitter.ui;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.twitter_app.tsuru.twitter.R;
import com.twitter_app.tsuru.twitter.adapter.TweetTimelineAdapter;
import com.twitter_app.tsuru.twitter.TwitterUtils;
import com.twitter_app.tsuru.twitter.authenticator.TwitterOAuthActivity;
import java.util.List;

import twitter4j.Paging;
import twitter4j.Twitter;
import twitter4j.TwitterException;


public class MainActivity extends ListActivity{

    private TweetTimelineAdapter adapter;
    private Twitter twitter;
    ProgressDialog prog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!TwitterUtils.hasAccessToken(this)) {//アクセストークンが存在していなかったときの処理
            Intent intent = new Intent(this, TwitterOAuthActivity.class);
            startActivity(intent);
            finish();
        } else {
            adapter = new TweetTimelineAdapter(this);
            setListAdapter(adapter);
            prog=new ProgressDialog(this);
            prog.setProgressStyle(prog.STYLE_SPINNER);
            prog.setMessage("読み込み中です");
            prog.setCancelable(true);
            prog.show();
            twitter = TwitterUtils.getTwitterInstance(this);
            reloadTimeLine();

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_twitter_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh://更新ボタンを押したときの処理
                reloadTimeLine();
                showToast("更新しました！");
                return true;
            case R.id.menu_tweet://投稿ボタンを押したときの処理
                Intent intent = new Intent(this, MyTweetActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_home://ホームボタンを押したときの処理
                Intent profile = new Intent(this, MyTwitterProfileActivity.class);
                startActivity(profile);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void reloadTimeLine() {//非同期によるタイムラインの取得
        AsyncTask<Void, Void, List<twitter4j.Status>> task = new AsyncTask<Void, Void, List<twitter4j.Status>>() {


            @Override
            protected List<twitter4j.Status> doInBackground(Void... params) {
                try {
                    Paging paging=new Paging();
                    //タイムラインの取得数を指定
                    paging.setCount(200);


                    return twitter.getHomeTimeline(paging);
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<twitter4j.Status> result) {
                if (result != null) {
                    prog.dismiss();
                    adapter.clear();
                    for (final twitter4j.Status status : result) {
                        adapter.add(status);
                    }
                    getListView().setSelection(0);
                } else {
                    showToast("タイムラインの取得に失敗しました。。。");
                }
            }
        };
        task.execute();
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}