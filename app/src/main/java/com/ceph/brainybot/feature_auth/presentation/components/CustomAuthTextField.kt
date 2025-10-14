package com.ceph.brainybot.feature_auth.presentation.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun CustomAuthTextField(
    modifier: Modifier = Modifier,
    text: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    supportingText: @Composable () -> Unit,
    keyboardActions: KeyboardActions,
    keyboardOptions: KeyboardOptions,
    visualTransformation: VisualTransformation,
    trailingIcon: ImageVector,
    onIconClick: () -> Unit
) {
    TextField(
        value = text,
        onValueChange = onValueChange,
        placeholder = {
            Text(text = placeholder)
        },
        trailingIcon = {
            IconButton(
                onClick = onIconClick
            ) {
                Icon(
                    imageVector = trailingIcon,
                    contentDescription = placeholder
                )
            }

        },
        supportingText = supportingText,
        modifier = modifier,
        singleLine = true,
        shape = RoundedCornerShape(20.dp),
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
            unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.onPrimary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        visualTransformation = visualTransformation
    )
}

