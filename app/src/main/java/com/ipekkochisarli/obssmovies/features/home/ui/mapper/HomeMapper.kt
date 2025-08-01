package com.ipekkochisarli.obssmovies.features.home.ui.mapper

import com.ipekkochisarli.obssmovies.common.CarouselItem
import com.ipekkochisarli.obssmovies.features.home.domain.MovieUiModel

fun List<MovieUiModel>.toCarouselItems(): List<CarouselItem> =
    this.take(3).map { movie ->
        CarouselItem(
            imageUrl = movie.carouselUrl.orEmpty(),
            title = movie.title,
            releaseYear = movie.releaseYear,
        )
    }
