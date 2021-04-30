package dk.itu.moapd.gocaching.controller

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import dk.itu.moapd.gocaching.R
import dk.itu.moapd.gocaching.User
import dk.itu.moapd.gocaching.model.database.GeoCacheViewModel
import java.util.regex.Pattern

class LoginFragment:Fragment() {
    private val emailPattern = Regex(";^[a-zA-Z0-9.!#\$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*\$;")
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button

    companion object{
        lateinit var geoCacheVM: GeoCacheViewModel
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.login_fragment,container,false)
        emailEditText = view.findViewById(R.id.login_email_textfield)
        passwordEditText = view.findViewById(R.id.login_password_textfield)
        loginButton = view.findViewById(R.id.login_button)
        registerButton = view.findViewById(R.id.new_account_button)
        geoCacheVM = ViewModelProviders.of(this).get(GeoCacheViewModel::class.java)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginButton.setOnClickListener {
            geoCacheVM.getUsers().observe(this, Observer<List<User>>{
                var users = it
            if (emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()){
                val email = emailEditText.text.toString()
                val user = users.find { user -> user.email == email}
                if (user != null && user.password == passwordEditText.text.toString() ){
                    val intent = Intent(activity, GoCachingActivity::class.java).apply {
                        putExtra("email", user.email)
                    }
                    startActivity(intent)
                }
            }
            })
        }
        registerButton.setOnClickListener {
            val intent = Intent(activity,RegisterActivity::class.java)
            startActivity(intent)
        }

    }


}