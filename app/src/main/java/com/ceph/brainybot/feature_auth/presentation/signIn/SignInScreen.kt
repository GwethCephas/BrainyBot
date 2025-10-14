package com.ceph.brainybot.feature_auth.presentation.signIn

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.ceph.brainybot.R
import com.ceph.brainybot.feature_auth.presentation.components.CustomAuthTextField
import com.ceph.brainybot.feature_auth.presentation.viewModel.AuthViewModel
import com.ceph.brainybot.feature_chat.presentation.components.isEmailValid
import com.ceph.brainybot.feature_chat.presentation.components.isPasswordValid

@Composable
fun SignInScreen(
    viewModel: AuthViewModel,
    navigateToHome: () -> Unit,
    navigateToCreateUser: () -> Unit
) {


    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }


    val focusManager = LocalFocusManager.current

    val state by viewModel.state.collectAsState()

    LaunchedEffect(state) {
        if (state is SignInState.Success) {
            navigateToHome()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.ime)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (state is SignInState.IsLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )

            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Image(
                painter = painterResource(R.drawable.brainybot_dark),
                contentDescription = "BrainyBot image",
                modifier = Modifier
                    .height(200.dp)
                    .width(200.dp)
            )
        }



        Column(
            modifier = Modifier
                .weight(1f)
                .clip(
                    RoundedCornerShape(
                        topStart = 30.dp,
                        topEnd = 30.dp
                    )
                )
                .background(MaterialTheme.colorScheme.primary),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Spacer(Modifier.height(50.dp))

            CustomAuthTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                text = email,
                onValueChange = {
                    email = it
                    emailError = if (isEmailValid(email)) null else "Enter a valid Email Address"
                },
                supportingText = {
                    if (emailError != null) {
                        Text(
                            text = emailError!!,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                },
                onIconClick = {
                    focusManager.clearFocus()
                },
                placeholder = "Email",
                trailingIcon = Icons.Rounded.Email,
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                visualTransformation = VisualTransformation.None
            )

            Spacer(Modifier.height(20.dp))

            CustomAuthTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                text = password,
                onValueChange = {
                    password = it
                    passwordError = if (isPasswordValid(password)) null else "Please enter a valid Password"
                },
                supportingText = {
                    if (passwordError != null) {
                        Text(
                            text = passwordError!!,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                onIconClick = {
                    isPasswordVisible = !isPasswordVisible
                },
                placeholder = "Password",
                trailingIcon = if (isPasswordVisible) Icons.Default.VisibilityOff
                else Icons.Default.Visibility,
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                visualTransformation = if (isPasswordVisible) VisualTransformation.None
                else PasswordVisualTransformation()
            )
            Spacer(Modifier.height(20.dp))

            Button(
                modifier = Modifier
                    .width(220.dp)
                    .height(50.dp),
                onClick = {
                    viewModel.signInWithEmailAndPassword(email, password)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = Color.LightGray
                ),
                enabled = isEmailValid(email) && isPasswordValid(password)
            ) {
                Text(
                    text = "Sign In",
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    fontWeight = MaterialTheme.typography.bodyLarge.fontWeight

                )
            }
            Spacer(Modifier.height(10.dp))

            TextButton(
                onClick = {
                    navigateToCreateUser()
                }
            ) {
                Text(
                    text = "Don't have an account? Sign Up",
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                    color = Color.White
                )
            }
            Spacer(Modifier.height(10.dp))


            Button(
                modifier = Modifier
                    .height(50.dp),
                onClick = {
                    viewModel.signInWithGoogle()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.google_login_icon_light),
                    contentDescription = "Google Icon",
                    tint = Color.Unspecified
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "Sign in with Google",
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    fontWeight = MaterialTheme.typography.bodyLarge.fontWeight
                )
            }
        }

    }
}
