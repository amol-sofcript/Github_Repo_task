package com.amol.example.ui.activity;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.amol.example.R;
import com.amol.example.data.local.entity.GithubEntity;
import com.amol.example.databinding.GithubDetailActivityBinding;
import com.amol.example.utils.AppUtils;
import com.squareup.picasso.Picasso;

import static com.amol.example.AppConstants.INTENT_POST;

public class GithubDetailActivity extends AppCompatActivity {

    private GithubEntity githubEntity;
    private GithubDetailActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialiseView();
    }

    private void initialiseView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_repo_detail);
        githubEntity = getIntent().getParcelableExtra(INTENT_POST);

        Picasso.get().load(githubEntity.getOwner().getAvatarUrl())
                .placeholder(R.drawable.default_profile_icon_purple)
                .into(binding.itemProfileImg);

        binding.itemTitle.setText(githubEntity.getFullName());
        binding.itemStars.setText(String.valueOf(githubEntity.getStarsCount()));
        binding.itemWatchers.setText(String.valueOf(githubEntity.getWatchers()));
        binding.itemForks.setText(String.valueOf(githubEntity.getForks()));

        if (githubEntity.getLanguage() != null) {
            binding.itemImgLanguage.setVisibility(View.VISIBLE);
            binding.itemLanguage.setVisibility(View.VISIBLE);
            binding.itemLanguage.setText(githubEntity.getLanguage());
            updateColorTheme();

        } else {
            binding.itemImgLanguage.setVisibility(View.INVISIBLE);
            binding.itemLanguage.setVisibility(View.INVISIBLE);
        }

    }


    private void updateColorTheme() {
        int bgColor = AppUtils.getColorByLanguage(getApplicationContext(), githubEntity.getLanguage());

        binding.appBarLayout.setBackgroundColor(bgColor);
        binding.mainToolbar.toolbar.setBackgroundColor(bgColor);
        binding.itemImgLanguage.setImageDrawable(AppUtils.updateGradientDrawableColor(getApplicationContext(), bgColor));
        AppUtils.updateStatusBarColor(this, bgColor);
    }
}
