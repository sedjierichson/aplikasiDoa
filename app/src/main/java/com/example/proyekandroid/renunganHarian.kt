package com.example.proyekandroid

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.proyekandroid.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [renunganHarian.newInstance] factory method to
 * create an instance of this fragment.
 */
class renunganHarian : Fragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val db = Firebase.firestore
    lateinit var _pickDate : ImageView
    lateinit var _tvJudulRenungan : TextView
    lateinit var _tvIsiRenungan : TextView
    lateinit var _tvTanggal : TextView

    //lateinit var binding : ActivityMainBinding

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
        return inflater.inflate(R.layout.fragment_renungan_harian, container, false)
    }

    var day = 0
    var month = 0
    var year = 0
    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0
    var currentDate = 0

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        val reference = db.collection("Renungan")

        savedDay = dayOfMonth
        savedMonth = month
        savedYear= year

        _tvTanggal.text = "$savedDay/$savedMonth/$savedYear"

//        reference.whereEqualTo("tanggal", _tvTanggal.text).addSnapshotListener { snapshot, e ->
//            if (e != null){
//                println ("Listen failed." + e)
//                return@addSnapshotListener
//            }
//            for (document in snapshot!!){
//                _tvJudulRenungan.text = document.data.getValue("judul").toString()
//                _tvIsiRenungan.text = document.data.getValue("isi").toString()
//            }
//
//        }

    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {

    }

    private fun getDateTimeCalendar(){
        val cal : Calendar = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONDAY)
        year = cal.get(Calendar.YEAR)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _tvJudulRenungan = view.findViewById<TextView>(R.id.tvJudulRenungan)
        _tvIsiRenungan = view.findViewById<TextView>(R.id.tvIsiRenungan)
        _tvTanggal = view.findViewById<TextView>(R.id.tvTanggal)
        val reference = db.collection("Renungan")
        val _gambarRenungan = view.findViewById<ImageView>(R.id.gambarRenungan)
        _pickDate = view.findViewById<ImageView>(R.id.pickDate)

        val sdf = SimpleDateFormat("dd/M/yyyy")
        val sdfGambar = SimpleDateFormat("dd-M-yyyy")
        var currentDate = sdf.format(Date())
        val currentImage = sdfGambar.format(Date())
        _tvTanggal.text = currentDate


        _pickDate.setOnClickListener{
            DatePickerDialog(requireActivity(), this, year, month, day).show()
            DatePickerDialog.BUTTON_POSITIVE
            //println(year +  month + day)
            //println("Dari tombol" + currentDate)

            println("TESSSS" + savedYear)
            reference.whereEqualTo("tanggal", _tvTanggal.text).get()
                .addOnCompleteListener {
                    for (document in it.result!!){
                        _tvJudulRenungan.text = document.data.getValue("judul").toString()
                        _tvIsiRenungan.text = document.data.getValue("isi").toString()
                    }
                }
        }

        //println(currentDate)

        //System.out.println(" C DATE is  "+ currentDate)
        reference.whereEqualTo("tanggal", _tvTanggal.text).get()
            .addOnCompleteListener {
                for (document in it.result!!){
                    _tvJudulRenungan.text = document.data.getValue("judul").toString()
                    _tvIsiRenungan.text = document.data.getValue("isi").toString()
                }
            }

//        reference.whereEqualTo("tanggal", _tvTanggal.text).addSnapshotListener { snapshot, e ->
//            if (e != null){
//                println ("Listen failed." + e)
//                return@addSnapshotListener
//            }
//            for (document in snapshot!!){
//                _tvJudulRenungan.text = document.data.getValue("judul").toString()
//                _tvIsiRenungan.text = document.data.getValue("isi").toString()
//            }
//
//        }

        //Retrieve Image dari Firebase Storage
        val imageRef = FirebaseStorage.getInstance().reference.child("Renungan/$currentImage.jpg")
        val localFile = File.createTempFile("tempImage","jpg")

        imageRef.getFile(localFile)
            .addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            _gambarRenungan.setImageBitmap(bitmap)
            }
            .addOnFailureListener{
                Toast.makeText(activity, "Failed To Retrieve Image", Toast.LENGTH_LONG).show()
            }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment renunganHarian.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            renunganHarian().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }



}