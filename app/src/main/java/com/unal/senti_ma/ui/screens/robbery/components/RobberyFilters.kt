package com.unal.senti_ma.ui.screens.robbery.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.unal.senti_ma.R
import com.unal.senti_ma.utils.formatDate

@Composable
fun RobberyFilters(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    selectedType: String?,
    selectedFromTimestamp: Long?,
    selectedToTimestamp: Long?,
    selectedAddress: String,
    onTypeSelected: (String?) -> Unit,
    onDateClick: () -> Unit,
    onAddressChange: (String) -> Unit,
    onClearAddress: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 12.dp,
                start = 12.dp,
                end = 12.dp
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(8.dp)
    ) {
        TextButton(
            onClick = {
                onExpandedChange(!expanded)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = null
            )

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Text(
                text = stringResource(
                    R.string.text_robbery_filters
                )
            )

            Spacer(
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = if (expanded) {
                    Icons.Default.ExpandLess
                } else {
                    Icons.Default.ExpandMore
                },
                contentDescription = stringResource(
                    if (expanded) {
                        R.string.description_collapse_filters
                    } else {
                        R.string.description_expand_filters
                    }
                )
            )
        }

        if (expanded) {
            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                // 1. Robbery type
                RobberyTypeFilterDropdown(
                    selectedType = selectedType,
                    onTypeSelected = onTypeSelected,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                // 2. Address
                OutlinedTextField(
                    value = selectedAddress,
                    onValueChange = onAddressChange,
                    label = {
                        Text(
                            text = stringResource(
                                R.string.text_robbery_address_label
                            )
                        )
                    },
                    placeholder = {
                        Text(
                            text = stringResource(
                                R.string.text_robbery_address_placeholder
                            )
                        )
                    },
                    singleLine = true,
                    trailingIcon = {
                        if (selectedAddress.isNotEmpty()) {
                            IconButton(
                                onClick = onClearAddress
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = null
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(
                    modifier = Modifier.height(15.dp)
                )

                // 3. Date range
                OutlinedButton(
                    onClick = onDateClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    if (
                        selectedFromTimestamp != null &&
                        selectedToTimestamp != null
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = stringResource(
                                    R.string.text_robbery_date_from,
                                    formatDate(
                                        selectedFromTimestamp
                                    )
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                text = stringResource(
                                    R.string.text_robbery_date_to,
                                    formatDate(
                                        selectedToTimestamp
                                    )
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    } else {
                        Text(
                            text = stringResource(
                                R.string.text_robbery_date_range_title
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
