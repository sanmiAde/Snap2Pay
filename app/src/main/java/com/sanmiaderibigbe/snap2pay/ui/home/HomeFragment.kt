package com.sanmiaderibigbe.snap2pay.ui.home


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.sanmiaderibigbe.snap2pay.R
import com.sanmiaderibigbe.snap2pay.ui.login.LoginViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragment : Fragment() {

    private  val viewModel by viewModel<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()

        viewModel.getLoginResource().observe(viewLifecycleOwner, Observer {
            when(it.data){
                LoginViewModel.AuthenticationState.AUTHENTICATED -> Toast.makeText(activity, "Welcome", Toast.LENGTH_SHORT).show()
                LoginViewModel.AuthenticationState.UNAUTHENTICATED -> navController.navigate(HomeFragmentDirections.actionHomeFragmentToUnauthenticatedFragment())

            }
        })

        txt_test.setOnClickListener {
            val firebase = FirebaseAuth.getInstance()
            firebase.signOut()
        }

    }
}
