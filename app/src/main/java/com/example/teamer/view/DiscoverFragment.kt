package com.example.teamer.view


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.example.teamer.R
import com.example.teamer.data.UserData
import com.example.teamer.model.CardStackAdapter
import com.example.teamer.model.TeamerVM
import com.yuyakaido.android.cardstackview.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class DiscoverFragment : Fragment() {

    private lateinit var vm : TeamerVM
    private val manager by lazy { CardStackLayoutManager(context, MyCardStackListener()) }

    inner class MyCardStackListener: CardStackListener {
        override fun onCardDisappeared(view: View?, position: Int) {
        }

        override fun onCardDragging(direction: Direction?, ratio: Float) {
        }

        override fun onCardSwiped(direction: Direction?) {
            if (direction == Direction.Right) {
                Log.d("CardStackView", "onCardSwiped: p = ${vm.discoverProfileData.value?.get(manager.topPosition-1)}, d = $direction")
                vm.sendFriendRequest(vm.discoverProfileData.value?.get(manager.topPosition-1)?.uid!!)
            }
        }

        override fun onCardCanceled() {
        }

        override fun onCardAppeared(view: View?, position: Int) {
        }

        override fun onCardRewound() {
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val viewF = inflater.inflate(R.layout.fragment_discover, container, false)

        vm = activity.run {
            ViewModelProviders.of(this!!).get(TeamerVM::class.java)
        }

        // tinder card stack initialization
        val cardStackView = viewF.findViewById<CardStackView>(R.id.card_stack_view)

        // set parameters

        manager.setStackFrom(StackFrom.None)
        manager.setVisibleCount(1)
        manager.setTranslationInterval(8.0f)
        manager.setScaleInterval(0.95f)
        manager.setSwipeThreshold(0.3f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL)
        manager.setCanScrollHorizontal(true)
        manager.setCanScrollVertical(true)
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)

        cardStackView.layoutManager = manager

        vm.updateDiscoverProfilesData()

        vm.updateDiscoverProfilesData().observe(this@DiscoverFragment, Observer { discoverProfileData ->
            cardStackView.adapter = CardStackAdapter(discoverProfileData as List<UserData>)
        })




        // Inflate the layout for this fragment
        return viewF
    }


}
