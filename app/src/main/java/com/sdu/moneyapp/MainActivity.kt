package com.sdu.moneyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.sdu.moneyapp.ui.theme.MoneyAppTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        FirebaseMessaging.getInstance().isAutoInitEnabled = true

        val authManager = FirebaseAuthenticationManager
        val databaseManager = FirebaseDatabaseManager
        val currentUserUid = authManager.getCurrentUserUid()

        setContent {
            MoneyAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting()
                }
            }
        }
    }
}

@Composable
fun Greeting() {
    Text(
        text = "Billy"
    )
}

@Composable
fun Home(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .requiredWidth(width = 360.dp)
            .requiredHeight(height = 800.dp)
            .background(color = Color.White)
    ) {
        GroupSituation(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 21.dp,
                    y = 447.dp))
        GroupSituation(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 21.dp,
                    y = 591.dp))
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 21.dp,
                    y = 303.dp)
                .requiredWidth(width = 318.dp)
                .requiredHeight(height = 62.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape = RoundedCornerShape(25.dp))
                    .background(color = Color(0xffd9d9d9)))
            Profile()
            Text(
                text = "Create a group",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 20.sp),
                modifier = Modifier
                    .fillMaxSize())
        }
        GroupSituation(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 21.dp,
                    y = 519.dp))
        GroupSituation(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 21.dp,
                    y = 519.dp))
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 21.dp,
                    y = 375.dp)
                .requiredWidth(width = 318.dp)
                .requiredHeight(height = 62.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape = RoundedCornerShape(25.dp))
                    .background(color = Color(0xffd9d9d9)))
            Profile()
            Text(
                text = "The boys",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 20.sp),
                modifier = Modifier
                    .fillMaxSize())
            Text(
                text = "53 DKK",
                color = Color.Black,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 16.sp),
                modifier = Modifier
                    .fillMaxSize())
            Text(
                text = "You lent",
                color = Color.Black,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 11.sp),
                modifier = Modifier
                    .fillMaxSize())
        }
        Text(
            text = "Groups",
            color = Color.Black,
            style = TextStyle(
                fontSize = 20.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 37.dp,
                    y = 218.dp)
                .requiredWidth(width = 258.dp)
                .requiredHeight(height = 26.dp))
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 21.dp,
                    y = 83.dp)
                .requiredWidth(width = 318.dp)
                .requiredHeight(height = 102.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape = RoundedCornerShape(25.dp))
                    .background(color = Color(0xffd9d9d9)))
            Text(
                text = "Overall you owe",
                color = Color.Black,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 20.sp),
                modifier = Modifier
                    .fillMaxSize())
            Text(
                text = "69 DKK",
                color = Color.Black,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .fillMaxSize())
        }
        SearchBar(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 21.dp,
                    y = 251.dp))
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 26.dp,
                    y = 24.dp)
                .requiredWidth(width = 41.dp)
                .requiredHeight(height = 44.dp))
        Text(
            text = "Billy",
            color = Color.Black,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 0.dp,
                    y = 24.dp)
                .requiredWidth(width = 360.dp)
                .requiredHeight(height = 44.dp)
                .wrapContentHeight(align = Alignment.CenterVertically))
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 108.dp,
                    y = 725.dp)
                .requiredWidth(width = 360.dp)
                .requiredHeight(height = 75.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(0xffbfbfbf)))
            Text(
                text = "Add Expense",
                color = Color.Black,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 20.sp),
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight(align = Alignment.CenterVertically))
        }
    }
}

@Composable
fun GroupSituation(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .requiredWidth(width = 318.dp)
            .requiredHeight(height = 62.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(shape = RoundedCornerShape(25.dp))
                .background(color = Color(0xffd9d9d9)))
        Profile()
        Text(
            text = "House",
            color = Color.Black,
            style = TextStyle(
                fontSize = 20.sp),
            modifier = Modifier
                .fillMaxSize())
        Text(
            text = "108 DKK",
            color = Color.Black,
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 16.sp),
            modifier = Modifier
                .fillMaxSize())
        Text(
            text = "You lent",
            color = Color.Black,
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 11.sp),
            modifier = Modifier
                .fillMaxSize())
    }
}

@Composable
fun Profile(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 9.dp,
                end = 265.dp,
                top = 9.dp,
                bottom = 9.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(shape = CircleShape)
                .background(color = Color(0xfff8f5f5)))
        Text(
            text = "👩‍💼",
            color = Color.Black,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .fillMaxSize())
    }
}

@Composable
fun SearchBar(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .requiredWidth(width = 318.dp)
            .requiredHeight(height = 44.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(shape = RoundedCornerShape(25.dp))
                .background(color = Color(0xffd9d9d9)))
        Text(
            text = "🔍 Search",
            color = Color.Black,
            style = TextStyle(
                fontSize = 20.sp),
            modifier = Modifier
                .fillMaxSize())
    }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun HomePreview() {
    Home(Modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MoneyAppTheme {
        Greeting()
    }
}