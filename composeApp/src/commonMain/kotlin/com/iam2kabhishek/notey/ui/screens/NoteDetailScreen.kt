package com.iam2kabhishek.notey.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iam2kabhishek.notey.data.notes.NoteEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    existingNote: NoteEntity?,
    onSave: (title: String, content: String) -> Unit,
    onDelete: () -> Unit,
    onBack: () -> Unit
) {
    var title by remember(existingNote) { mutableStateOf(existingNote?.title ?: "") }
    var content by remember(existingNote) { mutableStateOf(existingNote?.content ?: "") }
    
    val isEditing = existingNote != null
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Edit Note" else "New Note") },
                actions = {
                    if (isEditing) {
                        TextButton(onClick = onDelete) {
                            Text("Delete", color = MaterialTheme.colorScheme.error)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Content") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                minLines = 5
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = { onSave(title, content) },
                modifier = Modifier.fillMaxWidth(),
                enabled = title.isNotBlank() || content.isNotBlank()
            ) {
                Text("Save")
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            TextButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancel")
            }
        }
    }
}

@Preview
@Composable
fun NoteDetailScreenCreatePreview() {
    NoteDetailScreen(
        existingNote = null,
        onSave = { _, _ -> },
        onDelete = {},
        onBack = {}
    )
}

@Preview
@Composable
fun NoteDetailScreenEditPreview() {
    NoteDetailScreen(
        existingNote = NoteEntity(1, "Sample Note", "This is the note content", 1000, 2000),
        onSave = { _, _ -> },
        onDelete = {},
        onBack = {}
    )
}