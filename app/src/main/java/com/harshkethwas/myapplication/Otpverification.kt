package com.harshkethwas.myapplication

import android.app.appsearch.GlobalSearchSession
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.*
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.json.JSONTokener
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit

class Otpverification : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var verifyBtn: Button
    private lateinit var resendTV: TextView
    private lateinit var inputOTP1: EditText
    private lateinit var inputOTP2: EditText
    private lateinit var inputOTP3: EditText
    private lateinit var inputOTP4: EditText
    private lateinit var inputOTP5: EditText
    private lateinit var inputOTP6: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var OTP: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var phoneNumber: String
    private lateinit var lottieAnimationView: LottieAnimationView

    private val url = URL(URLs.urlUserLogin)
    private var postData:String = ""
    private var contact:String=""

    var fullName:String=""
    var city:String=""
    var email:String=""
    var msg:String=""


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otpverification)

            OTP = intent.getStringExtra("OTP").toString()
            resendToken = intent.getParcelableExtra("resendToken")!!
            phoneNumber = intent.getStringExtra("phoneNumber")!!

            init()
        Handler(Looper.getMainLooper()).postDelayed({
            lottieAnimationView.cancelAnimation()
        }, 60000)

            progressBar.visibility = View.INVISIBLE
            addTextChangeListener()
            resendOTPTvVisibility()

            resendTV.setOnClickListener {
                resendVerificationCode()
                resendOTPTvVisibility()
            }

            verifyBtn.setOnClickListener {
                //collect otp from all the edit texts
                val typedOTP =
                    (inputOTP1.text.toString() + inputOTP2.text.toString() + inputOTP3.text.toString()
                            + inputOTP4.text.toString() + inputOTP5.text.toString() + inputOTP6.text.toString())

                if (typedOTP.isNotEmpty()) {
                    if (typedOTP.length == 6) {
                        val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
                            OTP, typedOTP
                        )
                        progressBar.visibility = View.VISIBLE
                        signInWithPhoneAuthCredential(credential)
                    } else {
                        makeText(this, "Please Enter Correct OTP", LENGTH_SHORT).show()
                    }
                } else {
                    makeText(this, "Please Enter OTP", LENGTH_SHORT).show()
                }


            }






        }

        private fun resendOTPTvVisibility() {
            inputOTP1.setText("")
            inputOTP2.setText("")
            inputOTP3.setText("")
            inputOTP4.setText("")
            inputOTP5.setText("")
            inputOTP6.setText("")
            resendTV.visibility = View.INVISIBLE
            resendTV.isEnabled = false

            Handler(Looper.myLooper()!!).postDelayed(Runnable {
                resendTV.visibility = View.VISIBLE
                resendTV.isEnabled = true
            }, 60000)
        }

        private fun resendVerificationCode() {
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)                 // Activity (for callback binding)
                .setCallbacks(callbacks)
                .setForceResendingToken(resendToken)// OnVerificationStateChangedCallbacks
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }

        private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Log.d("TAG", "onVerificationFailed: ${e.toString()}")
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Log.d("TAG", "onVerificationFailed: ${e.toString()}")
                }
                progressBar.visibility = View.VISIBLE
                // Show a message and update the UI
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                // Save verification ID and resending token so we can use them later
                OTP = verificationId
                resendToken = token
            }
        }

        private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
            auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information

                        makeText(this, "Authenticate Successfully", LENGTH_SHORT).show()
                        GlobalScope.launch(Dispatchers.Main){
                            sendToMain()
                        }
                    } else {
                        // Sign in failed, display a message and update the UI
                        Log.d("TAG", "signInWithPhoneAuthCredential: ${task.exception.toString()}")
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                        }
                        // Update UI
                    }
                    progressBar.visibility = View.VISIBLE
                }
        }

    private suspend fun sendToMain() {
            startActivity(Intent(this, registration2::class.java))
//        postData = "contact=$phoneNumber"
//        val httpConnection = HttpConnect.HttpConnection()
//        val res = httpConnection.connect(url,postData)
//
//        if(res=="no user found")
//        {
//            msg = "New User"
//           Toast.makeText(this,msg, Toast.LENGTH_LONG).show()
//            startActivity(Intent(this, registration2::class.java))
//        }
//        else
//        {
//            val jsonObject = JSONTokener(res).nextValue() as JSONObject
//            fullName = jsonObject.getString("fullname")
//            email = jsonObject.getString("email")
//            city = jsonObject.getString("city")
//
//            PassData.uFullname = fullName
//            PassData.uContact = phoneNumber
//            PassData.uEmail = email
//            PassData.uCity = city
//
//            msg = "Namaskar $fullName!"
//            Toast.makeText(this,msg, Toast.LENGTH_LONG).show()
//            startActivity((Intent(this, HomeFragment::class.java)))
//        }
    }

        private fun addTextChangeListener() {
            inputOTP1.addTextChangedListener(EditTextWatcher(inputOTP1))
            inputOTP2.addTextChangedListener(EditTextWatcher(inputOTP2))
            inputOTP3.addTextChangedListener(EditTextWatcher(inputOTP3))
            inputOTP4.addTextChangedListener(EditTextWatcher(inputOTP4))
            inputOTP5.addTextChangedListener(EditTextWatcher(inputOTP5))
            inputOTP6.addTextChangedListener(EditTextWatcher(inputOTP6))
        }

        private fun init() {
            auth = FirebaseAuth.getInstance()
            progressBar = findViewById(R.id.otpProgressBar)
            verifyBtn = findViewById(R.id.btn3)
            resendTV = findViewById(R.id.resendTextView)
            inputOTP1 = findViewById(R.id.inputotp1)
            inputOTP2 = findViewById(R.id.inputotp2)
            inputOTP3 = findViewById(R.id.inputotp3)
            inputOTP4 = findViewById(R.id.inputotp4)
            inputOTP5 = findViewById(R.id.inputotp5)
            inputOTP6 = findViewById(R.id.inputotp6)
            lottieAnimationView = findViewById(R.id.lav_actionBar)

        }


        inner class EditTextWatcher(private val view: View) : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {

                val text = p0.toString()
                when (view.id) {
                    R.id.inputotp1 -> if (text.length == 1) inputOTP2.requestFocus()
                    R.id.inputotp2 -> if (text.length == 1) inputOTP3.requestFocus() else if (text.isEmpty()) inputOTP1.requestFocus()
                    R.id.inputotp3 -> if (text.length == 1) inputOTP4.requestFocus() else if (text.isEmpty()) inputOTP2.requestFocus()
                    R.id.inputotp4 -> if (text.length == 1) inputOTP5.requestFocus() else if (text.isEmpty()) inputOTP3.requestFocus()
                    R.id.inputotp5 -> if (text.length == 1) inputOTP6.requestFocus() else if (text.isEmpty()) inputOTP4.requestFocus()
                    R.id.inputotp6 -> if (text.isEmpty()) inputOTP5.requestFocus()

                }
            }


    }
}