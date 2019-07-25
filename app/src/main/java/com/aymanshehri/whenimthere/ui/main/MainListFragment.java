package com.aymanshehri.whenimthere.ui.main;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aymanshehri.whenimthere.ListFragment;
import com.aymanshehri.whenimthere.R;
import com.aymanshehri.whenimthere.services.MyFirebaseGetter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private boolean isGotList;
    private String userEmail;

    private OnFragmentInteractionListener mListener;


    @BindView(R.id.navbar)
    BottomNavigationView navbar;
    @BindView(R.id.tv_friends_email)
    TextView friendsEmail;
    private Fragment fragment;


    public MainListFragment() {
        // Required empty public constructor
    }

    public static MainListFragment newInstance(boolean param1, String param2) {
        MainListFragment fragment = new MainListFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isGotList = getArguments().getBoolean(ARG_PARAM1);
            userEmail = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_list, container, false);
        ButterKnife.bind(this, view);
        if(userEmail != MyFirebaseGetter.getUserEmail()){
            friendsEmail.setText(userEmail);
            friendsEmail.setVisibility(View.VISIBLE);
        }else{
            friendsEmail.setText("");
            friendsEmail.setVisibility(View.GONE);
        }

        navbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if(menuItem.getItemId() == R.id.nav_got)
                    fragment = ListFragment.newInstance(true,userEmail);
                else
                    fragment = ListFragment.newInstance(false,userEmail);

                getChildFragmentManager().beginTransaction().replace(R.id.fl_list_fragment_container, fragment).commit();
                return true;
            }
        });

        fragment = ListFragment.newInstance(isGotList, userEmail);
        getChildFragmentManager().beginTransaction().replace(R.id.fl_list_fragment_container, fragment).commit();


        return view;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
