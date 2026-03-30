import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ComunidadAtomo(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .width(137.dp)
                .height(64.dp),
    ) {
        Box(
            modifier =
                modifier
                    .width(56.dp)
                    .height(32.dp)
                    .clip(RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier =
                    modifier
                        .width(56.dp)
                        .height(32.dp)
                        .clip(RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center,
            ) {

            }
        }
        Text(
            text = "Comunidad",
            modifier =
                modifier
                    .padding(top = 42.dp)
                    .fillMaxWidth()
                    .height(16.dp),
            style =
                TextStyle(
                    fontFamily = FontFamily.Default,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.5.sp,
                    lineHeight = 16.sp,
                    color = Color(0xFF49454F),
                    textAlign = Center,
                ),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ComunidadAtomoPreview() {
    ComunidadAtomo()
}