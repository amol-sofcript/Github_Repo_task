package com.amol.example.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import com.amol.example.data.local.entity.GithubEntity;
import com.amol.example.ui.activity.GithubDetailActivity;

import static com.amol.example.AppConstants.INTENT_POST;

public class NavigatorUtils {

    public static void redirectToDetailScreen(Activity activity,
                                              GithubEntity githubEntity,
                                              ActivityOptionsCompat options) {

        Intent intent = new Intent(activity, GithubDetailActivity.class);
        intent.putExtra(INTENT_POST, githubEntity);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    public static void openBrowser(Activity activity,
                                   String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        activity.startActivity(i);
    }
}
