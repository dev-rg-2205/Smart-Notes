package com.test.snotes.item

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.test.snotes.R


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CustomEditTextFieldPreview() {
    var userEmail by remember { mutableStateOf("") }
    val navController = rememberNavController()
    CustomEditTextField(
        value = userEmail,
        onValueChange = { userEmail = it },
        label = "Password",
        placeholder = "Enter Your Password",
        iconRes = R.drawable.password_icon,
        colorRes = R.color.editFieldColor,
        keyboardType = KeyboardType.Password,
        enabled = true,
        showText = true,
    )
}

@Composable
fun CustomEditTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    @DrawableRes iconRes: Int,
    @ColorRes colorRes: Int,
    keyboardType: KeyboardType,
    enabled: Boolean,
    showText : Boolean
) {

    var passwordVisible by remember { mutableStateOf(showText) }




    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = colorResource(id = colorRes)) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(5.dp),
        enabled = enabled,
        placeholder = { Text(placeholder, color = Color.Gray) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        textStyle = LocalTextStyle.current.copy(color = Color.Black),
        leadingIcon = { Image(painter  =  painterResource(id = iconRes), contentDescription = "$label Icon") },
        trailingIcon = {
            if (keyboardType == KeyboardType.Password) {
                val image = if (passwordVisible)
                    painterResource(id = R.drawable.eye_hide_icon)
                else
                    painterResource(id = R.drawable.eye_show_icon)

                IconButton(onClick = {
                    passwordVisible = !passwordVisible
                }
                ) {
                    Image(painter = image, contentDescription = if (passwordVisible) "Hide password" else "Show password" , modifier = Modifier.size(18.dp))
                }
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colorResource(id = colorRes),
            unfocusedBorderColor = colorResource(id = colorRes),
            focusedLeadingIconColor = colorResource(id = colorRes),
            unfocusedLeadingIconColor = colorResource(id = colorRes),
            cursorColor = colorResource(id = colorRes),
            focusedLabelColor = colorResource(id = colorRes),
            unfocusedLabelColor = colorResource(id = colorRes),
            disabledTextColor = Color.Gray,
            disabledBorderColor = colorResource(id = colorRes),
            disabledLeadingIconColor = colorResource(id = colorRes),
            disabledLabelColor = colorResource(id = colorRes),
            disabledPlaceholderColor = colorResource(id = colorRes)
        ),
        visualTransformation = if (keyboardType == KeyboardType.Password && !passwordVisible)
            PasswordVisualTransformation()
        else
            VisualTransformation.None
    )
}
