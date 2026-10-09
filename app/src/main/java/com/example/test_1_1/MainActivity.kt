package com.example.test_1_1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.example.test_1_1.ui.theme.Test_1_1Theme

fun isValidPhoneNumber(input: String): Boolean {
    val trimmed = input.trim()
    if (trimmed.isEmpty()) return false

    val normalized = buildString {
        trimmed.forEachIndexed { index, ch ->
            when {
                ch.isDigit() -> append(ch)
                ch == '+' && index == 0 -> append(ch)
            }
        }
    }

    if (normalized.isEmpty()) return false

    if (normalized.startsWith("+")) {
        val digits = normalized.drop(1)
        return digits.all { it.isDigit() } && digits.length in 8..15
    }

    return normalized.all { it.isDigit() } && normalized.length >= 10
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Test_1_1Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FirstScreen()
                }
            }
        }
    }
}

@Composable
@Preview
fun FirstScreen() {
    val context = LocalContext.current
    var text by remember { mutableStateOf("") }
    var phoneError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
                phoneError = null
            },
            label = { Text("Введите текст или номер телефона") },
            isError = phoneError != null,
            modifier = Modifier.fillMaxWidth()
        )

        if (phoneError != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = phoneError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val intent = Intent(context, SecondActivity::class.java)
                intent.putExtra("EXTRA_TEXT", text)
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Открыть вторую Activity")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = {
                val input = text.trim()
                when {
                    input.isEmpty() -> {
                        phoneError = "Введите номер телефона"
                    }
                    !isValidPhoneNumber(input) -> {
                        phoneError = "Это не похоже на номер телефона. " +
                                "Используйте формат +7 999 123-45-67 или 89991234567"
                    }
                    else -> {
                        phoneError = null
                        val normalized = input
                            .filter { it.isDigit() || (it == '+' && input.indexOf(it) == 0) }
                        val dialIntent = Intent(
                            Intent.ACTION_DIAL,
                            Uri.parse("tel:$normalized")
                        )
                        context.startActivity(dialIntent)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Позвонить другу")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = {
                val shareText = text.trim()
                if (shareText.isNotEmpty()) {
                    val sendIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, shareText)
                    }
                    context.startActivity(
                        Intent.createChooser(sendIntent, "Поделиться через…")
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Поделиться текстом")
        }
    }
}