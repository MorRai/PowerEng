package com.rai.powereng.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.rai.powereng.R
import com.rai.powereng.databinding.FragmentContentBinding
import com.rai.powereng.extensions.setupWithNavController as setupWithNavControllerExt


class ContentFragment : Fragment() {
    private var _binding: FragmentContentBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentContentBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
           // setupBottomNavigationBar()
            setupBottomNavigationBarStandart()
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        //setupBottomNavigationBar()
        setupBottomNavigationBarStandart()
    }


    private fun setupBottomNavigationBarStandart() {
        with(binding){
            val nestedController =
                (childFragmentManager.findFragmentById(R.id.page_container) as NavHostFragment)
                    .navController
            bottomNavigation.setupWithNavController(nestedController)

          //  val appBarConfiguration = AppBarConfiguration(
         //       setOf(R.id.menu_words, R.id.menu_rating,  R.id.menu_profile)
         //   )
         //   setupActionBarWithNavController((requireActivity() as AppCompatActivity),nestedController, appBarConfiguration)

        }

    }


    private fun setupBottomNavigationBar() {
        val navGraphIds = listOf(
            R.navigation.units_nav_graph,
            R.navigation.rating_nav_graph,
            R.navigation.profile_nav_graph
        )



        with(binding) {
            //написали свою что бы отдельные стеки и корретно отображало
            bottomNavigation.setupWithNavControllerExt(
                navGraphIds = navGraphIds,
                fragmentManager = childFragmentManager,
                containerId = R.id.page_container,
                intent = requireActivity().intent
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}