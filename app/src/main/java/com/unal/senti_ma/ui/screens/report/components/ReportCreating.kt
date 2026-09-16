package com.unal.senti_ma.ui.screens.report.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.unal.senti_ma.R

@Composable
fun ReportCreating() {
    Text(
        text = stringResource(
            R.string.text_creating_report
        ),
        style = MaterialTheme.typography.bodyMedium
    )
}
