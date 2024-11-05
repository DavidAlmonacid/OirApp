package com.example.oirapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.oirapp.R
import com.example.oirapp.ui.preview.DarkLightPreviews
import com.example.oirapp.ui.theme.MyApplicationTheme

@Composable
fun UserInfo(
    modifier: Modifier = Modifier,
    userImageUrl: String? = null,
    userName: String,
    userRole: String,
) {
    Row(modifier = modifier.fillMaxWidth()) {
        userImageUrl?.let { url ->
            Image(
                painter = rememberAsyncImagePainter(url),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(57.dp)
                    .clip(MaterialTheme.shapes.small),
            )
        } ?: Image(
            painter = painterResource(R.drawable.user_placeholder),
            contentDescription = null,
            modifier = Modifier
                .size(57.dp)
                .clip(MaterialTheme.shapes.small),
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = userName,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 4.dp),
            )

            Text(
                text = userRole,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.alpha(0.8f),
            )
        }
    }
}

@DarkLightPreviews
@Composable
private fun UserInfoPreview() {
    MyApplicationTheme {
        UserInfo(
            userName = "David",
            userRole = "Estudiante",
            modifier = Modifier.padding(16.dp),
        )
    }
}
