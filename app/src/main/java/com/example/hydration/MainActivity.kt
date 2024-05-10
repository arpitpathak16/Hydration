package com.example.hydration

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.hydration.ui.theme.HydrationTheme
import kotlinx.coroutines.delay
import java.text.DateFormat
import java.util.*
import kotlin.concurrent.timerTask
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import com.example.composecharts.components.GaugeChart
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.example.composecharts.components.BarChart
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    // Notification channel details
    private val CHANNEL_ID = "channelID"
    private val CHANNEL_NAME = "channelName"
    private val NOTIFICATION_ID = 1 // Unique notification ID

    private lateinit var beepMediaPlayer: MediaPlayer

    private val notificationTexts = listOf(
        "Remember to drink water!",
        "Stay hydrated!",
        "Don't forget to hydrate!",
        "Do you know?\n About 15.5 cups (3.7 liters) of fluids a day for men is a healthy amount.",
        "Do you know?\n About 11.5 cups (2.7 liters) of fluids a day for women is a healthy amount",
        "Water is the elixir of life. Drink up!", "Stay hydrated, stay healthy.",
        "Hydrate like it's your job.",
        "Water: nature's best kept secret for health.",
        "Drink water like it's your favorite beverage.",
        "Keep calm and drink water.",
        "Hydration is the key to vitality.",
        "Sip by sip, stay refreshed.",
        "Water: the original energy drink.",
        "Fuel your body with hydration.",
        "Don't forget to hydrate; your body will thank you.",
        "Hydration is the foundation of wellness.",
        "Water: the ultimate pick-me-up.",
        "Every drop counts; drink up!",
        "Listen to your body; it's thirsty for water.",
        "Hydration is the key to unlocking your full potential.",
        "Water: the magic potion for a healthier you.",
        "Stay hydrated, stay vibrant.",
        "One sip closer to a happier you.",
        "Hydration is self-care in liquid form.",
        "Quench your thirst, fuel your greatness.",
        "Water: the ultimate beauty elixir.",
        "Drink water and be happy.",
        "H2-Oh yeah! Keep those fluids flowing.",
        "A hydrated body is a happy body.",
        "Hydration is the cornerstone of well-being.",
        "Water: your body's best friend.",
        "Sip, hydrate, repeat.",
        "Stay hydrated and conquer the day.",
        "Your body is a temple; hydrate accordingly.",
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        beepMediaPlayer = MediaPlayer.create(this, R.raw.alarm)
//        val navController = rememberNavController()
        enableEdgeToEdge()
        setContent {
            HydrationTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "Home") {
                    composable("Home") {
                        HomeScreen(navController = navController)
                    }
                    composable("OtherScreen") {
                        OtherScreen(navController = navController)
                    }
                    composable("OtherScreen_week2") {
                        OtherScreen_week2(navController = navController)
                    }
                    composable("OtherScreen_week3") {
                        OtherScreen_week3(navController = navController)
                    }
                    composable("OtherScreen_week4") {
                        OtherScreen_week4(navController = navController)
                    }
                    composable("OtherScreen_month") {
                        OtherScreen_month(navController = navController)
                    }
                }
            }
        }

        val timer = Timer()
        timer.schedule(timerTask {
            val currentTime = Calendar.getInstance()
            val intervalMinutes = 1
            // Call showNotification with the interval and counter
            showNotification(intervalMinutes)
        }, 0, 2 * 60 * 60 * 1000)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotification(intervalMinutes: Int) {
        createNotificationChannel()

        val randomIndex = (0 until notificationTexts.size).random()
        val randomNotificationText = notificationTexts[randomIndex]

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Hydration Reminder")
            .setContentText(randomNotificationText)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(pendingIntent) // Set the pending intent
            .setAutoCancel(true)
            .setColorized(true)
            .setColor(ContextCompat.getColor(this, R.color.water))
            .build()

        val notificationManager = NotificationManagerCompat.from(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun getAppIntent(): PendingIntent {
        val intent = Intent(this, MainActivity::class.java)
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            enableLights(true)
        }
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    private fun navigateToApp() {
        // Handle navigation to app here
        Toast.makeText(this, "Navigating to app...", Toast.LENGTH_SHORT).show()
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(onGenderChanged: (Boolean) -> Unit) {
//        val blue = Color.BlueWater
    val context = LocalContext.current
    var gender by remember { mutableStateOf(false) }
    TopAppBar(
        title = {
            Text(text = "Hydration Remainder")
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Blue,
            titleContentColor = Color.White,
        ),
        navigationIcon = {
            IconButton(onClick = { Toast.makeText(context, "Hydration Remainder", Toast.LENGTH_SHORT).show()}) {
                Icon(painter = painterResource(id = R.drawable.bluebottle), contentDescription = "Hydration Remainder", tint = Color.White)

            }
        },
        actions = {
            Row {
                IconButton(onClick = { }) {
                    Icon(painter = painterResource(id = R.drawable.m), contentDescription = "Notification", tint = Color.White)

                }

            }
        }
    )
}

@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(modifier = Modifier.fillMaxWidth()) { innerPadding ->
        var gender by remember { mutableStateOf(false) }

        TopBar(
            onGenderChanged = { genderValue ->
                gender = genderValue
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(36.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Greeting(
                name = "Android",
                onNotificationClick = { /* Handle notification click */ },
                navController = navController
            )
        }
    }
}

    @Composable
    fun Greeting(
        name: String,
        modifier: Modifier = Modifier,
        onNotificationClick: () -> Unit,
        navController: NavController
    ) {
        val calendar = Calendar.getInstance()
        val formattedTime = remember { DateFormat.getTimeInstance().format(calendar.time) }
        val formattedDate = remember { DateFormat.getDateInstance().format(calendar.time) }

        val timeState = remember { mutableStateOf(formattedTime) }
        val dateState = remember { mutableStateOf(formattedDate) }

        var clickTimes by remember { mutableStateOf(emptyList<String>()) }
        val context = LocalContext.current

        val values = listOf(50f, 75f, 30f, 90f) // Sample values representing heights of bars
        val maxHeight = 200.dp // Maximum height of the chart


        LaunchedEffect(true) {
            while (true) {
                delay(1000)
                calendar.timeInMillis = System.currentTimeMillis()
                timeState.value = DateFormat.getTimeInstance().format(calendar.time)
                dateState.value = DateFormat.getDateInstance().format(calendar.time)
            }
        }

        val onClick: () -> Unit = {
            Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show()
            val currentTime = DateFormat.getTimeInstance().format(Calendar.getInstance().time)
            clickTimes = clickTimes + currentTime
        }

        var counter by remember { mutableStateOf(0) }
        var counter_goal by remember { mutableStateOf(0) }
        var countglass by remember { mutableStateOf(0) }
        val pValue = if (countglass * 7 > 100) 100 else countglass * 7

        var countglass_w by remember { mutableStateOf(0) }
        val pValue_w = if (countglass_w * 8 > 100) 100 else countglass_w * 8
        val showDialog = remember { mutableStateOf(false) }

        Column(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight(),
//        verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = modifier
                    .clickable(onClick = onClick)
                    .size(250.dp),
//                .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logowater),
                    contentDescription = "Stay Hydrated",
                    modifier = Modifier.size(250.dp)
                )
            }

            Text(
                text = "'Drink Water-Stay Hydrated'",
                style = MaterialTheme.typography.titleLarge.copy(fontStyle = FontStyle.Italic)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column {
                    Text(text = "Date:  ")
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .height(50.dp)
                            .width(120.dp)
//                    .wrapContentSize()
                            .border(2.dp, color = Color.Blue, shape = MaterialTheme.shapes.medium),
                        contentAlignment = Alignment.Center
                    )
                    {
                        Text(text = dateState.value)
                    }

                }
                Column {
                    Text(text = "Time:  ")
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .height(50.dp)
                            .width(120.dp)
//                    .wrapContentSize()
                            .border(2.dp, color = Color.Blue, shape = MaterialTheme.shapes.medium),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = timeState.value)
                    }


                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier.wrapContentSize(),
            ){
                Button(
                    onClick = { navController.navigate("OtherScreen") },
                    enabled = true
                ) {
                    Text(text = "Weekly Analysis")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = { navController.navigate("OtherScreen_month") },
                    enabled = true
                ) {Text(text = "Monthly Analysis")}
            }


            Spacer(modifier = Modifier.height(6.dp))
            Row {
                Column(
                    modifier = Modifier.wrapContentSize(),
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {

                        Column(
                            modifier = Modifier
                                .wrapContentHeight(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            GaugeChart(
                                modifier = Modifier.height(100.dp),
                                percentValue = pValue,
                                primaryColor = Color.Blue,
                                animated = true
                            )
                            Button(
                                onClick = {
                                    countglass++
                                    if (countglass == 14) {
                                        showDialog.value = true
                                    } else {
                                        Toast.makeText(context, "$countglass of glasses", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            ) {
                                Text(text = "Male")
                            }
                        }


                }
                if (showDialog.value) {
                    AlertDialog(
                        onDismissRequest = { showDialog.value = false },
                        title = {
                            Text(text = "Congratulations!")
                        },
                        text = {
                            Text(text = "You have reached 14 glasses of water! Drink more :)")
                        },
                        confirmButton = {
                            Button(
                                onClick = { showDialog.value = false }
                            ) {
                                Text("OK")
                            }
                        }
                    )
                }
                Spacer(modifier = Modifier.width(76.dp))

                    Column(
                        modifier = Modifier.wrapContentSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        GaugeChart(
                            modifier = Modifier.height(100.dp),
                            percentValue = pValue_w,
                            primaryColor = Color.Blue,
                            animated = true
                        )

                        Button(onClick = {
                            countglass_w++
                            Toast.makeText(context, "$countglass_w of glasses", Toast.LENGTH_SHORT)
                                .show()
                        }) {
                            Text(text = "Female")
                        }
                    }


            }

                Row {
                    TimeIntervalRow(
                        counter = counter,
                        onDecrement = { if (counter > 1) counter-- },
                        onIncrement = { if (counter < 4) counter++ },
                    )
                    Spacer(modifier = Modifier.width(26.dp))
                    SetGoalRow(
                        counter_goal = counter_goal,
                        onDecrement = { if (counter_goal > 1) counter_goal-- },
                        onIncrement = { if (counter_goal < 16) counter_goal++ },
                    )
                }




            Row {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(modifier = Modifier.height(16.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {


                    }

                }

            }

            Button(
                onClick = { onNotificationClick() },
                enabled = counter in 1..4
            ) {
                Text(text = "Confirm Count")
            }


        }

        }


@Composable
fun TimeIntervalRow(counter: Int, onDecrement: () -> Unit, onIncrement: () -> Unit) {
    Row(modifier = Modifier.wrapContentSize()) {


        Column(
//            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text(text = "Time Interval")
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .height(50.dp)
                    .width(150.dp)
                    .border(2.dp, color = Color.Magenta, shape = MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                Text(text = counter.toString(), color = Color.Red)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.wrapContentSize(),
//                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = onDecrement,
                    modifier = Modifier.wrapContentSize()
                ) {
                    Text(text = "-", style = TextStyle(fontSize = 25.sp))
                }

                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = onIncrement,
                    modifier = Modifier.wrapContentSize()
                ) {
                    Text(text = "+", style = TextStyle(fontSize = 25.sp))
                }
            }

        }
    }
}

@Composable
fun SetGoalRow(counter_goal: Int, onDecrement: () -> Unit, onIncrement: () -> Unit) {
    Row(modifier = Modifier.wrapContentSize()) {


        Column(
//            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text(text = "Set Goal")
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .height(50.dp)
                    .width(150.dp)
                    .border(2.dp, color = Color.Magenta, shape = MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                Text(text = counter_goal.toString(), color = Color.Red)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.wrapContentSize(),
//                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = onDecrement,
                    modifier = Modifier.wrapContentSize()
                ) {
                    Text(text = "-", style = TextStyle(fontSize = 25.sp))
                }

                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = onIncrement,
                    modifier = Modifier.wrapContentSize()
                ) {
                    Text(text = "+", style = TextStyle(fontSize = 25.sp))
                }
            }

        }
    }
}


@Composable
fun OtherScreen(navController: NavController) {

    val values = listOf(50f, 75f, 30f, 90f) // Sample values representing heights of bars
    val maxHeight = 200.dp // Maximum height of the chart
    val (isChartVisible, setChartVisible) = remember { mutableStateOf(false) }
    var inputValue1 by remember { mutableStateOf("") }
    var inputValue2 by remember { mutableStateOf("") }
    var inputValue3 by remember { mutableStateOf("") }
    var inputValue4 by remember { mutableStateOf("") }
    var inputValue5 by remember { mutableStateOf("") }
    var inputValue6 by remember { mutableStateOf("") }
    var inputValue7 by remember { mutableStateOf("") }

    val inputValues = listOf(
        inputValue1.toFloatOrNull() ?: 0f,
        inputValue2.toFloatOrNull() ?: 0f,
        inputValue3.toFloatOrNull() ?: 0f,
        inputValue4.toFloatOrNull() ?: 0f,
        inputValue5.toFloatOrNull() ?: 0f,
        inputValue6.toFloatOrNull() ?: 0f,
        inputValue7.toFloatOrNull() ?: 0f
    )
    val context = LocalContext.current
    val sum = (inputValue1.toIntOrNull() ?: 0) + (inputValue2.toIntOrNull() ?: 0) + (inputValue3.toIntOrNull() ?: 0) + (inputValue4.toIntOrNull() ?: 0) + (inputValue5.toIntOrNull() ?: 0) + (inputValue6.toIntOrNull() ?: 0) + (inputValue7.toIntOrNull() ?: 0)
    var showModal by remember { mutableStateOf(false) }
    if (sum >= 105) {
        Toast.makeText(context, "Congratulations! You have reached 105 glasses of water!", Toast.LENGTH_SHORT).show()
//        showModal = true
    }

//    if (showModal) {
//        AlertDialog(
//            onDismissRequest = { showModal = false },
//            title = {
//                Text(text = "Congratulations!")
//            },
//            text = {
//                Text(text = "You have drunk more water than the average person this week!")
//            },
//            confirmButton = {
//                Button(
//                    onClick = { showModal = false }
//                ) {
//                    Text("OK")
//                }
//            }
//        )
//    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        var gender by remember { mutableStateOf(false) }

        TopBar(
            onGenderChanged = { genderValue ->
                gender = genderValue
                // Handle gender change here if needed
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Week 1 ANalysis", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(70.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                TextField(
                    value = inputValue1,
                    onValueChange = { inputValue1 = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    placeholder = { Text("Day1") }
                )
            }
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(70.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                TextField(
                    value = inputValue2,
                    onValueChange = { inputValue2 = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    placeholder = { Text("Day2") }
                )
            }
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(70.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                TextField(
                    value = inputValue3,
                    onValueChange = { inputValue3 = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    placeholder = { Text("Day3") }
                )
            }
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(70.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                TextField(
                    value = inputValue4,
                    onValueChange = { inputValue4 = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    placeholder = { Text("Day4") }
                )
            }

        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(70.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                TextField(
                    value = inputValue5,
                    onValueChange = { inputValue5 = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    placeholder = { Text("Day5") }
                )
            }
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(70.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                TextField(
                    value = inputValue6,
                    onValueChange = { inputValue6 = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    placeholder = { Text("Day6") }
                )
            }
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(70.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                TextField(
                    value = inputValue7,
                    onValueChange = { inputValue7 = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    placeholder = { Text("Day7") }
                )
            }
        }
        Spacer(modifier = Modifier.height(26.dp))
        Button(onClick = { setChartVisible(!isChartVisible)}, modifier = Modifier
            .width(150.dp)
            .height(60.dp)) {
            Text(text = "Generate Graph")
        }

        Spacer(modifier = Modifier.height(26.dp))

        Text(
            text = "Weekly Water Intake(in glasses): $sum",
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.height(26.dp))
        if (isChartVisible) {
            BarChart(
                modifier = Modifier.size(300.dp),
                values = inputValues,
                maxHeight = maxHeight
            )
        }
        Spacer(modifier = Modifier.height(26.dp))
        Button(onClick = { navController.navigate("OtherScreen_week2")}, modifier = Modifier
            .width(150.dp)
            .height(60.dp)) {
            Text(text = "Week 2")
        }
    }

}


@Composable
fun OtherScreen_week2(navController: NavController)
{
    val values = listOf(50f, 75f, 30f, 90f) // Sample values representing heights of bars
    val maxHeight = 200.dp // Maximum height of the chart
    val (isChartVisible, setChartVisible) = remember { mutableStateOf(false) }
    var inputValue1 by remember { mutableStateOf("") }
    var inputValue2 by remember { mutableStateOf("") }
    var inputValue3 by remember { mutableStateOf("") }
    var inputValue4 by remember { mutableStateOf("") }
    var inputValue5 by remember { mutableStateOf("") }
    var inputValue6 by remember { mutableStateOf("") }
    var inputValue7 by remember { mutableStateOf("") }

    val inputValues = listOf(
        inputValue1.toFloatOrNull() ?: 0f,
        inputValue2.toFloatOrNull() ?: 0f,
        inputValue3.toFloatOrNull() ?: 0f,
        inputValue4.toFloatOrNull() ?: 0f,
        inputValue5.toFloatOrNull() ?: 0f,
        inputValue6.toFloatOrNull() ?: 0f,
        inputValue7.toFloatOrNull() ?: 0f
    )

    val sum = (inputValue1.toIntOrNull() ?: 0) + (inputValue2.toIntOrNull() ?: 0) + (inputValue3.toIntOrNull() ?: 0) + (inputValue4.toIntOrNull() ?: 0) + (inputValue5.toIntOrNull() ?: 0) + (inputValue6.toIntOrNull() ?: 0) + (inputValue7.toIntOrNull() ?: 0)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        var gender by remember { mutableStateOf(false) }

        TopBar(
            onGenderChanged = { genderValue ->
                gender = genderValue
                // Handle gender change here if needed
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Week 2 Analysis", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(70.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                TextField(
                    value = inputValue1,
                    onValueChange = { inputValue1 = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    placeholder = { Text("Day1") }
                )
            }
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(70.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                TextField(
                    value = inputValue2,
                    onValueChange = { inputValue2 = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    placeholder = { Text("Day2") }
                )
            }
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(70.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                TextField(
                    value = inputValue3,
                    onValueChange = { inputValue3 = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    placeholder = { Text("Day3") }
                )
            }
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(70.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                TextField(
                    value = inputValue4,
                    onValueChange = { inputValue4 = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    placeholder = { Text("Day4") }
                )
            }

        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(70.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                TextField(
                    value = inputValue5,
                    onValueChange = { inputValue5 = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    placeholder = { Text("Day5") }
                )
            }
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(70.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                TextField(
                    value = inputValue6,
                    onValueChange = { inputValue6 = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    placeholder = { Text("Day6") }
                )
            }
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(70.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                TextField(
                    value = inputValue7,
                    onValueChange = { inputValue7 = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    placeholder = { Text("Day7") }
                )
            }
        }
        Spacer(modifier = Modifier.height(26.dp))
        Button(onClick = { setChartVisible(!isChartVisible)}, modifier = Modifier
            .width(150.dp)
            .height(60.dp)) {
            Text(text = "Generate Graph")
        }

        Spacer(modifier = Modifier.height(26.dp))

        Text(
            text = "Weekly Water Intake(in glasses): $sum",
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.height(26.dp))
        if (isChartVisible) {
            BarChart(
                modifier = Modifier.size(300.dp),
                values = inputValues,
                maxHeight = maxHeight
            )
        }
        Spacer(modifier = Modifier.height(26.dp))
        Button(onClick = { navController.navigate("OtherScreen_week3")}, modifier = Modifier
            .width(150.dp)
            .height(60.dp)) {
            Text(text = "Week 3")
        }
    }

}


@Composable
fun OtherScreen_week3(navController: NavController){
    val values = listOf(50f, 75f, 30f, 90f) // Sample values representing heights of bars
    val maxHeight = 200.dp // Maximum height of the chart
    val (isChartVisible, setChartVisible) = remember { mutableStateOf(false) }
    var inputValue1 by remember { mutableStateOf("") }
    var inputValue2 by remember { mutableStateOf("") }
    var inputValue3 by remember { mutableStateOf("") }
    var inputValue4 by remember { mutableStateOf("") }
    var inputValue5 by remember { mutableStateOf("") }
    var inputValue6 by remember { mutableStateOf("") }
    var inputValue7 by remember { mutableStateOf("") }

    val inputValues = listOf(
        inputValue1.toFloatOrNull() ?: 0f,
        inputValue2.toFloatOrNull() ?: 0f,
        inputValue3.toFloatOrNull() ?: 0f,
        inputValue4.toFloatOrNull() ?: 0f,
        inputValue5.toFloatOrNull() ?: 0f,
        inputValue6.toFloatOrNull() ?: 0f,
        inputValue7.toFloatOrNull() ?: 0f
    )

    val sum = (inputValue1.toIntOrNull() ?: 0) + (inputValue2.toIntOrNull() ?: 0) + (inputValue3.toIntOrNull() ?: 0) + (inputValue4.toIntOrNull() ?: 0) + (inputValue5.toIntOrNull() ?: 0) + (inputValue6.toIntOrNull() ?: 0) + (inputValue7.toIntOrNull() ?: 0)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        var gender by remember { mutableStateOf(false) }

        TopBar(
            onGenderChanged = { genderValue ->
                gender = genderValue
                // Handle gender change here if needed
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Week 3 Analysis", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(70.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                TextField(
                    value = inputValue1,
                    onValueChange = { inputValue1 = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    placeholder = { Text("Day1") }
                )
            }
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(70.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                TextField(
                    value = inputValue2,
                    onValueChange = { inputValue2 = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    placeholder = { Text("Day2") }
                )
            }
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(70.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                TextField(
                    value = inputValue3,
                    onValueChange = { inputValue3 = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    placeholder = { Text("Day3") }
                )
            }
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(70.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                TextField(
                    value = inputValue4,
                    onValueChange = { inputValue4 = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    placeholder = { Text("Day4") }
                )
            }

        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(70.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                TextField(
                    value = inputValue5,
                    onValueChange = { inputValue5 = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    placeholder = { Text("Day5") }
                )
            }
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(70.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                TextField(
                    value = inputValue6,
                    onValueChange = { inputValue6 = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    placeholder = { Text("Day6") }
                )
            }
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(70.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                TextField(
                    value = inputValue7,
                    onValueChange = { inputValue7 = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    placeholder = { Text("Day7") }
                )
            }
        }
        Spacer(modifier = Modifier.height(26.dp))
        Button(onClick = { setChartVisible(!isChartVisible)}, modifier = Modifier
            .width(150.dp)
            .height(60.dp)) {
            Text(text = "Generate Graph")
        }

        Spacer(modifier = Modifier.height(26.dp))

        Text(
            text = "Weekly Water Intake(in glasses): $sum",
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.height(26.dp))
        if (isChartVisible) {
            BarChart(
                modifier = Modifier.size(300.dp),
                values = inputValues,
                maxHeight = maxHeight
            )
        }
        Spacer(modifier = Modifier.height(26.dp))
        Button(onClick = { navController.navigate("OtherScreen_week4")}, modifier = Modifier
            .width(150.dp)
            .height(60.dp)) {
            Text(text = "Week 4")
        }
    }

}

@Composable
fun OtherScreen_week4(navController: NavController){
    val values = listOf(50f, 75f, 30f, 90f) // Sample values representing heights of bars
    val maxHeight = 200.dp // Maximum height of the chart
    val (isChartVisible, setChartVisible) = remember { mutableStateOf(false) }
    var inputValue1 by remember { mutableStateOf("") }
    var inputValue2 by remember { mutableStateOf("") }
    var inputValue3 by remember { mutableStateOf("") }
    var inputValue4 by remember { mutableStateOf("") }
    var inputValue5 by remember { mutableStateOf("") }
    var inputValue6 by remember { mutableStateOf("") }
    var inputValue7 by remember { mutableStateOf("") }

    val inputValues = listOf(
        inputValue1.toFloatOrNull() ?: 0f,
        inputValue2.toFloatOrNull() ?: 0f,
        inputValue3.toFloatOrNull() ?: 0f,
        inputValue4.toFloatOrNull() ?: 0f,
        inputValue5.toFloatOrNull() ?: 0f,
        inputValue6.toFloatOrNull() ?: 0f,
        inputValue7.toFloatOrNull() ?: 0f
    )

    val sum = (inputValue1.toIntOrNull() ?: 0) + (inputValue2.toIntOrNull() ?: 0) + (inputValue3.toIntOrNull() ?: 0) + (inputValue4.toIntOrNull() ?: 0) + (inputValue5.toIntOrNull() ?: 0) + (inputValue6.toIntOrNull() ?: 0) + (inputValue7.toIntOrNull() ?: 0)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        var gender by remember { mutableStateOf(false) }

        TopBar(
            onGenderChanged = { genderValue ->
                gender = genderValue
                // Handle gender change here if needed
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Week 4 Analysis", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(70.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                TextField(
                    value = inputValue1,
                    onValueChange = { inputValue1 = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    placeholder = { Text("Day1") }
                )
            }
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(70.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                TextField(
                    value = inputValue2,
                    onValueChange = { inputValue2 = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    placeholder = { Text("Day2") }
                )
            }
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(70.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                TextField(
                    value = inputValue3,
                    onValueChange = { inputValue3 = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    placeholder = { Text("Day3") }
                )
            }
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(70.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                TextField(
                    value = inputValue4,
                    onValueChange = { inputValue4 = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    placeholder = { Text("Day4") }
                )
            }

        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(70.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                TextField(
                    value = inputValue5,
                    onValueChange = { inputValue5 = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    placeholder = { Text("Day5") }
                )
            }
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(70.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                TextField(
                    value = inputValue6,
                    onValueChange = { inputValue6 = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    placeholder = { Text("Day6") }
                )
            }
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(70.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                TextField(
                    value = inputValue7,
                    onValueChange = { inputValue7 = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    placeholder = { Text("Day7") }
                )
            }
        }
        Spacer(modifier = Modifier.height(26.dp))
        Button(onClick = { setChartVisible(!isChartVisible)}, modifier = Modifier
            .width(150.dp)
            .height(60.dp)) {
            Text(text = "Generate Graph")
        }

        Spacer(modifier = Modifier.height(26.dp))

        Text(
            text = "Weekly Water Intake(in glasses): $sum",
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.height(26.dp))
        if (isChartVisible) {
            BarChart(
                modifier = Modifier.size(300.dp),
                values = inputValues,
                maxHeight = maxHeight
            )
        }
        Spacer(modifier = Modifier.height(26.dp))
        Button(onClick = { navController.navigate("OtherScreen_month")}, modifier = Modifier
            .width(150.dp)
            .height(60.dp)) {
            Text(text = "Monthly")
        }
    }

}

@Composable
fun OtherScreen_month(navController: NavController){
    val values = listOf(50f, 75f, 30f, 90f) // Sample values representing heights of bars
    val maxHeight = 200.dp // Maximum height of the chart
    val (isChartVisible, setChartVisible) = remember { mutableStateOf(false) }
    var inputValue1 by remember { mutableStateOf("") }
    var inputValue2 by remember { mutableStateOf("") }
    var inputValue3 by remember { mutableStateOf("") }
    var inputValue4 by remember { mutableStateOf("") }

    val inputValues = listOf(
        inputValue1.toFloatOrNull() ?: 0f,
        inputValue2.toFloatOrNull() ?: 0f,
        inputValue3.toFloatOrNull() ?: 0f,
        inputValue4.toFloatOrNull() ?: 0f,
    )

    val sum = (inputValue1.toIntOrNull() ?: 0) + (inputValue2.toIntOrNull() ?: 0) + (inputValue3.toIntOrNull() ?: 0) + (inputValue4.toIntOrNull() ?: 0)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        var gender by remember { mutableStateOf(false) }

        TopBar(
            onGenderChanged = { genderValue ->
                gender = genderValue
                // Handle gender change here if needed
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Monthly Analysis", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(70.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                TextField(
                    value = inputValue1,
                    onValueChange = { inputValue1 = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    placeholder = { Text("Week1") }
                )
            }
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(70.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                TextField(
                    value = inputValue2,
                    onValueChange = { inputValue2 = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    placeholder = { Text("Week2") }
                )
            }
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(70.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                TextField(
                    value = inputValue3,
                    onValueChange = { inputValue3 = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    placeholder = { Text("Week3") }
                )
            }
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .height(70.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                TextField(
                    value = inputValue4,
                    onValueChange = { inputValue4 = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    placeholder = { Text("Week4") }
                )
            }

        }

        Spacer(modifier = Modifier.height(26.dp))
        Button(onClick = { setChartVisible(!isChartVisible)}, modifier = Modifier
            .width(150.dp)
            .height(60.dp)) {
            Text(text = "Generate Graph")
        }

        Spacer(modifier = Modifier.height(26.dp))

        Text(
            text = "Monthly Water Intake(in glasses): $sum",
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.height(26.dp))
        if (isChartVisible) {
            BarChart(
                modifier = Modifier.size(300.dp),
                values = inputValues,
                maxHeight = maxHeight
            )
        }
        Spacer(modifier = Modifier.height(26.dp))
        Button(onClick = { navController.navigate("Home")}, modifier = Modifier
            .width(150.dp)
            .height(60.dp)) {
            Text(text = "Home")
        }
    }
}

//@Preview(showBackground = true, device = "id:pixel_6")
//@Composable
//fun GreetingPreview() {
////    TopBar()
//    HydrationTheme {
//
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ){
//            Greeting(name = "Android", onNotificationClick = {}, navController = navController)
////            GaugeChart(percentValue = 25, primaryColor = Color.Magenta)
//        }
////        Greeting(name = "Android", onNotificationClick = {})
////        GaugeChart(percentValue = 25, primaryColor = Color.Magenta)
//    }
//}

@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun GreetingPreview() {
    HydrationTheme {
        val navController = rememberNavController()

        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Greeting(
                    name = "Android",
                    onNotificationClick = { /* Handle notification click */ },
                    navController = navController
                )
            }
        }
    }
}