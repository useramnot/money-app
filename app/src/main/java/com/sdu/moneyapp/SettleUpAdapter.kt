package com.sdu.moneyapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView

class SettleUpAdapter(
    context: Context,
    resource: Int,
    private val onSettleUpClick: (String, Boolean) -> Unit
) : ArrayAdapter<String>(context, resource) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val participantUid = getItem(position)

        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_settle_up_participant, parent, false)

        val textViewParticipantUid = view.findViewById<TextView>(R.id.textViewSettleUpParticipantUid)
        val buttonSettleUp = view.findViewById<Button>(R.id.buttonSettleUp)
        val buttonRemind = view.findViewById<Button>(R.id.buttonRemind)
        val textViewSettledUp = view.findViewById<TextView>(R.id.textViewSettledUp)

        textViewParticipantUid.text = participantUid

        // TODO: Implement logic to determine if the user is settled up or owed
        val isOwed = true
        if (isOwed) {
            // If owed, show "Remind" button
            buttonRemind.visibility = View.VISIBLE
            buttonRemind.setOnClickListener {
                if (participantUid != null) {
                    onSettleUpClick(participantUid, isOwed)
                }
            }
            buttonSettleUp.visibility = View.GONE
            textViewSettledUp.visibility = View.GONE
        } else {
            // If settled up, show "You are settled up" text
            buttonSettleUp.visibility = View.GONE
            buttonRemind.visibility = View.GONE
            textViewSettledUp.visibility = View.VISIBLE
        }

        return view
    }
}
