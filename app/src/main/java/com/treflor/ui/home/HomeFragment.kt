package com.treflor.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.treflor.R
import com.treflor.data.remote.response.JourneyResponse
import com.treflor.internal.ui.base.TreflorScopedFragment
import com.treflor.ui.home.list.JourneyItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class HomeFragment : TreflorScopedFragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: HomeViewModelFactory by instance()
    private lateinit var viewModel: HomeViewModel
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)
        navController = Navigation.findNavController(view)
        bindUI()
    }

    private fun bindUI() = launch {
        viewModel.journeys.await().observe(this@HomeFragment, Observer {
            if (it == null) return@Observer
            initRecyclerView(it.toJourneyItems())
            progress_circular.visibility = View.GONE
        })

    }

    private fun List<JourneyResponse>.toJourneyItems(): List<JourneyItem> {
        return this.map {
            JourneyItem(it)
        }
    }

    private fun initRecyclerView(items: List<JourneyItem>) {
        val groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
            addAll(items)
            setOnItemClickListener { _, _ ->
                navController.navigate(R.id.action_homeFragment_to_journeyDetailsFragment)
            }
        }
        rv_journey_rows.apply {
            layoutManager = LinearLayoutManager(this@HomeFragment.context)
            adapter = groupAdapter
        }
    }
}