package com.test.snotes.item

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.test.snotes.R


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HeaderPreview() {
    val navController = rememberNavController()
    Header("Demo") {

    }
}


@Composable
fun Header(pageName: String, onClickable: () -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(colorResource(id = R.color.subColor)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Back Icon
            Box(
                modifier = Modifier
                    .size(50.dp) // Equal space for icon
                    .clickable(
                        indication = null, // Removes the ripple effect
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {
                            onClickable()
                        }),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.back_icon),
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(20.dp)
                )
            }

            // Centered Text
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = pageName,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(R.font.medium)),
                    color = colorResource(id = R.color.white),
                    fontSize = 20.sp
                )
            }

            // Right side spacer (to balance back icon)
            Box(
                modifier = Modifier
                    .size(50.dp) // Equal size as back icon
            )
        }
    }

}