package com.aneeshajose.trending.displayrepos;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.aneeshajose.genericadapters.SimpleGenericAdapterBuilder;
import com.aneeshajose.genericadapters.SimpleGenericRecyclerViewAdapter;
import com.aneeshajose.trending.DependencyInjector;
import com.aneeshajose.trending.R;
import com.aneeshajose.trending.base.App;
import com.aneeshajose.trending.base.activity.BaseActivity;
import com.aneeshajose.trending.common.ConstantsKt;
import com.aneeshajose.trending.models.Repo;
import com.aneeshajose.trending.models.ResponseWrapper;
import com.aneeshajose.trending.utility.InjectorUtilsKt;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import butterknife.BindView;

public class DisplayTrendingReposActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.shimmerLayout)
    ShimmerFrameLayout shimmerFrameLayout;

    @BindView(R.id.rvRepoList)
    RecyclerView rvRepoList;

    private TrendingReposViewModel viewModel;
    private SimpleGenericRecyclerViewAdapter<Repo> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_trending_repos);
        initAdapter();
        initViewModel(savedInstanceState);
        swipeRefresh.setOnRefreshListener(this);
    }

    private void initAdapter() {
        adapter = new SimpleGenericAdapterBuilder<>(context, Repo.class, VH_Repo.LAYOUT, view -> new VH_Repo(view, glide))
                .setSelectable(true)
                .setSingleSelection(true)
                .setMaxSelection(2)
                .setSetSelectionIndicator((viewHolder, isSelected) -> ((VH_Repo) viewHolder).setSelectionIndicator(isSelected))
                .setOnItemSelectionChanged((position, item, isSelected) -> viewModel.setclickedItemPosition(isSelected ? position : ConstantsKt.DEFAULT_SEL_ITEM_POSITION))
                .buildRecyclerViewAdapter();
        rvRepoList.setLayoutManager(new LinearLayoutManager(this));
        rvRepoList.setAdapter(adapter);
    }

    private void initViewModel(Bundle savedInstanceState) {
        viewModel = InjectorUtilsKt.getTrendingReposViewModel(this, savedInstanceState, (App) appContext);
        startShimmer();
        viewModel.getRepos().observe(this, listResponseWrapper -> {
            handleResponse(listResponseWrapper);
            stopLoading();
            handleSavedInstance(viewModel.getclickedItemPosition());
        });
    }

    private void handleSavedInstance(int clickedItemPosition) {
        if (clickedItemPosition < 0 || clickedItemPosition >= adapter.getItemCount()) return;
        adapter.addToSelectedItems(adapter.get(clickedItemPosition));
    }

    private void handleResponse(ResponseWrapper<List<Repo>> listResponseWrapper) {
        if (listResponseWrapper.getBody() != null)
            onReposReceived(listResponseWrapper.getBody());
        else
            showError(listResponseWrapper.getMsg(), true);
    }

    private void onReposReceived(List<Repo> body) {
        if (body.isEmpty()) {
            showError(getString(R.string.oops), getString(R.string.no_relevant_data_found), true);
            return;
        }
        adapter.resetAllItems(body);
    }

    @Override
    protected void callDependencyInjector(DependencyInjector injectorComponent) {
        injectorComponent.injectDependencies(this);
    }

    @Override
    protected void handleRetryClick() {
        swipeRefresh.setRefreshing(true);
        onRefresh();
    }

    private void stopLoading() {
        swipeRefresh.setRefreshing(false);
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        rvRepoList.setVisibility(View.VISIBLE);
    }

    private void startShimmer() {
        rvRepoList.setVisibility(View.GONE);
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_display_repos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miSortByStars:
                viewModel.sortReposByStars(adapter.getAll());
                return true;
            case R.id.miSortByName:
                viewModel.sortReposByName(adapter.getAll());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        startShimmer();
        viewModel.loadRepos();
    }
}
