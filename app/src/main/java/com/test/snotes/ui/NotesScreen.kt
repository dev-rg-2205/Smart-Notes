package com.test.snotes.ui


import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.test.snotes.dataModel.Note
import com.test.snotes.item.GlobalLoaderOverlay
import com.test.snotes.item.NotesCard
import com.test.snotes.navigator.Router
import com.test.snotes.utils.AlarmScheduler
import com.test.snotes.viewModel.NotesViewModel

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NotesScreenPreview() {
    val navController = rememberNavController()
    NotesScreen(navController)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(navController: NavController) {
    val viewModel: NotesViewModel = viewModel()
    val notes by viewModel.notes.collectAsState()
    val context = LocalContext.current

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(context, "Please Allow Notification Permission! From App Info!", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED -> {
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    LaunchedEffect(true) { viewModel.loadNotes() }

    GlobalLoaderOverlay()


    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(Color(0xFF4CAF50)), // Material green
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Notes",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(
                    Router.AddEditNoteScreen(
                        id = "",
                        title = "",
                        description = "",
                        reminderTime = "",
                        userId = "",
                        isAlarmOn = false
                    )
                )
            }) {
                Text("+")
            }
        }
    ) {

        LazyColumn(
            Modifier
                .padding(it)
                .fillMaxSize()
        ) {

            items(notes) { task ->
                NotesCard(
                    title = task.title,
                    description = task.description,
                    reminderTime = task.reminderTime,
                    isAlarmOn = task.isAlarmOn,
                    onAlarmToggle = { it ->
                        task.isAlarmOn = it
                        if (task.isAlarmOn) {
                            Log.e("Alarm are schedule", "Start")
                            AlarmScheduler.scheduleAlarmFromFormattedDate(
                                context = context,
                                noteId = task.id,
                                formattedDateTime = task.reminderTime,
                                title = task.title,
                                description = task.description
                            )
                        } else {
                            Log.e("Alarm are schedule", "false")
                            AlarmScheduler.cancelAlarm(context, task.id)
                        }

                        viewModel.updateNote(
                            Note(
                                id = task.id,
                                title = task.title,
                                description = task.description,
                                reminderTime = task.reminderTime,
                                isAlarmOn = task.isAlarmOn,
                                userId = task.userId
                            )

                        )
                    },
                    onEditClick = {
                        navController.navigate(
                            Router.AddEditNoteScreen(
                                id = task.id,
                                title = task.title,
                                description = task.description,
                                reminderTime = task.reminderTime,
                                userId = task.userId,
                                isAlarmOn = task.isAlarmOn
                            )
                        )
                    },
                    onDeleteClick = {
                        AlarmScheduler.cancelAlarm(context, task.id)
                        viewModel.deleteNote(task.id)
                    }
                )
            }
        }

    }
}
