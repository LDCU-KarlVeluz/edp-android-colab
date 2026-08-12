package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.theme.MyApplicationTheme

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    if (state.isPreview) {
        ProfilePreview(state, onBack = viewModel::backToEdit)
    } else {
        ProfileForm(state, viewModel)
    }
}

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
        
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = state.name,
            onValueChange = viewModel::onNameChange,
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = state.email,
            onValueChange = viewModel::onEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = state.contactNumber,
            onValueChange = viewModel::onContactChange,
            label = { Text("Contact Number") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = state.address,
            onValueChange = viewModel::onAddressChange,
            label = { Text("Address") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = state.username,
            onValueChange = viewModel::onUsernameChange,
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))
        Text("Skills", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)

        Row(
            modifier = Modifier.padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = state.newSkill,
                onValueChange = viewModel::onNewSkillChange,
                label = { Text("Add skill") },
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            Button(onClick = viewModel::addSkill) {
                Text("Add")
            }
        }

        state.skills.forEach { skill ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("• $skill", modifier = Modifier.weight(1f))
                TextButton(onClick = { viewModel.removeSkill(skill) }) {
                    Text("Remove", color = MaterialTheme.colorScheme.error)
                }
            }
        }

        Spacer(Modifier.height(32.dp))
        
        Button(
            onClick = viewModel::showPreview,
            modifier = Modifier.fillMaxWidth(),
            enabled = state.name.isNotBlank() && state.email.isNotBlank()
        ) {
            Text("Preview")
        }

        OutlinedButton(
            onClick = viewModel::clearAll,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Text("Clear All")
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
            text = "Profile Preview",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DetailItem("Full Name", state.name)
                DetailItem("Email Address", state.email)
                DetailItem("Contact Number", state.contactNumber)
                DetailItem("Address", state.address)
                DetailItem("Username", state.username)

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                Text("Skills", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)

                if (state.skills.isEmpty()) {
                    Text("No skills added", color = Color.Gray)
                } else {
                    FlowRow(
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

        Spacer(Modifier.height(24.dp))
        
        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to edit")
        }
    }
}

@Composable
private fun DetailItem(label: String, value: String) {
    Column {
        Text(label.uppercase(), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
        Text(value.ifBlank { "Not set" }, fontSize = 18.sp)
    }
}

@Composable
private fun SkillChip(skill: String) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = MaterialTheme.shapes.medium
    ) {
        Text(
            text = skill,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    MyApplicationTheme {
        ProfileScreen()
    }
}
