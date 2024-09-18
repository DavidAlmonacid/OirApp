package com.example.oirapp.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.example.oirapp.ui.preview.CustomPreview
import com.example.oirapp.ui.theme.MyApplicationTheme
import com.example.oirapp.utils.removeUppercaseAccents

@Composable
fun CustomButton(
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

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes labelId: Int,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    readOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = stringResource(labelId), fontWeight = FontWeight.Medium) },
        shape = MaterialTheme.shapes.medium,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
        ),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        singleLine = true,
        readOnly = readOnly,
        trailingIcon = trailingIcon,
        modifier = modifier.fillMaxWidth(),
    )
}

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

@Composable
fun GroupCard(
    groupName: String,
    groupCode: String,
    role: String,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(68.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape,
                    ),
            ) {
                Text(
                    text = groupName.take(3).uppercase().removeUppercaseAccents(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Medium,
                )
            }

            Column {
                Text(
                    text = groupName,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.fillMaxWidth(),
                )

                if (role == "Docente") {
                    Text(
                        text = groupCode,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }
            }
        }
    }
}

// Previews
//@DarkLightPreviews
//@Composable
//private fun CustomButtonPreview() {
//    MyApplicationTheme {
//        CustomButton(onClick = {}, textId = R.string.iniciar_sesion)
//    }
//}

//@CustomPreview
//@Composable
//private fun CustomTextFieldPreview() {
//    MyApplicationTheme {
//        CustomTextField(value = "", onValueChange = {}, labelId = R.string.email)
//    }
//}

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

//@CustomPreview
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

@CustomPreview
@Composable
private fun GroupCardDocentePreview() {
    MyApplicationTheme {
        GroupCard(
            groupName = "Cálculo Diferencial",
            groupCode = "123456",
            role = "Docente",
            modifier = Modifier.padding(16.dp),
        )
    }
}

@CustomPreview
@Composable
private fun GroupCardEstudiantePreview() {
    MyApplicationTheme {
        GroupCard(
            groupName = "Inglés IV",
            groupCode = "123456",
            role = "Estudiante",
            modifier = Modifier.padding(16.dp),
        )
    }
}
