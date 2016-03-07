# Movie Pedia

This application is a part of Android Developer Nanodegree program, which I made from scratch. Although "Movie Pedia" scritly follows P1-Rubrics, additional functionailties like CollapsingToolBarLayout scroll etc., are also included.
"Movie Pedia" simply fetches data from "moviedb.org" API and presents it as a sophisticated layout.

## Features

With the app, you can:
* Discover the most popular, the most rated or the highest rated movies
* Read a detailed plot summary
* Turn Safe Search on/off to toggle inclusion of adult content
* View movie posters and covers

## TODO

Feature yet to implement:
* Adaptive UI for both phone and tablets
* Native content provider to store movies locally to view them offline
* Migrate sorting functionality from Preference to Spinner on ActionBar/Toolbar
* Trailers in DetailActivity
* Detailed reviews for each movie

## Contribute

This app uses [TMDB](https://www.themoviedb.org/documentation/api) API to retrieve movies.
You must provide your own API key in order to build the app. Obtain the key and make a new file:
    ```
    gradle.properties
    ```
in root directory and place your key in it as:
    ```
    API_KEY="INSERT_YOUR_KEY_HERE"
    ```
. And you are good to go.

## ScreenShots

![screen](../master/screenshots/browse_movie_screen.png)

![screen](../master/screenshots/browse_movie_screen_landscape.png)

![screen](../master/screenshots/movie_detail_screen_1.png)

![screen](../master/screenshots/movie_detail_screen_2.png)

![screen](../master/screenshots/preference_screen.png)

![screen](../master/screenshots/preference_sorting.png)

## Libraries used

* [Picasso](https://github.com/square/picasso)
* [ButterKnife](https://github.com/JakeWharton/butterknife)




[Android Developer Nanodegree](https://www.udacity.com/course/android-developer-nanodegree--nd801)
