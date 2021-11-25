package com.example.proyekandroid

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyekandroid.databinding.FragmentDukunganDoaBinding
import com.example.proyekandroid.databinding.FragmentPostDoaBinding
import com.getbase.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.parcelize.Parcelize

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [dukunganDoa.newInstance] factory method to
 * create an instance of this fragment.
 */
class dukunganDoa : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userArrayList: ArrayList<listDoa>

    private var _binding: FragmentDukunganDoaBinding? = null
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
        _binding = FragmentDukunganDoaBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnTambahDoa = view.findViewById<Button>(R.id.btnTambahDoa)
        val db = Firebase.firestore

        binding.btnTambahDoa.setOnClickListener {
            view.findNavController().navigate(R.id.action_dukunganDoa_to_postDoa)
        }

        userRecyclerView = view.findViewById(R.id.recyclerViewDoa)
        //userRecyclerView.layoutManager = LinearLayoutManager(this)

        userRecyclerView.setHasFixedSize(true)
        userArrayList = arrayListOf<listDoa>()

        val referenceWujudDoa = db.collection("Wujud Doa")



//        referenceWujudDoa.get().addOnSuccessListener {
//            var listUser: ArrayList<listDoa> = ArrayList()
//            listUser.clear()
//            for (document in it){
//                listUser.add((listDoa(
//                    document.data.get("sender") as String,
//                    document.data.get("doa") as String,
//                    document.data.get("jumlahDukungan") as String
//                )))
//            }
//            var myAdapter = MyAdapter(listUser)
//            userRecyclerView.apply {
//                layoutManager = LinearLayoutManager(context)
//                adapter = myAdapter
//            }
//
//            myAdapter.setOnItemClickListener {
//                for (document in listUser){
//                    println(document.sender)
//                    println(document.doa)
//                }
//            }
//
//        }


        //Pakai ini utk mendapat realtime update langsung ketika ada yang mendukung doa
        referenceWujudDoa.addSnapshotListener { snapshot, e ->
            if (e != null){
                println ("Listen failed." + e)
                return@addSnapshotListener
            }
            var listUser: ArrayList<listDoa> = ArrayList()
            listUser.clear()
            for (document in snapshot!!){
                listUser.add((listDoa(
                    document.data.get("sender") as String,
                    document.data.get("doa") as String,
                    document.data.get("jumlahDukungan") as String
                )))
            }
            var myAdapter = MyAdapter(listUser)
            userRecyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = myAdapter
            }

            myAdapter.setOnItemClickListener {
                for (document in listUser){
                    println(document.sender)
                    println(document.doa)
                }
            }


        }



    }

    @Parcelize
    class listDoa(val sender: String, val doa: String, val jumlah: String) : Parcelable

    class MyAdapter (private val userList: ArrayList<listDoa>): RecyclerView.Adapter<MyAdapter.MyHolderView>(){


        private var listener: ((item: listDoa) -> Unit)? = null

        fun setOnItemClickListener(listener: (item: listDoa) -> Unit) {
            this.listener = listener
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_wujud_doa,
            parent, false)

            return MyHolderView(itemView)
        }

        override fun onBindViewHolder(holder: MyHolderView, position: Int) {
            val currentItem = userList[position]
            val db = Firebase.firestore
            val reference = db.collection("Wujud Doa")
            var jumlahDukungan = ""

            holder.userName.text = currentItem.sender
            holder.doa.text = currentItem.doa
            holder.tvjumlahDukungan.text = currentItem.jumlah.toString() + " orang telah mendukung"


            holder.btnDukungDoa.setOnClickListener {
                //println("Posisi = " + position + "Nama Pengirim " + currentItem.sender)
                listener?.invoke(userList[position])

                reference.whereEqualTo("doa",currentItem.doa).get().addOnCompleteListener {
                    if (it.isSuccessful){
                        for (document in it.result!!){
                            val msgID = document.data.getValue("msgId").toString()
                            println("MSG ID = " + msgID)

                            jumlahDukungan = document.data.getValue("jumlahDukungan").toString()
                            println("Jumlah Dukungan = " + jumlahDukungan)

                            var tambah = jumlahDukungan.toInt() + 1
                            var tambah2 = tambah.toString()

                            println(document.id)
                            //Update tambah dukungan doa
                            reference.document(document.id).update("jumlahDukungan", tambah2).addOnSuccessListener {
                                Toast.makeText(holder.itemView.context,"Berhasil Mendukung Doa " + holder.userName.text, Toast.LENGTH_SHORT).show()
                            }
                        }

                    }
                }


                //print("Nama Pengirim " + currentItem.sender)
                //listener.onItemClick(position)
            }
        }

        override fun getItemCount(): Int {
            return userList.size
        }
        class MyHolderView(itemView: View): RecyclerView.ViewHolder (itemView){

            val userName = itemView.findViewById<TextView>(R.id.tvNamaPengirim)
            val doa = itemView.findViewById<TextView>(R.id.tvWujudDoa)
            val btnDukungDoa = itemView.findViewById<Button>(R.id.btnTambahDukunganDoa)
            val tvjumlahDukungan = itemView.findViewById<TextView>(R.id.tvJumlahDukungan)

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment dukunganDoa.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            dukunganDoa().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}