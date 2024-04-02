package com.example.imsafeapp.community.user

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imsafeapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CommunityActivity : AppCompatActivity() {

    val communities = listOf(
        "Aldona", "Benaulim", "Bicholim", "Calangute", "Canacona", "Cortalim", "Cuncolim",
        "Cumbarjua", "Curchorem", "Curtorim", "Dabolim", "Fatorda", "Margao", "Maem", "Mandrem",
        "Marcaim", "Mapusa", "Mormugao", "Navelim", "Nuvem", "Panaji", "Pernem (SC)", "Ponda",
        "Poriem", "Porvorim", "Priol", "Quepem", "Saligao", "Sanquelim", "Sanvordem", "Santa Cruz",
        "Sanguem", "Siroda", "Siolim", "St. Andre", "Taleigao", "Tivim", "Valpoi", "Vasco Da Gama", "Velim"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_community)
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/

        var selectedConstituency: String? = null

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewCommunity)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId!!)
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val selectedConstituency = dataSnapshot.child("selectedConstituency").value.toString()

                recyclerView.adapter = CommunityAdapter(communities, selectedConstituency!!)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                //possible errors
            }
        })


    }
}