package com.harshkethwas.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.harshkethwas.myapplication.adapters.NotificationAdapter
import com.harshkethwas.myapplication.databinding.FragmentBidBinding


class BidFragment : Fragment() {

    private lateinit var binding: FragmentBidBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBidBinding.inflate(inflater, container, false)

        val notifications = listOf(
            "Your order has been Canceled Successfully",
            "Order has been taken by the driver", "Congrats Your Order Placed",
            "Your order has been Canceled Successfully",
            "Order has been taken by the driver", "Congrats Your Order Placed"
        )
        val notificationImages = listOf(
            R.drawable.sademoji,
            R.drawable.buss,
            R.drawable.congratulations,
            R.drawable.arrow_left,
            R.drawable.lock,
            R.drawable.congratulations
        )
        val adapter = NotificationAdapter(
            ArrayList(notifications),
            ArrayList(notificationImages)
        )

        binding.notificationRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.notificationRecyclerView.adapter = adapter

        return binding.root
    }

    companion object {

    }
}