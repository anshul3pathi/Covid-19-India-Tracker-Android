package com.example.covid19tracker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.covid19tracker.R
import com.example.covid19tracker.database.StateData

class StateWiseListAdapter(private val listener: StateWiseItemClicked): RecyclerView.Adapter<StateWiseViewHolder>() {

    private val items: ArrayList<StateData> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StateWiseViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.state_wise_item_view, parent, false)
        val viewHolder = StateWiseViewHolder(view)
        view.setOnClickListener {
            listener.onItemClicked(viewHolder)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: StateWiseViewHolder, position: Int) {
        val currentItem = items[position]
        holder.stateNameText.text = currentItem.stateName
        holder.confirmedNumberRV.text = currentItem.confirmed
        holder.activeNumberRV.text = currentItem.active
        holder.recoveredNumberRV.text = currentItem.recovered
        holder.deceasedNumberRV.text = currentItem.deceased

        holder.confirmedDeltaTextView.text = currentItem.confirmedDelta
        holder.activeDeltaTextView.text = currentItem.activeDelta
        holder.recoveredDeltaTextView.text = currentItem.recoveredDelta
        holder.deceasedDeltaTextView.text = currentItem.deceasedDelta
        holder.deltaLinearLayout.visibility = View.GONE
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
    val stateNameText: TextView = itemView.findViewById(R.id.state_name_textView)

    val confirmedNumberRV: TextView = itemView.findViewById(R.id.item_confirmed_number)
    val activeNumberRV: TextView = itemView.findViewById(R.id.item_active_number)
    val recoveredNumberRV: TextView = itemView.findViewById(R.id.item_recovered_number)
    val deceasedNumberRV: TextView = itemView.findViewById(R.id.item_deceased_number)

    val deltaLinearLayout: LinearLayout = itemView.findViewById(R.id.delta_numbers_linearLayout)
    val confirmedDeltaTextView: TextView = itemView.findViewById(R.id.item_confirmed_delta_number)
    val activeDeltaTextView: TextView = itemView.findViewById(R.id.item_active_delta_number)
    val recoveredDeltaTextView: TextView = itemView.findViewById(R.id.item_recovered_delta_number)
    val deceasedDeltaTextView: TextView = itemView.findViewById(R.id.item_deceased_delta_number)

    var deltaVisible = false
}

interface StateWiseItemClicked {
    fun onItemClicked(viewHolder: StateWiseViewHolder)
}