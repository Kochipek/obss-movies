package com.ipekkochisarli.obssmovies.features.cast.domain

data class CastUiModel(
    val id: Int,
    val name: String,
    val profileImageUrl: String,
    val birthday: String? = null,
    val placeOfBirth: String? = null,
    val knownForDepartment: String? = null,
    val biography: String = "",
)
