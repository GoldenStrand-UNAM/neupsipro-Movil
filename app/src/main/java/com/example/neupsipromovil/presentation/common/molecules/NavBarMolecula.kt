import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.neupsipromovil.presentation.common.atoms.NavBarRectangleAtom
import com.example.neupsipromovil.presentation.common.molecules.ActividadesMolecula
import com.example.neupsipromovil.presentation.common.molecules.ComunidadMolecula
import com.example.neupsipromovil.presentation.common.molecules.CuidadoMolecula

@Composable
fun NavBarMolecula(
    modifier: Modifier = Modifier,
) {
    NavBarRectangleAtom(modifier = modifier) {
        ComunidadMolecula(modifier = Modifier.weight(1f))
        CuidadoMolecula(modifier = Modifier.weight(1f))
        ActividadesMolecula(modifier = Modifier.weight(1f))
    }
}

@Preview(showBackground = true)
@Composable
fun NavBarMoleculaPreview() {
    NavBarMolecula()
}
