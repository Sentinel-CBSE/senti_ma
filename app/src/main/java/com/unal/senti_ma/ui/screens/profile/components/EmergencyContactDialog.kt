package com.unal.senti_ma.ui.screens.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.unal.senti_ma.R

@Composable
fun EmergencyContactDialog(
    title: String,
    initialName: String = "",
    initialPhone: String = "",
    initialRelationship: String = "",
    onDismiss: () -> Unit,
    onConfirm: (
        name: String,
        phone: String,
        relationship: String
    ) -> Unit
) {
    var name by remember(initialName) {
        mutableStateOf(initialName)
    }

    var phone by remember(initialPhone) {
        mutableStateOf(initialPhone)
    }

    var relationship by remember(initialRelationship) {
        mutableStateOf(initialRelationship)
    }

    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text(title)
        },

        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                    },
                    label = {
                        Text(
                            stringResource(
                                R.string.text_field_contact_name
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = {
                        phone = it
                    },
                    label = {
                        Text(
                            stringResource(
                                R.string.text_field_contact_phone
                            )
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = relationship,
                    onValueChange = {
                        relationship = it
                    },
                    label = {
                        Text(
                            stringResource(
                                R.string.text_field_contact_relationship
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },

        confirmButton = {
            TextButton(
                onClick = {
                    if (
                        name.isNotBlank() &&
                        phone.isNotBlank() &&
                        relationship.isNotBlank()
                    ) {
                        onConfirm(
                            name,
                            phone,
                            relationship
                        )
                    }
                }
            ) {
                Text(
                    stringResource(
                        R.string.text_button_save
                    )
                )
            }
        },

        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    stringResource(
                        R.string.text_button_cancel
                    )
                )
            }
        }
    )
}
