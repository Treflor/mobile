package com.treflor.ui.home.detailed

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.treflor.R
import com.treflor.internal.ui.base.TreflorScopedFragment
import kotlinx.android.synthetic.main.journey_details_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.factory

class JourneyDetailsFragment : TreflorScopedFragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: ((String) -> JourneyDetailsViewModelFactory) by factory()
    private lateinit var navController: NavController

    private lateinit var viewModel: JourneyDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.journey_details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val safeArgs = arguments?.let { JourneyDetailsFragmentArgs.fromBundle(it) }
        viewModel =
            ViewModelProviders.of(this, viewModelFactory(safeArgs!!.id))
                .get(JourneyDetailsViewModel::class.java)
        bindUI()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    private fun bindUI() = launch {
        this@JourneyDetailsFragment.setHasOptionsMenu(true)
        (activity as AppCompatActivity).apply {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        viewModel.journey.await().observe(this@JourneyDetailsFragment, Observer { journey ->
            if (journey == null) return@Observer
            Log.e("wtf",journey.journey.toString())
            col_toolbar.title = journey.journey?.title
            tv_difficulty.text = journey.journey?.level
            tv_destination.text = journey.journey?.destination?.name
            tv_origin.text = journey.journey?.origin?.name
            tv_content.text = journey.journey?.content ?: ""
            tv_distance.text = journey.direction?.distance?.text
            tv_duration.text = journey.direction?.duration?.text
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                navController.navigateUp()
                Log.e("wtf", "am i calling?")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
