package com.amol.example.data.repository;

import androidx.annotation.NonNull;

import com.amol.example.data.NetworkBoundResource;
import com.amol.example.data.Resource;
import com.amol.example.data.local.dao.GithubDao;
import com.amol.example.data.local.entity.GithubEntity;
import com.amol.example.data.remote.api.GithubApiService;
import com.amol.example.data.remote.model.GithubApiResponse;

import java.util.List;

import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Observable;

import static com.amol.example.AppConstants.QUERY_ORDER;
import static com.amol.example.AppConstants.QUERY_SORT;

@Singleton
public class GithubRepository {

    private GithubDao githubDao;
    private GithubApiService githubApiService;

    public GithubRepository(GithubDao githubDao, GithubApiService githubApiService) {
        this.githubDao = githubDao;
        this.githubApiService = githubApiService;
    }

    public Observable<Resource<List<GithubEntity>>> getRepositories(Long page) {
        return new NetworkBoundResource<List<GithubEntity>, GithubApiResponse>() {

            @Override
            protected void saveCallResult(@NonNull GithubApiResponse item) {
                List<GithubEntity> repositories = item.getItems();
                for (GithubEntity githubEntity : repositories) {
                    githubEntity.setPage(page);
                    githubEntity.setTotalPages(item.getTotalCount());
                }
                githubDao.insertRepositories(repositories);
            }

            @Override
            protected boolean shouldFetch() {
                return true;
            }

            @NonNull
            @Override
            protected Flowable<List<GithubEntity>> loadFromDb() {
                List<GithubEntity> repositories = githubDao.getRepositoriesByPage(page);
                return (repositories == null || repositories.isEmpty()) ?
                        Flowable.empty() : Flowable.just(repositories);
            }

            @NonNull
            @Override
            protected Observable<Resource<GithubApiResponse>> createCall() {
                return githubApiService.fetchRepositories(QUERY_SORT, QUERY_ORDER, page)
                        .flatMap(response ->
                                Observable.just(response.isSuccessful()
                                        ? Resource.success(response.body())
                                        : Resource.error("", new GithubApiResponse())));
            }

        }.getAsObservable();
    }
}
