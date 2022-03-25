import android.content.Context
import com.example.cryptolist.R
import java.io.IOException

class NoConnectivityException(val ctx: Context) : IOException() {
    override val message: String?
        get() = ctx.getString(R.string.no_internet_connection)
}