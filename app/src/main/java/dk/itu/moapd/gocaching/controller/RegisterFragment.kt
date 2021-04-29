package dk.itu.moapd.gocaching.controller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import dk.itu.moapd.gocaching.R
import dk.itu.moapd.gocaching.User
import dk.itu.moapd.gocaching.controller.LoginFragment.Companion.geoCacheVM
import dk.itu.moapd.gocaching.model.database.GeoCacheViewModel

class RegisterFragment:Fragment(){
    private val emailPattern = Regex(";^[a-zA-Z0-9.!#\$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*\$;")
    private lateinit var emailEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var repeatPasswordEditText: EditText
    private lateinit var registerButton: Button
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
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerButton.setOnClickListener {
            geoCacheVM.insert(User(
                name = nameEditText.text.toString(),
                email = emailEditText.text.toString(),
                password = passwordEditText.text.toString()
            )

            )
        }
    }
}