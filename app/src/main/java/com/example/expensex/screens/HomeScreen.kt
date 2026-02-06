package com.example.expensex.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true)
@Composable
fun HomeScreen(){
    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.height(300.dp)
            .fillMaxWidth()
            .background(color = Color(0xFFE3F2FD))) {  }
    }
}