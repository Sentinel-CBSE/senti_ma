package com.unal.senti_ma.ui.screens.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.unal.senti_ma.R
import com.unal.senti_ma.domain.enums.BloodTypeLetter
import com.unal.senti_ma.domain.enums.BloodTypeRh
import com.unal.senti_ma.domain.model.EmergencyContact
import com.unal.senti_ma.ui.screens.profile.events.ProfileUiEvent

@Composable
fun ProfileContent(
    user: com.unal.senti_ma.domain.model.User,
    isEditing: Boolean,
    isUpdating: Boolean,
    userUpdate: com.unal.senti_ma.domain.model.UserUpdate?,
    isDarkTheme: Boolean?,
    onToggleTheme: (Boolean) -> Unit,
    onEvent: (ProfileUiEvent) -> Unit,
    onAddContact: () -> Unit,
    onEditContact: (EmergencyContact) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        item {
            isDarkTheme?.let { darkTheme ->

                TextButton(
                    onClick = {
                        onToggleTheme(!darkTheme)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isUpdating
                ) {
                    Icon(
                        imageVector = Icons.Default.DarkMode,
                        contentDescription = stringResource(
                            R.string.description_icon_theme
                        )
                    )

                    Text(
                        text = stringResource(
                            R.string.text_dark_theme
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp)
                    )

                    Switch(
                        checked = darkTheme,
                        onCheckedChange = onToggleTheme,
                        enabled = !isUpdating,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor =
                                MaterialTheme.colorScheme.primary,
                            checkedTrackColor =
                                MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
            }
        }

        item {
            HorizontalDivider()
        }

        item {
            Spacer(
                modifier = Modifier.height(8.dp)
            )
        }

        if (isEditing && userUpdate != null) {

            item {
                OutlinedTextField(
                    value = userUpdate.displayName.orEmpty(),
                    onValueChange = {
                        onEvent(
                            ProfileUiEvent.UpdateProfileDraft(
                                userUpdate.copy(
                                    displayName = it
                                )
                            )
                        )
                    },
                    label = {
                        Text(
                            stringResource(
                                R.string.text_field_profile_name
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isUpdating
                )
            }

            item {
                OutlinedTextField(
                    value = userUpdate.eps.orEmpty(),
                    onValueChange = {
                        onEvent(
                            ProfileUiEvent.UpdateProfileDraft(
                                userUpdate.copy(
                                    eps = it
                                )
                            )
                        )
                    },
                    label = {
                        Text(
                            stringResource(
                                R.string.text_field_profile_eps
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isUpdating
                )
            }

            item {
                Text(
                    text = stringResource(
                        R.string.text_profile_blood_type
                    ),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BloodTypeLetter.entries.forEach { bloodType ->

                        OutlinedButton(
                            onClick = {
                                onEvent(
                                    ProfileUiEvent.UpdateProfileDraft(
                                        userUpdate.copy(
                                            bloodTypeLetter = bloodType
                                        )
                                    )
                                )
                            },
                            enabled = !isUpdating,
                            colors = if (userUpdate.bloodTypeLetter == bloodType) {
                                ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                )
                            } else {
                                ButtonDefaults.outlinedButtonColors()
                            }
                        ) {
                            Text(bloodType.name)
                        }
                    }
                }
            }

            item {
                Text(
                    text = stringResource(
                        R.string.text_profile_rh
                    ),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BloodTypeRh.entries.forEach { rh ->

                        OutlinedButton(
                            onClick = {
                                onEvent(
                                    ProfileUiEvent.UpdateProfileDraft(
                                        userUpdate.copy(
                                            bloodTypeRh = rh
                                        )
                                    )
                                )
                            },
                            enabled = !isUpdating,
                            colors = if (userUpdate.bloodTypeRh == rh) {
                                ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                )
                            } else {
                                ButtonDefaults.outlinedButtonColors()
                            }
                        ) {
                            Text(
                                if (rh == BloodTypeRh.POSITIVE) {
                                    "+"
                                } else {
                                    "-"
                                }
                            )
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    OutlinedButton(
                        onClick = {
                            onEvent(
                                ProfileUiEvent.CancelEditing
                            )
                        },
                        modifier = Modifier.weight(1f),
                        enabled = !isUpdating
                    ) {
                        Text(
                            stringResource(
                                R.string.text_button_cancel
                            )
                        )
                    }

                    Button(
                        onClick = {
                            onEvent(
                                ProfileUiEvent.SaveProfile
                            )
                        },
                        modifier = Modifier.weight(1f),
                        enabled = !isUpdating
                    ) {
                        Text(
                            stringResource(
                                R.string.text_button_save
                            )
                        )
                    }
                }
            }

        } else {

            item {
                Text(
                    text = user.displayName.orEmpty(),
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            item {
                Text(
                    text = user.email.orEmpty()
                )
            }

            item {
                Text(
                    text = stringResource(
                        R.string.text_profile_eps_value,
                        user.eps
                            ?: stringResource(
                                R.string.text_not_registered
                            )
                    )
                )
            }

            item {
                val bloodType =
                    user.bloodTypeLetter?.name
                        ?: stringResource(
                            R.string.text_not_registered
                        )

                val rh = when (user.bloodTypeRh) {
                    BloodTypeRh.POSITIVE -> "+"
                    BloodTypeRh.NEGATIVE -> "-"
                    null -> ""
                }

                Text(
                    text = stringResource(
                        R.string.text_profile_blood_type_value,
                        bloodType + rh
                    )
                )
            }

            item {
                Button(
                    onClick = {
                        onEvent(
                            ProfileUiEvent.StartEditing
                        )
                    },
                    enabled = !isUpdating,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(
                            R.string.description_icon_edit
                        )
                    )

                    Text(
                        text = stringResource(
                            R.string.text_button_edit_profile
                        ),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }

        item {
            Spacer(
                modifier = Modifier.height(8.dp)
            )

            HorizontalDivider()

            Spacer(
                modifier = Modifier.height(8.dp)
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(
                        R.string.text_emergency_contacts
                    ),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = onAddContact,
                    enabled = !isUpdating
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(
                            R.string.description_icon_add_contact
                        )
                    )
                }
            }
        }

        if (user.emergencyContacts.isEmpty()) {

            item {
                Text(
                    text = stringResource(
                        R.string.text_no_emergency_contacts
                    )
                )
            }

        } else {

            items(
                items = user.emergencyContacts,
                key = { it.uid }
            ) { contact ->

                EmergencyContactItem(
                    contact = contact,
                    enabled = !isUpdating,
                    onEdit = {
                        onEditContact(contact)
                    },
                    onDelete = {
                        onEvent(
                            ProfileUiEvent.DeleteEmergencyContact(
                                contact.uid
                            )
                        )
                    }
                )
            }
        }

        item {
            Spacer(
                modifier = Modifier.height(8.dp)
            )

            TextButton(
                onClick = {
                    onEvent(ProfileUiEvent.SignOut)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isUpdating
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = stringResource(
                        R.string.description_icon_sign_out
                    )
                )

                Text(
                    text = stringResource(
                        R.string.text_sign_out
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                )
            }
        }
    }
}
