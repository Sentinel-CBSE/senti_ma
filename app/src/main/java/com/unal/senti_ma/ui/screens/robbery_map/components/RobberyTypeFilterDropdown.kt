package com.unal.senti_ma.ui.screens.robbery_map.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.unal.senti_ma.R

private val ROBBERY_TYPES = listOf(
    null to R.string.text_robbery_type_all,
    "armed_robbery" to R.string.text_robbery_type_armed,
    "theft" to R.string.text_robbery_type_theft,
    "burglary" to R.string.text_robbery_type_burglary
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RobberyTypeFilterDropdown(
    selectedType: String?,
    onTypeSelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    val selectedLabel = ROBBERY_TYPES
        .first { it.first == selectedType }
        .second

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = stringResource(selectedLabel),
            onValueChange = {},
            readOnly = true,
            label = { Text(text = stringResource(R.string.text_robbery_type_label)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth()
        )

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = {
                isExpanded = false
            }
        ) {
            ROBBERY_TYPES.forEach { (typeValue, labelRes) ->
                DropdownMenuItem(
                    text = { Text(text = stringResource(labelRes)) },
                    onClick = {
                        isExpanded = false
                        onTypeSelected(typeValue)
                    }
                )
            }
        }
    }
}
