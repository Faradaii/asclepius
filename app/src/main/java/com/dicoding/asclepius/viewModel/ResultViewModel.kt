import android.net.Uri
import androidx.lifecycle.ViewModel

class ResultViewModel : ViewModel() {
    var imageUri: Uri? = null
    var predictions: String? = null
    var confidenceScore: Int = 0
}
