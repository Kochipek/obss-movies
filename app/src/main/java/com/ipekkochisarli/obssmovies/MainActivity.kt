package com.ipekkochisarli.obssmovies

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.ipekkochisarli.obssmovies.databinding.ActivityMainBinding
import com.ipekkochisarli.obssmovies.util.extensions.gone
import com.ipekkochisarli.obssmovies.util.extensions.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val showBottomNavigationIds =
        listOf(
            R.id.homeFragment,
            R.id.searchFragment,
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.bottomNav?.setOnApplyWindowInsetsListener { view, insets ->
            view.updatePadding(0, 0, 0, 0)
            insets
        }

        navController =
            (supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment).navController

        val isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        if (isLandscape) {
            binding.bottomNav?.gone()
            binding.navigationRail?.visible()
            binding.navigationRail?.setupWithNavController(navController)
        } else {
            binding.bottomNav?.visible()
            binding.navigationRail?.gone()
            binding.bottomNav?.setupWithNavController(navController)
        }

        binding.bottomNav?.setOnItemReselectedListener { }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in showBottomNavigationIds) {
                binding.bottomNav?.visible()
                binding.navigationRail?.visible()
            } else {
                binding.bottomNav?.gone()
                binding.navigationRail?.gone()
            }
        }
    }
}
