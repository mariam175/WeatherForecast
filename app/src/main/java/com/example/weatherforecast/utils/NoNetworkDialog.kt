package com.example.weatherforecast.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.weatherforecast.R

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NoInternetDialog() {
    Dialog (onDismissRequest = {}) {
        Box(
            modifier = Modifier
                .border(
                    2.dp,
                    Color.Black,
                    shape = RoundedCornerShape(10.dp),

                    )
                .size(300.dp)
               // .background(Color.White)
                .padding(16.dp)
        ) {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GlideImage(
                    model = R.drawable.no_network2,
                    contentDescription = "",
                    Modifier.size(200.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.no_internet_connection),
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    }
}
