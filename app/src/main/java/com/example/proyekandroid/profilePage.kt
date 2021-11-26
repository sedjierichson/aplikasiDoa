package com.example.proyekandroid

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import com.example.proyekandroid.databinding.FragmentDukunganDoaBinding
import com.example.proyekandroid.databinding.FragmentProfilePageBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [profilePage.newInstance] factory method to
 * create an instance of this fragment.
 */
class profilePage : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    val db = Firebase.firestore


    private var _binding: FragmentProfilePageBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentProfilePageBinding.inflate(inflater,container,false)
        val view = binding.root
        return view

        //return inflater.inflate(R.layout.fragment_profile_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val _tvProfileName = view.findViewById<TextView>(R.id.tvProfileName)
//        val _tvProfileEmail = view.findViewById<TextView>(R.id.tvProfileEmail)
//        val _tvProfilePassword = view.findViewById<TextView>(R.id.tvProfileHP)
        val _signout = view.findViewById<ImageView>(R.id.logoutButton)
        val _btnEditProfile = view.findViewById<Button>(R.id.btnEditProfile)


        var _etNamaProfile = view.findViewById<EditText>(R.id.etNamaProfile)
        var _etLastname = view.findViewById<EditText>(R.id.etLastname)
        var _etEmail = view.findViewById<EditText>(R.id.etEmail)

        val currentUser = Firebase.auth.currentUser
        val currenUID = Firebase.auth.uid
        val reference = db.collection("Users")

        reference.whereEqualTo("uid", currenUID).get()
            .addOnCompleteListener{
            if(it.isSuccessful) {
                for (document in it.result!!) {
//                    _tvProfileName.text = document.data.getValue("firstname")
//                        .toString() + " " + document.data.getValue("lastname").toString()
//                    _tvProfileEmail.text = document.data.getValue("email").toString()
                    _etNamaProfile.setText(document.data.getValue("firstname").toString())
                    _etLastname.setText(document.data.getValue("lastname").toString())
                    _etEmail.setText(document.data.getValue("email").toString())


                }
            }
            }
        reference.whereEqualTo("uid",currenUID).addSnapshotListener { snapshot, e ->
            if (e != null){
                println ("Listen failed." + e)
                return@addSnapshotListener
            }

        }

        _signout.setOnClickListener{
            Firebase.auth.signOut()
            goToMain()
        }

        _btnEditProfile.setOnClickListener {
            //view.findNavController().navigate(R.id.action_profilePage_to_editProfile)
            var namaBaru = _etNamaProfile.text.toString()
            var lastnameBaru = _etLastname.text.toString()
            var emailBaru = _etEmail.text.toString()
            println(namaBaru)
            reference.whereEqualTo("uid", currenUID).get().addOnCompleteListener {
                if (it.isSuccessful) {
                    for (document in it.result!!) {
                        reference.document(document.id).update("firstname", namaBaru).addOnSuccessListener {
                            Toast.makeText(activity,"Berhasil mengubah firstname",Toast.LENGTH_SHORT).show()
                        }
                        reference.document(document.id).update("lastname", lastnameBaru).addOnSuccessListener {
                            Toast.makeText(activity,"Berhasil mengubah firstname",Toast.LENGTH_SHORT).show()
                        }
//                        reference.document(document.id).update("email", emailBaru).addOnSuccessListener {
//                            Toast.makeText(activity,"Berhasil mengubah firstname",Toast.LENGTH_SHORT).show()
//                        }
                    }
                }
            }
        }

    }
    fun goToMain() {
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment profilePage.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            profilePage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}