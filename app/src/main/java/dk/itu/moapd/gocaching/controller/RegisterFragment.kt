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
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import dk.itu.moapd.gocaching.R
import dk.itu.moapd.gocaching.User
import dk.itu.moapd.gocaching.controller.LoginFragment.Companion.geoCacheVM
import dk.itu.moapd.gocaching.model.database.GeoCacheViewModel
import kotlinx.android.synthetic.main.register_fragment.*
import kotlinx.android.synthetic.main.user_profile_menu.*

class RegisterFragment:Fragment(){
    private val emailPattern = Regex("^[a-zA-Z0-9.!#\$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*\$")
    private lateinit var psReq1 : TextView
    private lateinit var psReq2 : TextView
    private lateinit var psReq3 : TextView
    private lateinit var psRepeat : TextView
    private lateinit var emailEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var repeatPasswordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var users : List<User>
    private var nameCorrectness = false
    private var passwordCorrectness = false
    private var emailCorrectness = false
    private var repeatCorrectness = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.register_fragment,container,false)
        emailEditText = view.findViewById(R.id.register_email_textfield)
        nameEditText = view.findViewById(R.id.register_name_textfield)
        passwordEditText = view.findViewById(R.id.register_password_textfield)
        repeatPasswordEditText = view.findViewById(R.id.repeat_password_textfield)
        registerButton = view.findViewById(R.id.register_account_button)
        psReq1 = view.findViewById(R.id.register_ps_req1)
        psReq2 = view.findViewById(R.id.register_ps_req2)
        psReq3 = view.findViewById(R.id.register_ps_req3)
        psRepeat = view.findViewById(R.id.ps_repeat_req)
        geoCacheVM.getUsers().observe(this, Observer<List<User>>{
            users = it
        })
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nameEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0 != null) {
                    if (p0.length < 6){
                        nameEditText.setTextColor(resources.getColor(R.color.wrong_red))
                        nameCorrectness = false
                    } else {
                        nameEditText.setTextColor(resources.getColor(R.color.black))
                        nameCorrectness = true
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        emailEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.let { emailPattern.matches(it) } == true){
                    emailCorrectness = true
                    emailEditText.setTextColor(resources.getColor(R.color.black))
                } else {
                    emailCorrectness = false
                    emailEditText.setTextColor(resources.getColor(R.color.wrong_red))
                }
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

        repeatPasswordEditText.addTextChangedListener(object : TextWatcher{
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

        registerButton.setOnClickListener {
            if(nameCorrectness &&
                    passwordCorrectness &&
                    emailCorrectness &&
                    repeatCorrectness
                    && !users.any{us -> us.email == emailEditText.text.toString()}){
                geoCacheVM.insert(User(
                        name = nameEditText.text.toString(),
                        email = emailEditText.text.toString(),
                        password = passwordEditText.text.toString()
                ))
                activity?.finish()
            }
            if (emailEditText.text.toString() == "admin"
                    && passwordEditText.text.toString() == "admin"
                    && !users.any { us -> us.email == "admin" && us.password == "admin"} ){
                geoCacheVM.insert(User(
                        name = "admin",
                        email = "admin",
                        password = "admin",
                        isAdmin = true
                ))
                activity?.finish()
            }
            else {
                Toast.makeText(this.context,"Invalid registration",Toast.LENGTH_SHORT)
            }
        }
    }

    private fun passwordChecker(ps:CharSequence?):Boolean{
        var req1 = false
        var req2 = false
        var req3 = false
        if (ps != null){
            if (ps.length > 8) {
                req1 = true
                psReq1.setTextColor(resources.getColor(R.color.black))
            } else {
                req1 = false
                psReq1.setTextColor(resources.getColor(R.color.wrong_red))
            }
            if(ps.any{ c -> c.isDigit()} && ps.any{ c -> c.isLetter()}){
                req2 = true
                psReq2.setTextColor(resources.getColor(R.color.black))
            }
            else {
                req2 = false
                psReq2.setTextColor(resources.getColor(R.color.wrong_red))
            }
            if(ps.any{ c -> c.isUpperCase()} && ps.any{ c -> c.isLowerCase()}){
                req3 = true
                psReq3.setTextColor(resources.getColor(R.color.black))
            }
            else {
                req3 = false
                psReq3.setTextColor(resources.getColor(R.color.wrong_red))
            }
            return req1 && req2 && req3
        } else
            return false
    }
}