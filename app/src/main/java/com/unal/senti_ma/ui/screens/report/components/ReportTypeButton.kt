package com.unal.senti_ma.ui.screens.report.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.unal.senti_ma.R
import com.unal.senti_ma.ui.screens.report.ReportUiState

@Composable
fun ReportTypeButton(
    type: String,
    text: String,
    icon: ImageVector,
    state: ReportUiState,
    onStartHolding: (String) -> Unit,
    onStopHolding: () -> Unit,
    onCancel: () -> Unit
) {
    val isHolding =
        state is ReportUiState.Holding &&
                state.type == type

    val isConfirming =
        state is ReportUiState.Confirming &&
                state.type == type

    val targetProgress = when {
        isHolding -> 1f
        isConfirming -> 0f
        else -> 0f
    }

    val progressDuration = when {
        isHolding -> 3_000
        isConfirming -> 3_000
        else -> 0
    }

    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(
            durationMillis = progressDuration,
            easing = LinearEasing
        ),
        label = "reportProgress"
    )

    val currentState by rememberUpdatedState(state)

    val currentOnStartHolding by rememberUpdatedState(
        onStartHolding
    )

    val currentOnStopHolding by rememberUpdatedState(
        onStopHolding
    )

    val currentOnCancel by rememberUpdatedState(
        onCancel
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .clip(
                RoundedCornerShape(16.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (isConfirming) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.primary
                    }
                )
        )

        if (isHolding || isConfirming) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .fillMaxSize()
                    .background(
                        if (isConfirming) {
                            MaterialTheme.colorScheme.errorContainer
                        } else {
                            MaterialTheme.colorScheme.primaryContainer
                        }
                    )
            )
        }

        Row(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(type) {
                    detectTapGestures(
                        onPress = {
                            when (currentState) {
                                is ReportUiState.Confirming -> {
                                    currentOnCancel()
                                    awaitRelease()
                                }

                                ReportUiState.Idle -> {
                                    currentOnStartHolding(type)

                                    try {
                                        awaitRelease()
                                    } finally {
                                        currentOnStopHolding()
                                    }
                                }

                                is ReportUiState.Holding -> {
                                    try {
                                        awaitRelease()
                                    } finally {
                                        currentOnStopHolding()
                                    }
                                }

                                else -> {
                                    return@detectTapGestures
                                }
                            }
                        }
                    )
                },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isConfirming) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(
                        R.string.text_cancel
                    ),
                    tint = MaterialTheme.colorScheme.onError,
                    modifier = Modifier.size(44.dp)
                )
            } else {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(28.dp)
                )

                Spacer(
                    modifier = Modifier.width(12.dp)
                )

                Text(
                    text = if (isHolding) {
                        "$text ${(animatedProgress * 100).toInt()}%"
                    } else {
                        text
                    },
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}
