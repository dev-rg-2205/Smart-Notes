package com.test.snotes.item

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import androidx.core.graphics.toColorInt
import com.test.snotes.R
import com.test.snotes.dataModel.Note


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NotesCardPreview() {
    val navController = rememberNavController()
    val dummyTasks = ArrayList<Note>()


    for (i in 0..10) {
        dummyTasks.add(
            Note(
                id = "4058",
                title = "Daily Routine",
                description = "Walk Morning 7:00 AM.",
                reminderTime = "2000",
                userId = "30-06-2025",
                isAlarmOn = false,
            )
        )
    }


    LazyColumn {
        items(dummyTasks) { task ->
            NotesCard(
                title = task.title,
                description = task.description,
                reminderTime = task.reminderTime,
                isAlarmOn = task.isAlarmOn,
                onAlarmToggle = {

                },
                onEditClick = {

                },
                onDeleteClick = {

                }
            )
        }
    }

}


@Composable
fun NotesCard(
    title: String = "",
    description: String = "",
    reminderTime: String = "",
    isAlarmOn: Boolean = false,
    onAlarmToggle: (Boolean) -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {

    var isAlarmOn1 by remember { mutableStateOf(isAlarmOn) }
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color("#FFDA69".toColorInt())) // Light Yellow
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.semibold)),
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Switch(
                    checked = isAlarmOn1,
                    onCheckedChange = {
                        if (reminderTime.isNotEmpty()){
                            isAlarmOn1 = !isAlarmOn1
                            onAlarmToggle(isAlarmOn1)
                        }else{
                            Toast.makeText(context, "Please select remember time first ,then enable it.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFFFF4081)
                    )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = description,
                fontFamily = FontFamily(Font(R.font.medium)),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Remember : $reminderTime",
                fontFamily = FontFamily(Font(R.font.medium)),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row {
                Button(
                    onClick = onEditClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4081)),
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(text = "Edit")
                }
                Button(
                    onClick = onDeleteClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9A9A9A))
                ) {
                    Text(text = "Delete")
                }
            }
        }
    }
}
