package com.example.munkyushin.retrofit2example;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by MunkyuShin on 3/12/16.
 */
public interface GithubService {
    @GET("users/{user}/repos")
    Call<List<Repository>> listRepos(@Path("user") String user);
}
