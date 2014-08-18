package com.twitter_app.tsuru.twitter;

import java.io.Serializable;

/**
 * Created by tsuru on 2014/08/18.
 */
public class TwitterProfileGet implements Serializable{
    public String screenName = null;
    public String name = null;
    public String imageUrl = null;
    public String profileExplain = null;
    public String followNum = null;
    public String followerNum = null;
}
