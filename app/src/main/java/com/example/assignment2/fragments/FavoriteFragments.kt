package com.example.assignment2.fragments


import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.assignment2.R
import com.example.assignment2.adapter.FavAdapter
import com.example.assignment2.database.RestaurantDatabase
import com.example.assignment2.database.RestaurantEnitity

/**
 * A simple [Fragment] subclass.
 */
class FavoriteFragments : Fragment() {
    lateinit var favRecyler: RecyclerView
    lateinit var layoutmanager: RecyclerView.LayoutManager
    lateinit var favDapter: FavAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_favorite_fragments, container, false)
        favRecyler = view.findViewById(R.id.favRecyler)
        var favLists = RetriveFavourite(activity as Context).execute().get()
        favDapter = FavAdapter(activity as Context, favLists)
        layoutmanager = LinearLayoutManager(activity)
        favRecyler.layoutManager = layoutmanager
        favRecyler.adapter = favDapter
        return view
    }
}

class RetriveFavourite(val context: Context) : AsyncTask<Void, Void, List<RestaurantEnitity>>() {

    override fun doInBackground(vararg p0: Void?): List<RestaurantEnitity> {
        var db =
            Room.databaseBuilder(context, RestaurantDatabase::class.java, "restaurant-db").build()
        return db.restaurantDao().getRestaurantsFav()
    }

}
