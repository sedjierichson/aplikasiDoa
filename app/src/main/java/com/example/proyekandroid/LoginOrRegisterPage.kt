package com.example.proyekandroid

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginOrRegisterPage.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginOrRegisterPage : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_or_register_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val _button_register = view.findViewById<Button>(R.id.button_to_Register_Page)
        val _button_login = view.findViewById<Button>(R.id.button_login)
        val _login_email = view.findViewById<EditText>(R.id.login_input_email)
        val _login_password = view.findViewById<EditText>(R.id.login_input_password)

        _button_register.setOnClickListener {
            val fragment_register = RegisterPage()
            val mFragmentManager = parentFragmentManager
            mFragmentManager.beginTransaction().apply {
                replace(R.id.frameContainer, fragment_register, RegisterPage::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }

        _button_login.setOnClickListener {
            val fragment_to_home = HomePage()
            val mFragmentManager = parentFragmentManager
            if (_login_email.text.isEmpty() || _login_password.text.isEmpty()){
                Toast.makeText(activity, "Please enter email/password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            FirebaseAuth.getInstance().signInWithEmailAndPassword(_login_email.text.toString(),_login_password.text.toString())
                .addOnCompleteListener{
                    if (it.isSuccessful){
//                        mFragmentManager.beginTransaction().apply {
//                            replace(R.id.frameContainer, fragment_to_home, HomePage::class.java.simpleName)
//                            commit()
//                        }
                        goToAttract()
                    }
                }
                .addOnFailureListener{
                    Toast.makeText(activity, "Failed to login : ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
    fun goToAttract() {
        val intent = Intent(activity, user_Activity::class.java)
        startActivity(intent)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginOrRegisterPage.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginOrRegisterPage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}