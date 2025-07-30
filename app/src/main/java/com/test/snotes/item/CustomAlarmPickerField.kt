package com.test.snotes.item

import android.app.TimePickerDialog
import android.util.Log
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import java.text.SimpleDateFormat
import java.util.*

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CustomAlarmPickerFieldPreview() {
    CustomAlarmPickerField("Select Alarm" , "") {
        // handle final datetime string
    }
}

@Composable
fun CustomAlarmPickerField(
    label: String,
    preSelectedDateAndTime : String,
    onDateTimeSelected: (String) -> Unit
) {
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var selectedTime by remember { mutableStateOf<Pair<Int, Int>?>(null) }
    var showDateModal by remember { mutableStateOf(false) }
    var showTimeModal by remember { mutableStateOf(false) }

    LaunchedEffect(preSelectedDateAndTime) {
        if (preSelectedDateAndTime.isNotEmpty()) {
            try {
                val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
                val date = formatter.parse(preSelectedDateAndTime)
                if (date != null) {
                    val calendar = Calendar.getInstance().apply { time = date }
                    selectedDate = calendar.timeInMillis
                    selectedTime = calendar.get(Calendar.HOUR_OF_DAY) to calendar.get(Calendar.MINUTE)
                }
            } catch (e: Exception) {
                Log.e("CustomAlarmPicker", "Invalid preSelectedDateAndTime: $preSelectedDateAndTime", e)
            }
        }
    }

    val formattedDateTime = remember(selectedDate, selectedTime) {
        if (selectedDate != null && selectedTime != null) {
            val calendar = Calendar.getInstance().apply {
                timeInMillis = selectedDate!!
                set(Calendar.HOUR_OF_DAY, selectedTime!!.first)
                set(Calendar.MINUTE, selectedTime!!.second)
            }
            convertMillisToDateTime(calendar.timeInMillis)
        } else ""
    }


    OutlinedTextField(
        value = formattedDateTime,
        onValueChange = {},
        label = { Text(label) },
        placeholder = { Text("DD/MM/YYYY hh:mm a") },
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = "Select date & time")
        },
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(selectedDate) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showDateModal = true
                    }
                }
            }
    )

    if (showDateModal) {
        DatePickerModal(
            onDateSelected = {
                if (it != null) {
                    selectedDate = it
                    showDateModal = false
                    showTimeModal = true // immediately open time picker
                } else {
                    showDateModal = false
                }
            },
            onDismiss = { showDateModal = false }
        )
    }

    if (showTimeModal) {
        TimePickerModal(
            onTimeSelected = { hour, minute ->
                if (hour != null && minute != null) {
                    selectedTime = hour to minute
                    if (selectedDate != null) {
                        val calendar = Calendar.getInstance().apply {
                            timeInMillis = selectedDate!!
                            set(Calendar.HOUR_OF_DAY, hour)
                            set(Calendar.MINUTE, minute)
                        }
                        val formatted = convertMillisToDateTime(calendar.timeInMillis)
                        onDateTimeSelected(formatted)
                    }
                }
                showTimeModal = false
            },
            onDismiss = { showTimeModal = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDateSelected(null) }) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun TimePickerModal(
    onTimeSelected: (hour: Int?, minute: Int?) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    TimePickerDialog(
        context,
        { _, selectedHour, selectedMinute ->
            onTimeSelected(selectedHour, selectedMinute)
        },
        hour,
        minute,
        false
    ).apply {
        setOnCancelListener {
            onDismiss()
        }
    }.show()
}

fun convertMillisToDateTime(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
    return formatter.format(Date(millis))
}
