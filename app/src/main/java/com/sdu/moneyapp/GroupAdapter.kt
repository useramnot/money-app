package com.sdu.moneyapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.sdu.moneyapp.model.Group

class GroupAdapter(context: Context, private var groups: List<Group>) :
    ArrayAdapter<Group>(context, 0, groups) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_group, parent, false)
        }

        val currentGroup = getItem(position)

        val groupNameTextView: TextView = itemView!!.findViewById(R.id.textViewGroupName)
        val groupSummaryTextView: TextView = itemView.findViewById(R.id.textViewGroupSummary)

        // Update the TextViews with actual data or placeholders
        groupNameTextView.text = currentGroup?.name ?: context.getString(R.string.group_name_placeholder)
        groupSummaryTextView.text = calculateGroupSummary(currentGroup)

        return itemView
    }

    private fun calculateGroupSummary(group: Group?): String {
        // TODO: Implement your app's logic to calculate and format the group summary
        // For example, you can calculate the total expenses and display it as "$X.XX"
        return context.getString(R.string.group_summary_placeholder)
    }

    fun updateGroups(newGroups: List<Group>) {
        groups = newGroups
        notifyDataSetChanged()
    }
}


