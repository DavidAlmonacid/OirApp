package com.example.oirapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.oirapp.R
import com.example.oirapp.ui.preview.CustomPreview
import com.example.oirapp.ui.theme.MyApplicationTheme
import com.example.oirapp.ui.viewmodel.PresenceState

@Composable
fun UsersList(
    modifier: Modifier = Modifier,
    users: List<PresenceState>,
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        modifier = modifier
            .widthIn(max = 300.dp)
            .fillMaxWidth(),
    ) {
        Text(
            text = stringResource(R.string.usuarios_conectados),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 20.dp, bottom = 8.dp, start = 20.dp),
        )

        LazyColumn(modifier = Modifier.padding(20.dp)) {
            items(
                items = users,
                key = { user -> user.id },
            ) { user ->
                UserItem(user = user)
            }
        }
    }
}

@Composable
private fun UserItem(
    modifier: Modifier = Modifier,
    user: PresenceState,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f),
        ) {
            // User avatar
            user.imageUrl?.let { url ->
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(url).crossfade(true)
                        .build(),
                    contentDescription = stringResource(R.string.imagen_de_perfil),
                    placeholder = painterResource(R.drawable.user_placeholder),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape),
                )
            } ?: Image(
                painter = painterResource(R.drawable.user_placeholder),
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape),
            )

            // User name
            Text(
                text = user.userName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
        }

        // User status
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color = Color.Green, shape = CircleShape),
        )
    }
}

@CustomPreview
@Composable
private fun UsersListPreview() {
    MyApplicationTheme {
        UsersList(
            users = listOf(
                PresenceState(id = "1", imageUrl = null, userName = "Perengano Perez Rodriguez"),
                PresenceState(id = "2", imageUrl = null, userName = "Fulanito De Tal"),
                PresenceState(id = "3", imageUrl = null, userName = "Menganito De Cual"),
                PresenceState(id = "4", imageUrl = null, userName = "Zutanito De Aquel"),
            )
        )
    }
}
