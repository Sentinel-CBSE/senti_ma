package com.unal.senti_ma.ui.screens.profile.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.unal.senti_ma.R
import com.unal.senti_ma.domain.model.EmergencyContact

@Composable
fun EmergencyContactItem(
    contact: EmergencyContact,
    enabled: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = contact.name,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = contact.phoneNumber
            )

            Text(
                text = contact.relationship
            )
        }

        IconButton(
            onClick = onEdit,
            enabled = enabled
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = stringResource(
                    R.string.description_icon_edit_contact
                )
            )
        }

        IconButton(
            onClick = onDelete,
            enabled = enabled
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(
                    R.string.description_icon_delete_contact
                )
            )
        }
    }
}
