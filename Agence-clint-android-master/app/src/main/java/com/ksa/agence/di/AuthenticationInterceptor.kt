import android.content.Context
import android.net.ConnectivityManager
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.ksa.agence.app.AgenceApp
import com.ksa.agence.common.BEARER
import com.ksa.agence.common.LANG
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.IOException
import java.util.Locale


class ConnectivityInterceptor(private val context: Context) : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isConnected()) {
            showToast("فشل الاتصال بالخادم. تحقق من اتصالك بالإنترنت وحاول مرة أخرى.")
            return Response.Builder()
                .body("فقدان الانترنت".toResponseBody(null)) // Whatever body
                .protocol(Protocol.HTTP_2)
                .message("فقدان الانترنت")
                .request(chain.request())
                .code(0)
                .build()        }
        return chain.proceed(chain.request())
    }

    private fun isConnected(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun showToast(message: String) {
        Handler(context.mainLooper).post {
            runOnUiThread {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

}

class AuthenticationInterceptor(private val context: Context) : Interceptor {

    private lateinit var lang: String

    @Throws(NoInternetException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = BEARER + " " + AgenceApp.pref.authToken.toString()
        if ( Locale.getDefault().language =="ar")
        {
            lang = AgenceApp.pref.getString(LANG, "ar").toString()

        }
        else{
            lang = AgenceApp.pref.getString(LANG, "en").toString()

        }

        val requestBuilder = chain.request().newBuilder()
            .header("Authorization", token)
            .header("Accept-Language", lang)
            .header("Accept", "application/json")


        val request = requestBuilder.build()

        return runBlocking {
            retryIO {
                chain.proceed(request)
            }
        }
    }
}

fun runOnUiThread(action: () -> Unit) {
    Handler(Looper.getMainLooper()).post(action)
}

class NoInternetException(message: String) : IOException(message)

// داخل retryIO
suspend fun <T> retryIO(
    times: Int = 3, initialDelay: Long = 100, // بالمللي ثانية
    maxDelay: Long = 1000, // بالمللي ثانية
    factor: Double = 2.0, block: suspend () -> T
): T {
    var currentDelay = initialDelay
    repeat(times - 1) {
        try {
            return block()
        } catch (e: NoInternetException) {
            // محاصرة الاستثناء هنا
            //  showToast("فشل الاتصال بالخادم. تحقق من اتصالك بالإنترنت وحاول مرة أخرى.")
            delay(currentDelay)
            currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
        } catch (e: IOException) {
            delay(currentDelay)
            currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
        }
    }
    return block() // المحاولة الأخيرة
}

