package com.example.covid19tracker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.covid19tracker.R
import com.example.covid19tracker.database.StateData

class StateWiseListAdapter(): RecyclerView.Adapter<StateWiseViewHolder>() {

    private val items: ArrayList<StateData> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StateWiseViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.state_wise_item_view, parent, false)
        return StateWiseViewHolder(view)
    }

    override fun onBindViewHolder(holder: StateWiseViewHolder, position: Int) {
        val currentItem = items[position]
        holder.stateNameText.text = currentItem.stateName
        holder.confirmedNumberRV.text = currentItem.confirmed
        holder.activeNumberRV.text = currentItem.active
        holder.recoveredNumberRV.text = currentItem.recovered
        holder.deceasedNumberRV.text = currentItem.deceased
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateCovidData(stateWiseData: List<StateData>) {
        items.clear()
        items.addAll(stateWiseData)
        notifyDataSetChanged()
    }

}

class StateWiseViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val stateNameText: TextView = itemView.findViewById(R.id.stateNameTextView)
    val confirmedNumberRV: TextView = itemView.findViewById(R.id.confirmedNumberRV)
    val activeNumberRV: TextView = itemView.findViewById(R.id.activeNumberRV)
    val recoveredNumberRV: TextView = itemView.findViewById(R.id.recoveredNumberRV)
    val deceasedNumberRV: TextView = itemView.findViewById(R.id.deceasedNumberRV)
}