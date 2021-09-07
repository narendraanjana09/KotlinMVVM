package com.nsa.navigation.ui

import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nsa.navigation.R
import com.nsa.navigation.adapters.MovieAdapter
import com.nsa.navigation.models.RecyclerList
import com.nsa.navigation.viewmodel.MainActivityViewModel
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentA.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentA : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_a, container, false)
        view.findViewById<Button>(R.id.button).setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.actionAtoB)


        }

        initViews(view)
        if (checkForInternet(this.requireContext())) {
            initViewModel()
        } else {
            Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show()
        }

        return view;

    }
    private fun checkForInternet(context: Context): Boolean {

        // register activity with the connectivity manager service
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Returns a Network object corresponding to
            // the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false

            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                // else return false
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }


    private lateinit var recyclrAdapter: MovieAdapter
    lateinit var recyclerView:RecyclerView


    private fun initViews(view: View) {
         recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.layoutManager=GridLayoutManager(context,4)
        } else {
            recyclerView.layoutManager=GridLayoutManager(context,2)
        }

        recyclrAdapter= MovieAdapter()
        recyclerView.adapter=recyclrAdapter

         refreshLayout =view.findViewById<SwipyRefreshLayout>(R.id.refreshLayout)

        refreshLayout.setOnRefreshListener {
            if (checkForInternet(this.requireContext())) {
                if(!this::viewModel.isInitialized){
                    initViewModel()
                }else{
                    viewModel.refresh()
                }
            } else {
                refreshLayout.isRefreshing=false
                Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show()
            }

        }


    }
    private lateinit var refreshLayout:SwipyRefreshLayout
    private lateinit var viewModel :MainActivityViewModel
    private fun initViewModel() {
       viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        viewModel.getRecyclerListObserver().observe(viewLifecycleOwner, Observer<RecyclerList> {

            if(it != null){

                recyclrAdapter.setUpdatedData(it.Search)
                refreshLayout.isRefreshing=false


                recyclerView.smoothScrollToPosition(recyclrAdapter.itemCount-9)
            }else{
                refreshLayout.isRefreshing=false
                Toast.makeText(activity,"Error In Getting Data",Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.makeApiCall()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentA.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentA().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}