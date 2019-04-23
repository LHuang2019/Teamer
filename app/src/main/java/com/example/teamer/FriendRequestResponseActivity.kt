package com.example.teamer

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.teamer.data.FriendRequest
import com.example.teamer.model.TeamerVM
import com.example.teamer.service.FriendRequestService

class FriendRequestResponseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_request_response)

        val friendRequest = intent.getSerializableExtra(
            FriendRequestService.FRIEND_REQUEST_INTENT) as? FriendRequest

        Log.d("test", friendRequest.toString())

        val userData = friendRequest?.sender

        val vm = this.run {
            ViewModelProviders.of(this).get(TeamerVM::class.java)
        }

        findViewById<TextView>(R.id.f_view_profile_username_tv).text = userData?.username

        val platformStr = userData?.platforms?.joinToString { platform -> platform }
        findViewById<TextView>(R.id.f_view_profile_platform_list_tv).text = platformStr

        val gameStr = userData?.games?.joinToString { game -> game }
        findViewById<TextView>(R.id.f_view_profile_game_list_tv).text = gameStr


        findViewById<Button>(R.id.a_friend_request_accept_btn).setOnClickListener {
            friendRequest?.uid?.let { uid -> vm.removeFriendRequest(uid) }
            friendRequest?.recipient?.let { recipient -> vm.addFriend(recipient, friendRequest.sender) }
            finish()
        }

        findViewById<Button>(R.id.a_friend_request_decline_btn).setOnClickListener {
            friendRequest?.uid?.let { uid -> vm.removeFriendRequest(uid) }
            finish()
        }
    }
}
