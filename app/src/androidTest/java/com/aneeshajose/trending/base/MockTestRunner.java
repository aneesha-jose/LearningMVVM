package com.aneeshajose.trending.base;

import android.app.Application;
import android.content.Context;

import androidx.test.runner.AndroidJUnitRunner;

import io.appflate.restmock.RESTMockServerStarter;
import io.appflate.restmock.android.AndroidAssetsFileParser;
import io.appflate.restmock.android.AndroidLogger;

/**
 * Created by Aneesha on 29/01/18.
 */

public class MockTestRunner extends AndroidJUnitRunner {

    @Override
    public Application newApplication(ClassLoader cl, String className, Context context) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        RESTMockServerStarter.startSync(new AndroidAssetsFileParser(getContext()), new AndroidLogger());
        return super.newApplication(cl, TestApp.class.getName(), context);
    }
}
