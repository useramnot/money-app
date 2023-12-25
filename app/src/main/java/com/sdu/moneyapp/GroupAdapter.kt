package com.sdu.moneyapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.sdu.moneyapp.model.Group

class GroupAdapter(context: Context, groups: List<Group>) :
    ArrayAdapter<Group>(context, 0, groups) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_group, parent, false)
        }

        val currentGroup = getItem(position)

        val groupNameTextView: TextView = itemView!!.findViewById(R.id.textViewGroupName)
        val groupSummaryTextView: TextView = itemView.findViewById(R.id.textViewGroupSummary)

        groupNameTextView.text = currentGroup?.name
        // TODO
        // Implement your app's logic to calculate and display the group summary
        // This can involve calculating and formatting the summary based on your app's requirements
        groupSummaryTextView.text = "Summary: $0.00"

        return itemView
    }
}
