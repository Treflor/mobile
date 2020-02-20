package com.treflor.ui.home.detailed

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar

import com.treflor.R
import com.treflor.internal.eventexcecutor.ActivityNavigation
import com.treflor.internal.ui.base.TreflorScopedFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.journey_details_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.factory

class JourneyDetailsFragment : TreflorScopedFragment(), KodeinAware, ActivityNavigation {

    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: ((String) -> JourneyDetailsViewModelFactory) by factory()
    private lateinit var navController: NavController
    private lateinit var id: String

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
        id = safeArgs!!.id
        viewModel =
            ViewModelProviders.of(this, viewModelFactory(safeArgs!!.id))
                .get(JourneyDetailsViewModel::class.java)
        viewModel.liveMessageEvent.setEventReceiver(this, this)
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
            progress_bar.visibility = View.GONE
            col_toolbar.title = journey.journey?.title
            tv_difficulty.text = journey.journey?.level
            tv_destination.text = journey.journey?.destination?.name
            tv_origin.text = journey.journey?.origin?.name
            tv_content.text = journey.journey?.content ?: ""
            tv_distance.text = journey.direction?.distance?.text
            tv_duration.text = journey.direction?.duration?.text
            journey.images?.let {
                initRecyclerView(journey.images.toImageItems())
            }
            val options: RequestOptions = RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.background)
                .error(R.drawable.background)
            Glide.with(context!!).load(journey.journey?.image).apply(options)
                .into(img_cover)

            if (viewModel.userId.isNullOrEmpty()) {
                img_btn_favorite.setOnClickListener {
                    navController.navigate(R.id.action_journeyDetailsFragment_to_loginFragment)
                }
            } else {
                journey.favorites?.let {
                    if (it.contains(viewModel.userId!!)) {
                        img_btn_favorite.setImageResource(R.drawable.ic_favorite)
                        img_btn_favorite.setOnClickListener {
                            img_btn_favorite.setBackgroundResource(R.drawable.ic_favorite_border)
                            viewModel.removeFromFavorite()
                        }
                    } else {
                        img_btn_favorite.setImageResource(R.drawable.ic_favorite_border)
                        img_btn_favorite.setOnClickListener {
                            img_btn_favorite.setBackgroundResource(R.drawable.ic_favorite)
                            viewModel.addToFavorite()
                        }
                    }
                }
                if (journey.favorites == null) {
                    img_btn_favorite.setImageResource(R.drawable.ic_favorite_border)
                    img_btn_favorite.setOnClickListener {
                        img_btn_favorite.setBackgroundResource(R.drawable.ic_favorite)
                        viewModel.addToFavorite()
                    }
                }
            }
        })

        fab_show_map.setOnClickListener {
            val actionDetail =
                JourneyDetailsFragmentDirections.actionJourneyDetailsFragmentToDetailedMapFragment(id)
            navController.navigate(actionDetail)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                navController.navigateUp()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun navigateUp(): Boolean {
        navController.navigate(R.id.action_signUpFragment_to_profileFragment)
        return true
    }

    override fun showSnackBar(s: String) {
        if (view != null) Snackbar.make(view!!, s, Snackbar.LENGTH_SHORT).show()
    }

    private fun initRecyclerView(items: List<ImageItem>) {
        val groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
            addAll(items)
        }
        rv_images.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = groupAdapter
        }
    }

    private fun List<String>.toImageItems(): List<ImageItem> {
        return this.map {
            ImageItem(it, context!!)
        }
    }

}
