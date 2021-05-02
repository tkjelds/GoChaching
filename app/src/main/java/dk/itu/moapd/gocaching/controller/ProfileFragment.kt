package dk.itu.moapd.gocaching.controller

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import dk.itu.moapd.gocaching.R
import dk.itu.moapd.gocaching.User
import dk.itu.moapd.gocaching.UserWithCaches
import dk.itu.moapd.gocaching.controller.LoginFragment.Companion.geoCacheVM
import dk.itu.moapd.gocaching.model.database.Difficulty


class ProfileFragment : Fragment() {
    private lateinit var nameTextField: TextView
    private lateinit var emailTextField: TextView
    private lateinit var easyCachesTextField: TextView
    private lateinit var mediumCachesTextField: TextView
    private lateinit var hardCachesTextField: TextView
    private lateinit var resetPasswordButton: Button
    private lateinit var user: User
    private lateinit var userChaches: UserWithCaches
    private lateinit var userEmail: String
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.user_profile_menu, container, false)
        nameTextField = view.findViewById(R.id.profile_name)
        emailTextField = view.findViewById(R.id.profile_email)
        easyCachesTextField = view.findViewById(R.id.profile_easy_cache_number)
        mediumCachesTextField = view.findViewById(R.id.profile_medium_cache_number)
        hardCachesTextField = view.findViewById(R.id.profile_hard_cache_number)
        resetPasswordButton = view.findViewById(R.id.reset_password_button)
        userEmail = arguments!!.getString("email")
        return view
    }

    override fun onStart() {
        super.onStart()
        geoCacheVM.getUsers().observe(this, Observer<List<User>> {
            user = it.find { user -> user.email == userEmail }!!
            nameTextField.text = user.name
            emailTextField.text = user.email
            geoCacheVM.getUserWithCaches().observe(this, Observer<List<UserWithCaches>> {
                userChaches = it.find { userwithcache -> userwithcache.user.uid == user.uid }!!
                var easyCaches = userChaches.caches.count { cache -> cache.difficulty == Difficulty.EASY }
                var mediumCaches = userChaches.caches.count { cache -> cache.difficulty == Difficulty.MEDIUM }
                var hardCaches = userChaches.caches.count { cache -> cache.difficulty == Difficulty.HARD }
                easyCachesTextField.text = easyCaches.toString()
                mediumCachesTextField.text = mediumCaches.toString()
                hardCachesTextField.text = hardCaches.toString()
            })

        })
        resetPasswordButton.setOnClickListener {
            val intent = Intent(activity, ResetPasswordActivity::class.java).apply {
                putExtra("email", userEmail)
            }
            startActivity(intent)
        }
    }

}