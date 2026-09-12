package com.example.senti_ma.ui.shared

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.example.senti_ma.R

/**
 * Top app bar for the forum screen with a clickable circular avatar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    onAvatarClick: () -> Unit,
    onHomeClick: () -> Unit,
    avatarUrl: Uri?
){
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = { onHomeClick() }) {
                Icon(Icons.Default.Home, contentDescription = stringResource(R.string.description_home_icon))
            }
        },
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = typography.headlineSmall
            )
        },
        actions = {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable { onAvatarClick() }
            ) {
                Image(
                    painter = rememberAsyncImagePainter(avatarUrl?: R.drawable.default_avatar),
                    contentDescription = stringResource(R.string.description_avatar_image),
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            }
        },
        modifier = modifier
    )

}
