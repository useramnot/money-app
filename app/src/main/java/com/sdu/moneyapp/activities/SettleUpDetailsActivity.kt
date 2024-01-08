package com.sdu.moneyapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.sdu.moneyapp.R
import com.sdu.moneyapp.databases.*
import com.sdu.moneyapp.model.User

class SettleUpDetailsActivity : ComponentActivity() {
    private val currentUser : String = AuthManager.getCurrentUserUid()
    private val groupId: String by lazy { intent.getStringExtra("groupId") ?: "" }
    private val otherUser : String by lazy { intent.getStringExtra("participant") ?: "" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private fun onBackClick() = finish()

    private fun onSettleClick(amount: Double) {
        BalanceDatabase.settleAmountWithUser(groupId, currentUser, otherUser, amount) {
            Toast.makeText(this, "Settled up", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    @Preview
    @Composable
    fun MyComposeLayout() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Button(
                onClick = {onBackClick()},
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(top = 16.dp)
            ) {
                Text(text = stringResource(id = R.string.back_button))
            }
            var amountSettled by remember { mutableDoubleStateOf(0.0) }
            var amount by remember { mutableDoubleStateOf(0.0) }
            var name by remember { mutableStateOf("Another user") }
            UserDatabase.getUserById(otherUser) {
                name = it.name
            }
            BalanceDatabase.getOwingOnGroupWithUser(groupId, currentUser, otherUser) {
                amount = it
            }

            Text(text = "You owe $name $amount")
            TextField(
                value = amount.toString(),
                onValueChange = { amountSettled = it.toDouble() },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp),
                label = { Text(text = stringResource(id = R.string.enter_amount)) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
            )
            Button(
                onClick = { onSettleClick(amountSettled) },
                modifier = Modifier
                    .wrapContentWidth()
                    .align(Alignment.End)
            ) {
                Text(text = "Settle up")
            }
        }
    }
}
