import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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
        Text(
            text = "Comunidad",
            modifier = modifier,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ComunidadAtomoPreview() {
    ComunidadAtomo()
}