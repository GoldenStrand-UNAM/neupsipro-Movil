package com.example.neupsipromovil.presentation.common.molecules

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.neupsipromovil.presentation.common.atoms.AppIcon
import com.example.neupsipromovil.presentation.common.atoms.AppText
import com.example.neupsipromovil.presentation.common.atoms.IconSize
import com.example.neupsipromovil.presentation.theme.AppTypography
import com.example.neupsipromovil.presentation.theme.Black
import com.example.neupsipromovil.presentation.theme.Blue01
import com.example.neupsipromovil.presentation.theme.Grey

@Suppress("ktlint:standard:function-naming")
@Composable
fun InputTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    Column(
        modifier =
            modifier
                .widthIn(max = 306.dp)
                .height(86.dp),
    ) {
        AppText(text = label, style = AppTypography.labelBase, color = Black)
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                AppText(text = placeholder, style = AppTypography.bodyBase, color = Grey)
            },
            leadingIcon = {
                AppIcon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    size = IconSize.Medium,
                    tint = Grey,
                )
            },
            singleLine = true,
            isError = isError,
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape = RoundedCornerShape(8.dp),
            colors =
                OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Blue01,
                    unfocusedBorderColor = Grey.copy(alpha = 0.3f),
                ),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp),
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
private fun InputTextFieldPreview() {
    InputTextField(
        label = "Usuario",
        value = "",
        onValueChange = {},
        placeholder = "Usuario",
        leadingIcon = Icons.Default.Person,
    )
}
