package com.example.teamer.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        cardStackView.layoutManager = CardStackLayoutManager(context)
        (cardStackView.layoutManager as CardStackLayoutManager).setStackFrom(StackFrom.None)
        (cardStackView.layoutManager as CardStackLayoutManager).setVisibleCount(1)
        (cardStackView.layoutManager as CardStackLayoutManager).setTranslationInterval(8.0f)
        (cardStackView.layoutManager as CardStackLayoutManager).setScaleInterval(0.95f)
        (cardStackView.layoutManager as CardStackLayoutManager).setSwipeThreshold(0.3f)
        (cardStackView.layoutManager as CardStackLayoutManager).setMaxDegree(20.0f)
        (cardStackView.layoutManager as CardStackLayoutManager).setDirections(Direction.HORIZONTAL)
        (cardStackView.layoutManager as CardStackLayoutManager).setCanScrollHorizontal(true)
        (cardStackView.layoutManager as CardStackLayoutManager).setCanScrollVertical(true)
        (cardStackView.layoutManager as CardStackLayoutManager).setSwipeableMethod(SwipeableMethod.AutomaticAndManual)

        cardStackView.adapter = CardStackAdapter(listOf(vm.getCurrentUserData().value,
            vm.getCurrentUserData().value,
            vm.getCurrentUserData().value,
            vm.getCurrentUserData().value,
            vm.getCurrentUserData().value,
            vm.getCurrentUserData().value,
            vm.getCurrentUserData().value) as List<UserData>)

        // Inflate the layout for this fragment
        return viewF
    }


}
