package com.example.proyekandroid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterPage.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterPage : Fragment() {
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
        return inflater.inflate(R.layout.fragment_register_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val _firstName = view.findViewById<EditText>(R.id.input_firstname)
        val _lastName = view.findViewById<EditText>(R.id.input_lastname)
        val _inputEmail = view.findViewById<EditText>(R.id.input_email)
        val _inputPassword = view.findViewById<EditText>(R.id.input_password)
        val _btnRegisterAccount = view.findViewById<Button>(R.id.button_register_account)
        val _alreadyHaveAccount = view.findViewById<TextView>(R.id.already_have_account)

        val db = Firebase.firestore


        //Button dalam bentuk text view untuk kembali ke login page jika user sudah punya akun
        _alreadyHaveAccount.setOnClickListener{
            val fragment_login = LoginOrRegisterPage()
            val mFragmentManager = parentFragmentManager
            mFragmentManager.beginTransaction().apply {
                replace(R.id.frameContainer, fragment_login, LoginOrRegisterPage::class.java.simpleName)
                //addToBackStack(null)
                commit()
            }
        }



        //button untuk melakukan register
        _btnRegisterAccount.setOnClickListener {

            //mengecek agar user tidak mengisi data kosong
            if (_firstName.text.isEmpty() || _lastName.text.isEmpty() || _inputEmail.text.isEmpty() || _inputPassword.text.isEmpty()) {
                Toast.makeText(activity, "Please enter data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            println(_firstName.text.toString())
            println(_lastName.text.toString())
            println(_inputEmail.text.toString())
            println(_inputPassword.text.toString())

            val fragment_to_home = HomePage()
            val mFragmentManager = parentFragmentManager

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(_inputEmail.text.toString(), _inputPassword.text.toString())
                .addOnCompleteListener() {
                    if (it.isSuccessful) {//kalau signup berhasil dilakukan
                        val _uid = FirebaseAuth.getInstance().uid?:""
                        val newUser = User(_uid,_firstName.text.toString(),_lastName.text.toString(), _inputEmail.text.toString())
                        db.collection("Users")
                            .add(newUser)

//                        mFragmentManager.beginTransaction().apply {
//                            replace(R.id.frameContainer, fragment_to_home, HomePage::class.java.simpleName)
//                            commit()
//                        }
                        goToAttract()
                        println("Succesfully created user : ${it.result?.user?.uid}")
                    }
                }
                .addOnFailureListener {//jika signup gagal
                    Toast.makeText(activity, "Failed to create user : ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }


    class User(val uid: String, val firstname: String, val lastname : String, val email: String)

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
         * @return A new instance of fragment RegisterPage.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterPage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}