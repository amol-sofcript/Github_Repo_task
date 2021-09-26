package com.amol.example.ui.activity;

import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.View;

import com.amol.example.R;
import com.amol.example.data.local.entity.GithubEntity;
import com.amol.example.databinding.GithubListActivityBinding;
import com.amol.example.factory.ViewModelFactory;
import com.amol.example.ui.adapter.GithubListAdapter;
import com.amol.example.ui.custom.recyclerview.RecyclerLayoutClickListener;
import com.amol.example.ui.custom.recyclerview.RecyclerViewPaginator;
import com.amol.example.ui.viewmodel.GithubListViewModel;
import com.amol.example.utils.AppUtils;
import com.amol.example.utils.NavigatorUtils;
import com.amol.example.utils.ShareUtils;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class GithubListActivity extends AppCompatActivity implements RecyclerLayoutClickListener {

    @Inject
    ViewModelFactory viewModelFactory;
    private GithubListActivityBinding binding;
    private GithubListViewModel githubListViewModel;
    private GithubListAdapter githubListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        initialiseViewModel();
        initialiseView();
    }

    private void initialiseView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_repo_list);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        githubListAdapter = new GithubListAdapter(getApplicationContext(), this);
        binding.recyclerView.setAdapter(githubListAdapter);
        binding.recyclerView.addOnScrollListener(new RecyclerViewPaginator(binding.recyclerView) {
            @Override
            public boolean isLastPage() {
                return githubListViewModel.isLastPage();
            }

            @Override
            public void loadMore() {
                githubListViewModel.fetchRepositories();
            }
        });

        /* This is to handle configuration changes:
         * during configuration change, when the activity
         * is recreated, we check if the viewModel
         * contains the list data. If so, there is no
         * need to call the api or load data from cache again */
        if (githubListViewModel.getRepositories().isEmpty()) {
            displayLoader();
            githubListViewModel.fetchRepositories();
        } else animateView(githubListViewModel.getRepositories());
    }


    private void initialiseViewModel() {
        githubListViewModel = ViewModelProviders.of(this, viewModelFactory).get(GithubListViewModel.class);
        githubListViewModel.getRepositoryListLiveData().observe(this, repositories -> {
            if (githubListAdapter.getItemCount() == 0) {
                if (!repositories.isEmpty()) {
                    animateView(repositories);

                } else displayEmptyView();

            } else if (!repositories.isEmpty()) displayDataView(repositories);
        });
    }

    private void displayLoader() {
        binding.viewLoader.rootView.setVisibility(View.VISIBLE);
    }

    private void hideLoader() {
        binding.viewLoader.rootView.setVisibility(View.GONE);
    }


    private void animateView(List<GithubEntity> repositories) {
        hideLoader();
        displayDataView(repositories);
        binding.recyclerView.scheduleLayoutAnimation();
    }

    private void displayDataView(List<GithubEntity> repositories) {
        binding.viewEmpty.emptyContainer.setVisibility(View.GONE);
        githubListAdapter.setItems(repositories);
    }

    private void displayEmptyView() {
        hideLoader();
        binding.viewEmpty.emptyContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void redirectToDetailScreen(View imageView, View titleView, View revealView, View languageView, GithubEntity githubEntity) {
        NavigatorUtils.redirectToDetailScreen(this, githubEntity,
                ActivityOptionsCompat.makeSceneTransitionAnimation(this, AppUtils.getTransitionElements(
                        getApplicationContext(), imageView, titleView, revealView, languageView
                )));
    }

    @Override
    public void sharePost(GithubEntity githubEntity) {
        ShareUtils.shareUrl(this, githubEntity.getHtmlUrl());
    }
}
