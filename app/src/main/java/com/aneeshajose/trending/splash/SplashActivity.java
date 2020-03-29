package com.aneeshajose.trending.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.aneeshajose.trending.DependencyInjector;
import com.aneeshajose.trending.R;
import com.aneeshajose.trending.base.activity.BaseActivity;
import com.aneeshajose.trending.displayrepos.DisplayTrendingReposActivity;

import static com.aneeshajose.trending.common.ConstantsKt.SPLASH_DELAY;

public class SplashActivity extends BaseActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        baseToolbar.setVisibility(View.GONE);
        initView();
    }

    private void initView() {
        new Handler().postDelayed(this::displayTrendingRepos, SPLASH_DELAY);
    }

    private void displayTrendingRepos() {
        startActivity(new Intent(this, DisplayTrendingReposActivity.class));
        overridePendingTransition(R.anim.fadein, R.anim.stay);
        finish();
    }

    @Override
    protected void callDependencyInjector(DependencyInjector injectorComponent) {
        injectorComponent.injectDependencies(this);
    }
}
