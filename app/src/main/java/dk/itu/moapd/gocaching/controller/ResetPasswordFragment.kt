package dk.itu.moapd.gocaching.controller

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import dk.itu.moapd.gocaching.R
import dk.itu.moapd.gocaching.User
import dk.itu.moapd.gocaching.controller.LoginFragment.Companion.geoCacheVM

class ResetPasswordFragment : Fragment() {
    private lateinit var psReq1: TextView
    private lateinit var psReq2: TextView
    private lateinit var psReq3: TextView
    private lateinit var psRepeat: TextView
    private lateinit var currentPasswordEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var repeatPasswordEditText: EditText
    private lateinit var confirmButton: Button
    private lateinit var email: String
    private var repeatCorrectness = false
    private var passwordCorrectness = false
    private var currentPasswordCorrectness = false
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.new_password_fragment, container, false)
        psReq1 = view.findViewById(R.id.ps_req1)
        psReq2 = view.findViewById(R.id.ps_req2)
        psReq3 = view.findViewById(R.id.ps_req3)
        psRepeat = view.findViewById(R.id.ps_repeat)
        currentPasswordEditText = view.findViewById(R.id.new_password_current_password)
        passwordEditText = view.findViewById(R.id.new_password_new_password)
        repeatPasswordEditText = view.findViewById(R.id.new_password_repeat)
        confirmButton = view.findViewById(R.id.new_password_confirm_button)
        email = arguments!!.getString("email")
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        currentPasswordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                geoCacheVM.getUsers().observe(this@ResetPasswordFragment, Observer<List<User>> {
                    var user = it.find { u -> u.email == email }!!
                    var psCorrect = user.password == p0.toString()
                    currentPasswordCorrectness = psCorrect
                })
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                passwordCorrectness = passwordChecker(p0)
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        repeatPasswordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0 != null) {
                    if (p0.toString() == (passwordEditText.text.toString())) {
                        repeatCorrectness = true
                        psRepeat.setTextColor(resources.getColor(R.color.black))
                    } else {
                        repeatCorrectness = false
                        psRepeat.setTextColor(resources.getColor(R.color.wrong_red))
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        confirmButton.setOnClickListener {
            if (currentPasswordCorrectness &&
                    passwordCorrectness &&
                    repeatCorrectness) {
                geoCacheVM.getUsers().observe(this@ResetPasswordFragment, Observer<List<User>> {
                    var user = it.find { u -> u.email == email }!!
                    geoCacheVM.update(User(
                            uid = user.uid,
                            name = user.name,
                            email = user.email,
                            password = passwordEditText.text.toString(),
                            isAdmin = user.isAdmin
                    ))
                })
                this.activity!!.finish()
            }
            if (!currentPasswordCorrectness) Toast.makeText(this.context, "Wrong Password", Toast.LENGTH_SHORT).show()
            else Toast.makeText(this.context, "Your password does not fulfill the requirements", Toast.LENGTH_SHORT).show()
        }

    }

    private fun passwordChecker(ps: CharSequence?): Boolean {
        var req1 = false
        var req2 = false
        var req3 = false
        if (ps != null) {
            if (ps.length > 8) {
                req1 = true
                psReq1.setTextColor(resources.getColor(R.color.black))
            } else {
                req1 = false
                psReq1.setTextColor(resources.getColor(R.color.wrong_red))
            }
            if (ps.any { c -> c.isDigit() } && ps.any { c -> c.isLetter() }) {
                req2 = true
                psReq2.setTextColor(resources.getColor(R.color.black))
            } else {
                req2 = false
                psReq2.setTextColor(resources.getColor(R.color.wrong_red))
            }
            if (ps.any { c -> c.isUpperCase() } && ps.any { c -> c.isLowerCase() }) {
                req3 = true
                psReq3.setTextColor(resources.getColor(R.color.black))
            } else {
                req3 = false
                psReq3.setTextColor(resources.getColor(R.color.wrong_red))
            }
            return req1 && req2 && req3
        } else
            return false
    }

}