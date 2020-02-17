package com.treflor.ui.profile.journeys

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.treflor.R
import com.treflor.data.remote.response.JourneyResponse
import com.treflor.internal.ui.base.TreflorScopedFragment
import com.treflor.ui.home.list.JourneyItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class UserJourneyFragment : TreflorScopedFragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private lateinit var viewModel: UserJourneyViewModel
    private val viewModelFactory: UserJourneyViewModelFactory by instance()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.user_journey_fragment, container, false)
    }

    private fun bindUI() = launch {
        viewModel.user.await().observe(this@UserJourneyFragment, Observer { user ->
            if (user == null) return@Observer

            GlobalScope.launch(Dispatchers.IO) {
                viewModel.journeys.await().observe(this@UserJourneyFragment, Observer {
                    initRecyclerView(it.toJourneyItems())
                })
            }

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
            setOnItemClickListener { item, _ ->
                (item as? JourneyItem)?.let {
                    val actionDetail =
                        UserJourneyFragmentDirections.actionUserJourneyFragmentToJourneyDetailsFragment(
                            it.journeyResponse.id
                        )

                    navController.navigate(actionDetail)
                }
            }
        }
        rv_journey_rows.apply {
            layoutManager = LinearLayoutManager(this@UserJourneyFragment.context)
            adapter = groupAdapter
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(UserJourneyViewModel::class.java)
        bindUI()
    }

}
