package com.wp.cheez.utils;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by cadi on 2016/10/20.
 */
@RunWith(AndroidJUnit4.class)
public class FpsUtilTest{

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void fps() throws Exception {
        float result = FpsUtil.fps();
        System.out.println("我就操了，为什么我看不到结果 = "  + result);
    }

}