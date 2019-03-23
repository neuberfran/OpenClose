package com.example.neube.smartdrive

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.neube.smartdrive.R.id.fecharJanela1
import com.example.neube.smartdrive.R.id.pararButton

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    internal var database = FirebaseDatabase.getInstance()
    internal var myRef = database.getReference()
    internal val janela1 = myRef.child("fcmotordoisa")
    internal val pararRef = myRef.child("MotorUm/Parar")

    var fcmotordoisa:Long? = null
    var parar:Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //updateText(janela1,textView1);

        janela1.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                fcmotordoisa = dataSnapshot.getValue(Long::class.java)
                if (fcmotordoisa == 1L) {
                    fecharJanela1.background = getDrawable(R.mipmap.jesqpretae)
                    fecharJanela1.isEnabled = false
                    pararButton.isEnabled = false
                } else if (fcmotordoisa == 0L) {
                    fecharJanela1.isEnabled = true
                    pararButton.isEnabled = true
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("file", "Failed to read value.", error.toException())
            }
        })

        pararRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                parar = dataSnapshot.getValue(Long::class.java)
                if (parar == 1L && fecharJanela1.isEnabled) {
                    fecharJanela1.background = getDrawable(R.mipmap.janelafrentee)
                    pararButton.background = getDrawable(R.mipmap.stopbb)
                } else if (parar == 0L && fecharJanela1.isEnabled) {
                    pararButton.background = getDrawable(R.mipmap.stopaa)
                    fecharJanela1.background = getDrawable(R.mipmap.jesqpretae)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("file", "Failed to read value.", error.toException())
            }
        })

        fecharJanela1.setOnClickListener {
            if (parar == 1L) {
                myRef.child("MotorUm/Direcao").setValue(1)
                myRef.child("MotorUm/Parar").setValue(0)
            }

//            if (fcmotordoisa == 0L) {
//                myRef.child("MotorUm/Direcao").setValue(1)
//                myRef.child("MotorUm/Parar").setValue(0)
//            }
        }

        pararButton.setOnClickListener {
            myRef.child("MotorUm/Parar").setValue(1)
            pararButton.background = getDrawable(R.mipmap.stopbb)
        }
    }
}