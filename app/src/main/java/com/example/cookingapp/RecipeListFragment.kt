package com.example.cookingapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecipeListFragment : Fragment() {
    val itemList = mutableListOf<Item>()
    private lateinit var adapter: MyAdapter
    private var dataSender: FragmentDataSender? = null
    private lateinit var sharedPreferences: SharedPreferences
    private val PREF_RECIPES = "recipes"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is FragmentDataSender){
            dataSender = context
        }
        else{
            throw RuntimeException("$context must implement FragmentDataSender")
        }
        sharedPreferences = context.getSharedPreferences("RecipesPrefs", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadRecipes()
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recipe_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.item_list_recipe)

        adapter = MyAdapter(itemList){item, _ ->
            val bundle = Bundle()
            bundle.putString("name", item.name)
            bundle.putString("diff", item.difficulty)
            bundle.putInt("time", item.time.toInt())
            bundle.putFloat("rating", item.rating)
            bundle.putString("desc", item.description)

            val recipeDetailsFragment = RecipeDetailsFragment()
            recipeDetailsFragment.arguments = bundle

            (requireActivity() as MainActivity).supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_panel, this, "recipeListFragmentTag")
                .addToBackStack(null)
                .commit()
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
    fun addItem(item: Item){
        itemList.add(item)
        saveRecipes()
        refreshData()
    }

    fun refreshData(){
        view?.findViewById<RecyclerView>(R.id.item_list_recipe)?.post{
            adapter.notifyDataSetChanged()
        }
    }

    fun updateItem(updatedItem: Item){
        val index = itemList.indexOfFirst { it.name === updatedItem.name }
        if(index != -1){
            itemList[index] = updatedItem
            saveRecipes()
            refreshData()
        }
    }

    fun removeItem(name: String?){
        itemList.removeAll {it.name == name}
        saveRecipes()
        refreshData()
    }

    fun filterRecipes(searchText: String, searchRating: Float){
        val filteredList = itemList.filter {item ->
            (item.name.contains(searchText, ignoreCase = true) ||
                    item.difficulty.contains(searchText, ignoreCase = true) ||
                    item.time.toString().contains(searchText, ignoreCase = true) ||
                    item.description.contains(searchText, ignoreCase = true)) &&
                    item.rating >= searchRating
        }

        Log.d("RecipeListFragment", "Filtered list size: ${filteredList.size}")
        filteredList.forEach { Log.d("RecipeListFragment", "Filtered item: ${it.name}") }

        adapter.updateData(filteredList.toList())
    }

    fun resetFilters(){
        loadRecipes()
        Log.d("RecipeListFragment", "Resetting filters, itemList size: ${itemList.size}")
        adapter.updateData(itemList.toList())
        adapter.notifyDataSetChanged()
    }

    fun loadRecipes(){
        Log.d("RecipeListFragment", "Loading recipes: ${itemList.size}")
        val json = sharedPreferences.getString(PREF_RECIPES, null)
        if(json != null) {
            val type = object : TypeToken<List<Item>>() {}.type
            itemList.clear()
            itemList.addAll(Gson().fromJson(json, type))
        }
        else{
            itemList.clear()
        }
    }

    private fun saveRecipes(){
        Log.d("RecipeListFragment", "Saving recipes: ${itemList.size}")
        val json = Gson().toJson(itemList)
        sharedPreferences.edit().putString(PREF_RECIPES, json).apply()
    }
}