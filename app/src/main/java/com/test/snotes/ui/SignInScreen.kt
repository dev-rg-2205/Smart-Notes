package com.test.snotes.ui

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.test.snotes.item.GlobalLoaderOverlay
import com.test.snotes.item.CustomButton
import com.test.snotes.item.CustomEditTextField
import com.test.snotes.R
import com.test.snotes.item.LogoSection
import com.test.snotes.navigator.Router
import com.test.snotes.utils.LoaderState

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignInScreenPreview() {
    val navController = rememberNavController()
    SignInScreen(navController)
}


@Composable
fun SignInScreen(navController: NavHostController) {

    val context = LocalContext.current
    var userEmail by remember { mutableStateOf("") }
    var userPassword by remember { mutableStateOf("") }

    BackHandler {
        (context as Activity).finish()
    }



    GlobalLoaderOverlay()


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.bgColor))
            .padding(15.dp)
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        ) {

            Spacer(modifier = Modifier.height(50.dp))

            LogoSection()

            Spacer(modifier = Modifier.height(20.dp))


            Text(
                text = stringResource(R.string.sign_in),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.semibold)),
                color = colorResource(R.color.mainColor),
                fontSize = 30.sp
            )

            Spacer(modifier = Modifier.height(10.dp))


            Text(
                text = stringResource(R.string.email),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Left,
                fontFamily = FontFamily(Font(R.font.semibold)),
                color = colorResource(R.color.black),
                fontSize = 16.sp
            )

            CustomEditTextField(
                value = userEmail,
                onValueChange = { userEmail = it },
                label = stringResource(R.string.email),
                placeholder = stringResource(R.string.enter_your_email),
                iconRes = R.drawable.email_icon,
                colorRes = R.color.editFieldColor,
                keyboardType = KeyboardType.Email,
                enabled = true,
                showText = true,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.password),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Left,
                fontFamily = FontFamily(Font(R.font.semibold)),
                color = colorResource(R.color.black),
                fontSize = 16.sp
            )

            CustomEditTextField(
                value = userPassword,
                onValueChange = { userPassword = it },
                label = stringResource(R.string.password),
                placeholder = stringResource(R.string.enter_your_password),
                iconRes = R.drawable.password_icon,
                colorRes = R.color.editFieldColor,
                keyboardType = KeyboardType.Password,
                enabled = true,
                showText = false,
            )


            Spacer(modifier = Modifier.height(35.dp))

            CustomButton(buttonText = stringResource(R.string.sign_in)) {
                if (userPassword.length >= 6) {
                    LoaderState.show()

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(userEmail, userPassword)
                        .addOnSuccessListener {
                            LoaderState.hide()
                            navController.navigate(Router.NotesScreen)
                        }
                        .addOnFailureListener { signInException ->

                            FirebaseAuth.getInstance().createUserWithEmailAndPassword(userEmail, userPassword)
                                .addOnSuccessListener {
                                    LoaderState.hide()
                                    navController.navigate(Router.NotesScreen)
                                }
                                .addOnFailureListener { createException ->
                                    LoaderState.hide()

                                    val errorMessage = when {
                                        createException.message?.contains("email address is badly formatted", ignoreCase = true) == true ->
                                            "Invalid email format."
                                        createException.message?.contains("already in use", ignoreCase = true) == true ->
                                            "Password is incorrect."
                                        createException.message?.contains("weak-password", ignoreCase = true) == true ->
                                            "Password is too weak. Use at least 6 characters."
                                        else -> createException.localizedMessage ?: "Authentication failed."
                                    }

                                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                                }
                        }

                } else {
                    Toast.makeText(context, "Password must be at least 6 characters.", Toast.LENGTH_SHORT).show()
                }
            }


        }
    }


}