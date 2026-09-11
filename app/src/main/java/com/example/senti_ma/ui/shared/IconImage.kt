package com.example.senti_ma.ui.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.senti_ma.R

/**
 * Displays a circular icon with a shadow and border.
 */
@Composable
fun IconImage(
    size: Dp,
    modifier: Modifier = Modifier
){
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .border(
                width = 2.dp,
                color = colorScheme.background,
                shape = CircleShape
            )
            .shadow(
                elevation = 16.dp,
                shape = CircleShape
            )
    ) {
        Image(
            painter = painterResource(id = R.drawable.icono_con_fondo_ensenas),
            contentDescription = stringResource(R.string.description_app_icon),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }

}
