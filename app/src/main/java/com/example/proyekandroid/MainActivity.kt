package com.example.proyekandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val mFragmentManager = supportFragmentManager
        val fragmentAccount = LoginOrRegisterPage()

        mFragmentManager.findFragmentByTag(LoginOrRegisterPage::class.java.simpleName)
        mFragmentManager
            .beginTransaction()
            .add(R.id.frameContainer, fragmentAccount, LoginOrRegisterPage::class.java.simpleName)
            .commit()
    }
}