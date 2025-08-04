package com.ipekkochisarli.obssmovies.features.home

import androidx.annotation.StringRes
import com.ipekkochisarli.obssmovies.R

enum class HomeSectionType(
    val endpoint: String,
    @StringRes val titleRes: Int,
) {
    POPULAR("movie/popular", R.string.section_popular),
    NOW_PLAYING("movie/now_playing", R.string.section_now_playing),
    TOP_RATED("movie/top_rated", R.string.section_top_rated),
    UPCOMING("movie/upcoming", R.string.section_upcoming),
}
