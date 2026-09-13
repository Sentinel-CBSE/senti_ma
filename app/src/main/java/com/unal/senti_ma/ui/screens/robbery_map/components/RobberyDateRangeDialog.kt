package com.unal.senti_ma.ui.screens.robbery_map.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.unal.senti_ma.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RobberyDateRangeDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onConfirm: (from: Long, to: Long) -> Unit,
    onClear: () -> Unit
) {
    val dateRangePickerState = rememberDateRangePickerState()

    DatePickerDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val from = dateRangePickerState.selectedStartDateMillis
                    val to = dateRangePickerState.selectedEndDateMillis

                    if (from != null && to != null) {
                        onConfirm(from, to)
                    }
                },
                enabled = dateRangePickerState.selectedStartDateMillis != null &&
                        dateRangePickerState.selectedEndDateMillis != null
            ) {
                Text(text = stringResource(R.string.text_apply))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onClear()
                    onDismiss()
                }
            ) {
                Text(text = stringResource(R.string.text_clear_filter))
            }
        }
    ) {
        DateRangePicker(
            state = dateRangePickerState,
            title = {
                Text(
                    text = stringResource(R.string.text_robbery_date_range_title),
                    fontWeight = FontWeight.Medium
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
