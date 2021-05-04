package dk.itu.moapd.gocaching.controller

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.budiyev.android.codescanner.*
import dk.itu.moapd.gocaching.GeoCache
import dk.itu.moapd.gocaching.R
import dk.itu.moapd.gocaching.User
import dk.itu.moapd.gocaching.UserCacheCrossRef
import dk.itu.moapd.gocaching.controller.LoginFragment.Companion.geoCacheVM
import kotlinx.android.synthetic.main.scanner_view.*
import java.util.*


private const val CAMERA_REQUEST_CODE = 101
private val pattern = Regex("(?![^\\d])([0-9.-]+).+?([0-9.-]+)")

class RegisterGeoCacheActivity : AppCompatActivity() {
    private lateinit var userEmail: String
    private lateinit var user: User
    private lateinit var codeScanner: CodeScanner
    private lateinit var caches: List<GeoCache>
    private lateinit var usersCaches: List<GeoCache>
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scanner_view)

        userEmail = intent.getStringExtra("email")
        geoCacheVM.getUsers().observe(this, Observer {
            user = it.find { u -> u.email == userEmail }!!
        })
        geoCacheVM.getGeoCaches().observe(this, Observer {
            caches = it.filter { c -> c.isApproved }
        })
        geoCacheVM.getUserWithCaches().observe(this, Observer {
            usersCaches = it.find { uc -> uc.user.email == userEmail }!!.caches
        })

        GeoCacheCodeScanning()
    }

    private fun GeoCacheCodeScanning() {
        codeScanner = CodeScanner(this, scanner_view)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                runOnUiThread {
                    if (pattern.containsMatchIn(it.text)) {
                        var qrText = pattern.findAll(it.text).elementAt(0).value.split(",")
                        if (caches.any { cache -> cache.lat.toString() == qrText.get(0) && cache.long_.toString() == qrText.get(1) }
                                && !usersCaches.any { cache -> cache.lat.toString() == qrText.get(0) && cache.long_.toString() == qrText.get(1) }) {
                            var cache = caches.find { cache -> cache.lat.toString() == qrText.get(0) && cache.long_.toString() == qrText.get(1) }!!
                            var userId = user.uid
                            geoCacheVM.insert(UserCacheCrossRef(
                                    gcid = cache.gcid,
                                    uid = userId)
                            )
                            showdialog()
                            geoCacheVM.update(GeoCache(
                                    gcid = cache.gcid,
                                    cache = cache.cache,
                                    where = cache.where,
                                    lat = cache.lat,
                                    long_ = cache.long_,
                                    date = cache.date,
                                    updateDate = cache.updateDate,
                                    category = cache.category,
                                    difficulty = cache.difficulty,
                                    score = score,
                                    registrations = (cache.registrations++),
                                    isApproved = cache.isApproved
                            ))
                            this@RegisterGeoCacheActivity.finish()
                            //Toast.makeText(this@RegisterGeoCacheActivity,"Success GeoCache Registered",Toast.LENGTH_SHORT).show()
                        }
                        //Toast.makeText(this@RegisterGeoCacheActivity,"GeoCache already Registered",Toast.LENGTH_SHORT).show()
                    } else {
                        //Toast.makeText(this@RegisterGeoCacheActivity,"Did not recognize qrCode",Toast.LENGTH_SHORT).show()
                    }
                    code_scanner_textview.text = it.text
                }
            }
            errorCallback = ErrorCallback {
                runOnUiThread {
                    Log.e("Main", "Camera initialization error: ${it.message}")
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
    fun showdialog(){
        val builder: AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        builder.setTitle(R.string.score)

// Set up the input
        val input = EditText(this)
        input.setHint("Enter score")
        input.inputType = InputType.TYPE_NUMBER_FLAG_SIGNED
        builder.setView(input)

        builder.setPositiveButton(R.string.confirm, DialogInterface.OnClickListener { dialog, which ->
            score = input.text.toString().toInt()
        })

        builder.show()
    }

}