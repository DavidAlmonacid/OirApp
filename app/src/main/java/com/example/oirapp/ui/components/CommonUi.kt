package com.example.oirapp.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.oirapp.R

@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    @StringRes textId: Int,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondaryContainer),
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = stringResource(textId),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp,
        )
    }
}
//@DarkLightPreviews
//@Composable
//private fun PrimaryButtonPreview() {
//    MyApplicationTheme {
//        PrimaryButton(onClick = {}, textId = R.string.iniciar_sesion)
//    }
//}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes labelId: Int,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    readOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = stringResource(labelId), fontWeight = FontWeight.Medium) },
        shape = MaterialTheme.shapes.medium,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true,
        readOnly = readOnly,
        trailingIcon = trailingIcon,
        modifier = modifier
            .fillMaxWidth()
            .alpha(if (value.isEmpty()) 0.6f else 1f),
    )
}
//@CustomPreview
//@Composable
//private fun CustomTextFieldPreview() {
//    MyApplicationTheme {
//        CustomTextField(value = "", onValueChange = {}, labelId = R.string.email)
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectRoleDropdown(
    options: List<Int>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier,
    ) {
        CustomTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            labelId = R.string.select_role,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(
                type = MenuAnchorType.PrimaryNotEditable,
                enabled = true,
            ),
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { option ->
                val context = LocalContext.current

                DropdownMenuItem(
                    onClick = {
                        onOptionSelected(context.resources.getString(option))
                        expanded = false
                    },
                    text = { Text(text = stringResource(option), fontSize = 16.sp) },
                )
            }
        }
    }
}
//@CustomPreview
//@Composable
//private fun SelectRoleDropdownPreview() {
//    MyApplicationTheme {
//        SelectRoleDropdown(
//            options = listOf(R.string.rol_estudiante, R.string.rol_docente),
//            selectedOption = "",
//            onOptionSelected = {},
//        )
//    }
//}

@Composable
fun UserInfo(
    userName: String,
    userRole: String,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(R.drawable.user_placeholder),
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .clip(MaterialTheme.shapes.small),
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = userName,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 4.dp),
            )

            Text(
                text = userRole,
                fontSize = 14.sp,
                modifier = Modifier.alpha(0.8f),
            )
        }
    }
}
//@DarkLightPreviews
//@Composable
//private fun UserInfoPreview() {
//    MyApplicationTheme {
//        UserInfo(
//            userName = "David",
//            userRole = "Estudiante",
//            modifier = Modifier.padding(16.dp),
//        )
//    }
//}

@Composable
fun MenuCard(
    onCloseSession: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(20.dp),
        ) {
            Text(text = "Mi cuenta")

            Text(
                text = "Cerrar sesión",
                modifier = Modifier.clickable(onClick = onCloseSession),
            )
        }
    }
}
//@Preview
//@Composable
//private fun MenuCardPreview() {
//    MyApplicationTheme {
//        MenuCard()
//    }
//}
