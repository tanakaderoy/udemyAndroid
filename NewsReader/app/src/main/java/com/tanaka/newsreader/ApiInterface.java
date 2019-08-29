package com.tanaka.newsreader;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {
        @GET("v0/topstories.json?print=pretty")
        Call<List<Integer>> getTopStories();

        @GET("v0/item/{articleId}.json?print=pretty")
        Call<Story> getArticle(@Path("articleId") int id);
    }