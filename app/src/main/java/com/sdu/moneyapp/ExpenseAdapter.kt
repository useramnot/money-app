package com.sdu.moneyapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.sdu.moneyapp.model.Expense

class ExpenseAdapter(private val context: Context, private val expenses: List<Expense>) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return expenses.size
    }

    override fun getItem(position: Int): Any {
        return expenses[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        val holder: ViewHolder

        if (view == null) {
            view = inflater.inflate(R.layout.item_expense, parent, false)
            holder = ViewHolder()
            holder.textViewExpenseName = view.findViewById(R.id.textViewExpenseName)
            holder.textViewExpenseDetails = view.findViewById(R.id.textViewExpenseDetails)
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        val expense = getItem(position) as Expense
        holder.textViewExpenseName.text = expense.description

        // Format the participants and amount dynamically based on the actual data
        val participants = expense.participants.joinToString()
        val formattedDetails = context.getString(R.string.expense_details_placeholder, participants, expense.amount)
        holder.textViewExpenseDetails.text = formattedDetails

        return view!!
    }

    private class ViewHolder {
        lateinit var textViewExpenseName: TextView
        lateinit var textViewExpenseDetails: TextView
    }
}

