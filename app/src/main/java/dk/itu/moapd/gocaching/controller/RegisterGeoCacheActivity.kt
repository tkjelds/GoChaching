package dk.itu.moapd.gocaching.controller

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import dk.itu.moapd.gocaching.R
import dk.itu.moapd.gocaching.R.layout.activity_go_caching
import kotlinx.android.synthetic.main.scanner_view.*

private const val CAMERA_REQUEST_CODE = 101
private val pattern = Regex("(?![^\\d])([0-9.-]+).+?([0-9.-]+)")
class RegisterGeoCacheActivity : AppCompatActivity() {

    private lateinit var codeScanner:CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scanner_view)

        GeoCacheCodeScanning()
    }

    private fun GeoCacheCodeScanning() {
        codeScanner = CodeScanner(this,scanner_view)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                runOnUiThread {
                    if(pattern.containsMatchIn(it.text)){
                        code_scanner_textview.setText(pattern.find(it.text)?.value)
                    }

                }
            }
            errorCallback = ErrorCallback {
                runOnUiThread {
                    Log.e("Main","Camera initialization error: ${it.message}")
                }
            }
        }

        scanner_view.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.stopPreview()
        super.onPause()
    }

}