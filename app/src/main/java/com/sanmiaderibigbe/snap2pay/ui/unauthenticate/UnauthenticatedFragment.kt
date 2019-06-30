package com.sanmiaderibigbe.snap2pay.ui.unauthenticate

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sanmiaderibigbe.snap2pay.R




/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [UnauthenticatedFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 *
 */
class UnauthenticatedFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_unauthenticated, container, false)
    }


}
