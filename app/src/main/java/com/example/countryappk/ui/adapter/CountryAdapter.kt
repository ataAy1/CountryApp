package com.example.countryappk.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.countryappk.R
import com.example.countryappk.data.entity.Country
import com.example.countryappk.databinding.ItemCountryBinding
import com.example.countryappk.ui.fragment.FeedFragmentDirections
import com.example.countryappk.util.downloadFromUrl
import com.example.countryappk.util.placeholderProgressBar

class CountryAdapter(val countryList: ArrayList<Country>) :
    RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {


    inner class CountryViewHolder(val binding: ItemCountryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCountryBinding.inflate(inflater, parent, false)
        return CountryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = countryList[position]
        holder.binding.name.text = country.countryName
        holder.binding.region.text = country.countryRegion
        holder.binding.itemLayout.setOnClickListener {
            val action = FeedFragmentDirections.actionFeedFragmentToCountryFragment()
            action.countryUUid = country.uuid
            Navigation.findNavController(it).navigate(action)
        }

        holder.binding.imageView.downloadFromUrl(
            country.imageUrl,
            placeholderProgressBar(holder.binding.root.context)
        )
    }

    fun updateCountryList(newCountryList: List<Country>) {
        countryList.clear()
        countryList.addAll(newCountryList)
        notifyDataSetChanged()
    }

}