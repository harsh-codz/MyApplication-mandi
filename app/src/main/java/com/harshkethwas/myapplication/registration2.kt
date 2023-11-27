package com.harshkethwas.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URL

class registration2 : AppCompatActivity() {

    private lateinit var btn: Button
    private val url = URL(URLs.urlUsers)
    private var postData:String = "";

    var fullname:String = ""
    var email:String=""
    var city:String=""

    lateinit var txtFullName:EditText
    lateinit var txtEmail:EditText
    lateinit var txtCity:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration2)

        btn = findViewById(R.id.RBtn)
        txtFullName = findViewById(R.id.fullName)
        txtEmail = findViewById(R.id.emailId)
        txtCity = findViewById(R.id.city)

        btn.setOnClickListener {
            if(validateEntries(txtFullName,txtEmail,txtCity))
            {
                GlobalScope.launch(Dispatchers.Main){
                    addUser()
                }
            }
            else
            {
                Toast.makeText(this,"Invalid inputs for fullname, email or city",Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun validateEntries(txtFullName:EditText,txtEmail:EditText,txtCity:EditText):Boolean
    {
        var valid:Boolean=true

        if(txtFullName.text.toString().isEmpty() || txtCity.text.toString().isEmpty() || txtEmail.text.toString().isEmpty())
        {
            valid = false
        }
        else
        {
            fullname=txtFullName.text.toString().uppercase()
            email = txtEmail.text.toString()
            city = txtCity.text.toString()
        }
        return valid
    }
    private suspend fun addUser(){
        postData = "fullname=$fullname" + "&email=$email" + "&city=$city"
        val httpConnection = HttpConnect.HttpConnection()
        var res = httpConnection.connect(url,postData)

        Toast.makeText(this,res,Toast.LENGTH_SHORT).show()
    }

}