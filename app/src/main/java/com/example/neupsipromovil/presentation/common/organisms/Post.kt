package com.example.neupsipromovil.presentation.common.organisms

import android.service.quicksettings.Tile
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.neupsipromovil.presentation.common.atoms.UsernamePostMolecule

@Composable
fun PostOrganism(
    modifier: Modifier = Modifier,
    username: String = "Username",
    avatarLetter: String = "U",
    title: String ="Titulo del Post",
    body: String = "Cuerpo del Post",
) {
    Column(
        modifier =
            modifier
                .width(360.dp)
                .wrapContentHeight()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF5F5F5))
                .padding(vertical = 10.dp),
    ) {
        UsernamePostMolecule(
            title = username,
            avatarLetter = avatarLetter,
        )

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 16.dp)
                    .padding(top = 10.dp),
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1A1A2E),
            )
        }

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 16.dp)
                    .padding(top = 10.dp, bottom = 10.dp),
        ) {
            Text(
                text = body,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF1A1A2E),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PostOrganismPreview() {
    PostOrganism(
        username = "Username",
        avatarLetter = "U",
        title ="BUENOS DIASSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS",
        body = "HOLAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
    )

}
