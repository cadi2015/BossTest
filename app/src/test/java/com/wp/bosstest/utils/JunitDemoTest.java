package com.wp.bosstest.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by cadi on 2016/10/12.
 */
public class JunitDemoTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void fuckPrint() throws Exception {
        JunitDemo.fuckPrint();
    }

}