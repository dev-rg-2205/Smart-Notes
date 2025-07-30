package com.test.snotes.ui


import android.annotation.SuppressLint
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.test.snotes.R
import com.test.snotes.dataModel.Note
import com.test.snotes.item.CustomAlarmPickerField
import com.test.snotes.item.CustomButton
import com.test.snotes.item.CustomWithoutIconEditTextField
import com.test.snotes.item.GlobalLoaderOverlay
import com.test.snotes.item.Header
import com.test.snotes.navigator.Router
import com.test.snotes.utils.AlarmScheduler
import com.test.snotes.viewModel.NotesViewModel


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddEditNoteScreenPreview() {
    val navController = rememberNavController()
    AddEditNoteScreen(
        navController,
    )
}

@SuppressLint("ScheduleExactAlarm")
@Composable
fun AddEditNoteScreen(
    navController: NavController,
    id: String = "",
    title1: String = "",
    description: String = "",
    reminderTime: String = "",
    userId: String = "",
    isAlarmOn: Boolean = false
) {
    val viewModel: NotesViewModel = viewModel()
    var title by remember { mutableStateOf(title1) }
    var desc by remember { mutableStateOf(description) }
    var reminder by remember { mutableStateOf(reminderTime) }
    val context = LocalContext.current
    val currentUser = FirebaseAuth.getInstance().currentUser
    val currentUserID = currentUser!!.uid

    GlobalLoaderOverlay()

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {

        Header("Sales Service Form") {
            navController.navigate(Router.NotesScreen)
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 15.dp, vertical = 10.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {

            CustomWithoutIconEditTextField(
                value = title,
                onValueChange = { title = it },
                label = "Title",
                placeholder = "Title",
                keyboardType = KeyboardType.Text,
                enabled = true,
                showText = true,
            )

            Spacer(modifier = Modifier.height(5.dp))

            CustomWithoutIconEditTextField(
                value = desc,
                onValueChange = { desc = it },
                label = "Description",
                placeholder = "Description",
                keyboardType = KeyboardType.Text,
                enabled = true,
                showText = true,
            )

            Spacer(modifier = Modifier.height(5.dp))

            CustomAlarmPickerField("Select Alarm", reminderTime) {
                reminder = it
                Log.e("Selected Date Time is", it)
            }

            Spacer(modifier = Modifier.height(10.dp))

            CustomButton(buttonText = "Save") {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val alarmManager =
                        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    if (!alarmManager.canScheduleExactAlarms()) {
                        val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                        intent.data = "package:${context.packageName}".toUri()
                        context.startActivity(intent)
                    } else {

                        if (id.isNotEmpty()) {
                            if (isAlarmOn && reminder.isNotEmpty()) {
                                AlarmScheduler.cancelAlarm(context, id)
                                AlarmScheduler.scheduleAlarmFromFormattedDate(
                                    context = context,
                                    noteId = id,
                                    formattedDateTime = reminderTime,
                                    title = title,
                                    description = description
                                )

                            }
                            viewModel.updateNote(
                                Note(
                                    id = id,
                                    title = title,
                                    description = desc,
                                    reminderTime = reminder,
                                    userId = userId,
                                    isAlarmOn = isAlarmOn
                                )
                            )
                        } else {
                            viewModel.addNote(
                                Note(
                                    title = title,
                                    description = desc,
                                    reminderTime = reminder,
                                    isAlarmOn = !reminder.isEmpty(),
                                    userId = currentUserID
                                )
                            )
                        }

                        navController.navigate(Router.NotesScreen)
                    }
                } else {
                    if (id.isNotEmpty()) {
                        if (isAlarmOn && reminder.isNotEmpty()) {
                            AlarmScheduler.cancelAlarm(context, id)
                            AlarmScheduler.scheduleAlarmFromFormattedDate(
                                context = context,
                                noteId = id,
                                formattedDateTime = reminderTime,
                                title = title,
                                description = description
                            )

                        }

                        viewModel.updateNote(
                            Note(
                                id = id,
                                title = title,
                                description = desc,
                                reminderTime = reminder,
                                isAlarmOn = isAlarmOn,
                                userId = userId
                            )
                        )
                    } else {
                        viewModel.addNote(
                            Note(
                                title = title,
                                description = desc,
                                reminderTime = reminder,
                                isAlarmOn = !reminder.isEmpty(),
                                userId = currentUserID
                            )
                        )
                    }

                    navController.navigate(Router.NotesScreen)
                }
            }

        }

    }


}
