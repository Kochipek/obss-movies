package com.ipekkochisarli.obssmovies.features.favorites.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ipekkochisarli.obssmovies.features.favorites.domain.uimodel.LibraryCategoryType
import com.ipekkochisarli.obssmovies.features.favorites.presentation.LibraryTabFragment

class LibraryPagerAdapter(
    fragment: Fragment,
) : FragmentStateAdapter(fragment) {
    private val tabs =
        listOf(
            LibraryCategoryType.WATCHED,
            LibraryCategoryType.WATCH_LATER,
        )

    override fun getItemCount(): Int = tabs.size

    override fun createFragment(position: Int): Fragment = LibraryTabFragment.newInstance(tabs[position])
}
