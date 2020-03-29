package com.aneeshajose.trending.base;

import androidx.multidex.MultiDexApplication;

import com.aneeshajose.trending.base.modules.AppModule;
import com.aneeshajose.trending.base.modules.NetworkModule;
import com.aneeshajose.trending.network.NetworkConstantsKt;

/**
 * Created by yashThakur on 01/03/16.
 */
public class App extends MultiDexApplication {

    protected AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        buildAppComponent();
    }

    public void buildAppComponent() {
        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule(NetworkConstantsKt.BASE_URL))
                .build();
    }

    public AppComponent getComponent() {
        return component;
    }

}
