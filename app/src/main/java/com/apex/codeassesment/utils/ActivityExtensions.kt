package com.apex.codeassesment.utils

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.apex.codeassesment.data.model.User
import com.apex.codeassesment.ui.details.DetailsActivityKt

fun AppCompatActivity.navigateDetails(user: User){
    val putExtra = Intent(this, DetailsActivityKt::class.java).putExtra("saved-user-key", user)
    startActivity(putExtra)
}