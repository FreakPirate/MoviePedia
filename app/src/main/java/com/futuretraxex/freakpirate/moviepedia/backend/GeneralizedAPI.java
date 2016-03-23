package com.futuretraxex.freakpirate.moviepedia.backend;

import com.futuretraxex.freakpirate.moviepedia.data.Models.ReviewModel;
import com.futuretraxex.freakpirate.moviepedia.data.Models.TrailerModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by FreakPirate on 3/23/2016.
 */
public interface GeneralizedAPI {

    @GET("/3/movie/{id}/reviews")
    Call<ReviewModel> getMovieReviews(@Path("id") int movieId, @Query("api_key") String apiKey);

    @GET("/3/movie/{id}/trailers")
    Call<TrailerModel> getMovieTrailer(@Path("id") int movieId, @Query("api_key") String apiKey);
}
