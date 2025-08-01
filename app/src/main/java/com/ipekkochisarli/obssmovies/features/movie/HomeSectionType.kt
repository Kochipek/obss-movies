package com.ipekkochisarli.obssmovies.features.movie

enum class HomeSectionType(
    val endpoint: String,
) {
    POPULAR("movie/popular"),
    NOW_PLAYING("movie/now_playing"),
    TOP_RATED("movie/top_rated"),
    UPCOMING("movie/upcoming"),
}
