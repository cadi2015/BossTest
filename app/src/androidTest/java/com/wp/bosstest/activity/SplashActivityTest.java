package com.wp.bosstest.activity;

import android.app.Activity;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by cadi on 2016/10/20.
 */
@RunWith(AndroidJUnit4.class)
public class SplashActivityTest extends ActivityInstrumentationTestCase2 {
    private Activity mActivity;

    public SplashActivityTest() {
        super(SplashActivity.class);
    }

    @Before
    public void setUp(){
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();
    }

    @Test
    public void launch(){
        if(mActivity == null) {
            System.out.println("null&&&…………%%%……………………");
        } else {
            System.out.println("OKOKOKOKOKOKOKOKOKOKOKOKOKOKOKOKOKOKOKOKOKOKOK");
        }
    }

}
