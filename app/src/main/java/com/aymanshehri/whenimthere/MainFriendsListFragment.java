package com.aymanshehri.whenimthere;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainFriendsListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    public MainFriendsListFragment() {
        // Required empty public constructor
    }

    public static MainFriendsListFragment newInstance() {
        MainFriendsListFragment fragment = new MainFriendsListFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_friends_list, container, false);
        Fragment fragment = FriendsListFragment.newInstance();
        getChildFragmentManager().beginTransaction().replace(R.id.fl_friends_fragment_container, fragment).commit();
        return view;
    }
}
