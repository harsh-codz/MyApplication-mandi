package com.harshkethwas.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_category -> {
                    replaceFragment(Marketplace())
                    activity?.title = "Home"
                }

                R.id.bottom_history -> {
                    replaceFragment(SearchFragment())
                    activity?.title = "Search"
                }

                R.id.bottom_notification -> {
                    replaceFragment(BidFragment())
                    activity?.title = "Place bids"
                }

                R.id.bottom_cart -> {
                    replaceFragment(OrdersFragment())
                    activity?.title = "Cart"
                }
            }
            true

        }
        replaceFragment(Marketplace())
        activity?.title = "Category"
        bottomNavigationView.selectedItemId = R.id.bottom_category

        val addFab = view.findViewById<FloatingActionButton>(R.id.addFabBtn)
        addFab.setOnClickListener {

            Toast.makeText(context,"Add Clicked", Toast.LENGTH_LONG).show()
        }
        return view
    }
    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.bottomFragment,fragment)
            .commit()
    }
}