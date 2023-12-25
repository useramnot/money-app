package com.sdu.moneyapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView

class ParticipantsAdapter(
    context: Context,
    resource: Int,
    participants: List<String> = emptyList(),
    private val onRemoveParticipantClick: (String) -> Unit
) : ArrayAdapter<String>(context, resource, participants) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val participantUid = getItem(position)

        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_participant, parent, false)

        val textViewParticipantUid = view.findViewById<TextView>(R.id.textViewParticipantUid)
        val buttonRemoveParticipant = view.findViewById<Button>(R.id.buttonRemoveParticipant)

        textViewParticipantUid.text = participantUid

        // Set up the click listener for the "Remove from Group" button
        buttonRemoveParticipant.setOnClickListener {
            if (participantUid != null) {
                onRemoveParticipantClick(participantUid)
            }
        }

        return view
    }
}


