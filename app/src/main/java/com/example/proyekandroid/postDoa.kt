package com.example.proyekandroid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [postDoa.newInstance] factory method to
 * create an instance of this fragment.
 */
class postDoa : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    val db = Firebase.firestore

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
        return inflater.inflate(R.layout.fragment_post_doa, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val _btnKirimWujudDoa = view.findViewById<Button>(R.id.btnKirimWujudDoa)
        val _editTextWujudDoa = view.findViewById<EditText>(R.id.editTextWujudDoa)

        val currentUser = Firebase.auth.currentUser
        val currentUID = Firebase.auth.uid
        val referenceWujudDoa = db.collection("Wujud Doa")
        val currentDateTime = LocalDateTime.now()


        _btnKirimWujudDoa.setOnClickListener {
            if (_editTextWujudDoa.text.isEmpty()){
                Toast.makeText(activity, "Masukkan Wujud Doa Anda", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //Get Current User Profile Name
            val referenceCurrentUser = db.collection("Users")
            var displayName : String = ""

            referenceCurrentUser.whereEqualTo("uid", currentUID).get().addOnCompleteListener{
                if(it.isSuccessful){
                    for (document in it.result!!){
                        displayName = document.data.getValue("firstname").toString() + " " + document.data.getValue("lastname").toString()
                        println(displayName)
                    }
                    val _uid = FirebaseAuth.getInstance().uid?:""
                    val messageID = referenceWujudDoa.document().id
                    val _jumlahDukungan = 0
                    val wujudDoa = wujudDoa(_uid, displayName , _editTextWujudDoa.text.toString(), currentDateTime.toString(), messageID, _jumlahDukungan.toString())

                    println("ID BARU WUJUD DOA " + messageID)

                    referenceWujudDoa.add(wujudDoa).addOnSuccessListener {
                        Toast.makeText(activity, "Wujud Doa berhasil dikirim", Toast.LENGTH_SHORT).show()

                    }
                        .addOnFailureListener{
                            Toast.makeText(activity, "Harap coba lagi", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            println("coba" + displayName)

            //Save wujud doa

        }

    }
    class wujudDoa (val uid: String, val sender: String, val doa: String, val time: String, val msgId: String, val jumlahDukungan: String)

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment postDoa.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            postDoa().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}