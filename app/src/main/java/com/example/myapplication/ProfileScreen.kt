package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.res.Configuration
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ProfileForm(state: ProfileUiState, viewModel: ProfileViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "My Profile",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = state.name,
            onValueChange = { viewModel.onNameChange(it) },
            label = { Text("Full name") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = state.email,
            onValueChange = { viewModel.onEmailChange(it) },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = state.contactNumber,
            onValueChange = { viewModel.onContactChange(it) },
            label = { Text("Contact number") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = state.address,
            onValueChange = { viewModel.onAddressChange(it) },
            label = { Text("Address") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = state.username,
            onValueChange = { viewModel.onUsernameChange(it) },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))
        Text("Skills", fontWeight = FontWeight.Bold)

        // Type a skill + Add button
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = state.newSkill,
                onValueChange = { viewModel.onNewSkillChange(it) },
                label = { Text("Add a skill") },
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            Button(onClick = { viewModel.addSkill() }) {
                Text("Add")
            }
        }

        // One row per skill, each with a Remove button
        state.skills.forEach { skill ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("• $skill", modifier = Modifier.weight(1f))
                TextButton(onClick = { viewModel.removeSkill(skill) }) {
                    Text("Remove")
                }
            }
        }

        Spacer(Modifier.height(20.dp))
        Button(
            onClick = { viewModel.showPreview() },
            modifier = Modifier.fillMaxWidth(),
            enabled = state.name.isNotBlank() && state.email.isNotBlank()
        ) {
            Text("Preview")
        }

        Spacer(Modifier.height(8.dp))
        OutlinedButton(
            onClick = { viewModel.clearAll() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Clear all")
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfilePreview(state: ProfileUiState, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Profile Preview",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ProfileDetailItem(label = "Full Name", value = state.name)
                ProfileDetailItem(label = "Email Address", value = state.email)
                ProfileDetailItem(label = "Contact Number", value = state.contactNumber)
                ProfileDetailItem(label = "Address", value = state.address)
                ProfileDetailItem(label = "Username", value = state.username)

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                )

                Text(
                    "Skills",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                if (state.skills.isEmpty()) {
                    Text(
                        "No skills added yet.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                } else {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        state.skills.forEach { skill ->
                            SkillChip(skill)
                        }
                    }
                }
            }
        }

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Back to edit", fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun ProfileDetailItem(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label.uppercase(),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary,
            letterSpacing = 1.sp
        )
        Text(
            text = if (value.isBlank()) "Not provided" else value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = if (value.isBlank()) Color.Gray else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun SkillChip(skill: String) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = MaterialTheme.shapes.large,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(
            text = skill,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    if (state.isPreview) {
        ProfilePreview(state = state, onBack = { viewModel.backToEdit() })
    } else {
        ProfileForm(state = state, viewModel = viewModel)
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    MyApplicationTheme {
        ProfileScreen()
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Dark Mode"
)
@Composable
fun ProfileScreenDarkPreview() {
    MyApplicationTheme {
        ProfileScreen()
    }
}
