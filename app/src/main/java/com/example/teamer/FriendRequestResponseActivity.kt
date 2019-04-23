package com.example.teamer

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.teamer.data.FriendRequest
import com.example.teamer.service.FriendRequestService

class FriendRequestResponseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_request_response)

        val friendRequest = intent.getSerializableExtra(
            FriendRequestService.FRIEND_REQUEST_INTENT) as? FriendRequest

        val btn : Button = findViewById(R.id.button)

        btn.setOnClickListener {
            finish()
        }
    }
}
