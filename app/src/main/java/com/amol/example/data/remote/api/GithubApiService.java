package com.amol.example.data.remote.api;

import com.amol.example.data.remote.model.GithubApiResponse;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GithubApiService {

    @GET("search/repositories")
    Observable<Response<GithubApiResponse>> fetchRepositories(@Query("sort") String sort,
                                                              @Query("order") String order,
                                                              @Query("page") Long page);
}
