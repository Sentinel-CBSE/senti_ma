package com.unal.senti_ma.ui.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.unal.senti_ma.R

@Composable
fun IconImage(
    modifier: Modifier = Modifier,
    size: Dp
) {
    val shape = RoundedCornerShape(14.dp)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .shadow(
                elevation = 8.dp,
                shape = shape
            )
            .clip(shape)
    ) {
        Image(
            painter = painterResource(id = R.drawable.fond),
            contentDescription = stringResource(
                R.string.description_app_icon
            ),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}
