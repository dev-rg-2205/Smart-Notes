package com.test.snotes.item

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.test.snotes.R


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CustomButtonPreview() {
    CustomButton(buttonText = "Click Me") {
        // Define what happens on click
    }
}


@Composable
fun CustomButton(buttonText: String, onClick: () -> Unit) {

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onClick() },
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.mainColor)
            )
        ) {
            Text(
                text = buttonText,
                fontSize = 17.sp,
                
                fontFamily = FontFamily(Font(R.font.semibold)),
                modifier = Modifier.padding(2.dp).height(30.dp),
                textAlign = TextAlign.Center
            )
        }
}
