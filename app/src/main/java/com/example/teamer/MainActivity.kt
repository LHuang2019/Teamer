package com.example.teamer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.example.teamer.data.FriendRequest
import com.example.teamer.model.TeamerVM
import com.example.teamer.service.FriendRequestMessagingService

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val friendRequest = intent.getSerializableExtra(
            FriendRequestMessagingService.FRIEND_REQUEST_INTENT) as? FriendRequest

        if (friendRequest != null) {
            val vm = this.run {
                ViewModelProviders.of(this).get(TeamerVM::class.java)
            }

            if (vm.getCurrentUser() != null && vm.getCurrentUser()?.uid != friendRequest.recipientUid) {
                vm.logout()
            }
        }

        setContentView(R.layout.activity_main)
    }
}
