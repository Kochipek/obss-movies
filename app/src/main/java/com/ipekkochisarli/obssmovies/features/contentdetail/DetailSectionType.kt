package com.ipekkochisarli.obssmovies.features.contentdetail

import com.ipekkochisarli.obssmovies.R

enum class DetailSectionType(
    val titleResId: Int,
) {
    CREDITS(R.string.section_cast),
    VIDEOS(R.string.section_videos),
    SIMILAR_MOVIES(R.string.section_similar_movies),
}
