package com.aneeshajose.trending.base.activity;

import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.aneeshajose.trending.DaggerDependencyInjector;
import com.aneeshajose.trending.DependencyInjector;
import com.aneeshajose.trending.R;
import com.aneeshajose.trending.base.App;
import com.aneeshajose.trending.base.qualifiers.ActivityContext;
import com.aneeshajose.trending.base.qualifiers.ApplicationContext;
import com.bumptech.glide.RequestManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public abstract class BaseActivity extends AppCompatActivity {

    @BindView(R.id.tbBaseToolbar)
    public Toolbar baseToolbar;

    @BindView(R.id.vDivider)
    public View vDivider;

    @BindView(R.id.tvToolbarTitle)
    public TextView tvToolbarTitle;

    @BindView(R.id.rlErrorLayout)
    public RelativeLayout rlErrorLayout;

    @BindView(R.id.tvErrorHeader)
    public TextView tvErrorHeader;

    @BindView(R.id.tvErrorDescription)
    public TextView tvErrorDescription;

    @BindView(R.id.btnRetry)
    public Button btnRetry;

    @ActivityContext
    @Inject
    protected Context context;

    @ApplicationContext
    @Inject
    public Context appContext;

    @Inject
    public RequestManager glide;

    private RelativeLayout baseLayout;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        baseLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.base_layout, null);
        setContentView(baseLayout);
        stubLayoutRes(layoutResID);
        ButterKnife.bind(this);
        initialiseDaggerDependencies();
        initActionBar();
    }

    private void stubLayoutRes(@LayoutRes int layoutResID) {
        ViewStub stub = baseLayout.findViewById(R.id.container);
        stub.setLayoutResource(layoutResID);
        stub.inflate();
    }

    private void initActionBar() {
        setSupportActionBar(baseToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        baseToolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.more_black_24));
    }

    private void initialiseDaggerDependencies() {
        callDependencyInjector(initialiseDaggerInjector());
    }

    private DependencyInjector initialiseDaggerInjector() {
        return DaggerDependencyInjector.builder()
                .appComponent(((App) getApplication()).getComponent())
                .activityModule(new ActivityModule(this))
                .build();
    }

    protected abstract void callDependencyInjector(DependencyInjector injectorComponent);

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null && item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btnRetry)
    public void retry() {
        handleRetryClick();
    }

    /**
     * override to give retry button a mechanism if using the retry button
     */
    protected void handleRetryClick() {
    }

    protected void showError(boolean shouldShowRetryButton) {
        rlErrorLayout.setVisibility(View.VISIBLE);
        btnRetry.setVisibility(shouldShowRetryButton ? View.VISIBLE : View.GONE);
    }

    protected void showError(String errorDescription, boolean shouldShowRetryButton) {
        showError(shouldShowRetryButton);
        tvErrorDescription.setText(errorDescription);
    }

    protected void showError(String errorHeader, String errorDescription, boolean shouldShowRetryButton) {
        showError(errorDescription, shouldShowRetryButton);
        tvErrorHeader.setText(errorHeader);
    }

    protected void hideError() {
        rlErrorLayout.setVisibility(View.GONE);
    }
}
