package com.aneeshajose.trending.base;

import android.app.Application;

import com.aneeshajose.trending.base.modules.AppModule;

/**
 * Created by yashThakur on 01/03/16.
 */
public class App extends Application {

    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        buildAppComponent();
    }

    public void buildAppComponent() {
        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getComponent() {
        return component;
    }

}
