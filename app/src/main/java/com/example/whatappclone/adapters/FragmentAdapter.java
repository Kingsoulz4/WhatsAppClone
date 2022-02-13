package com.example.whatappclone.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.whatappclone.fragments.CallFragment;
import com.example.whatappclone.fragments.ChatFragment;
import com.example.whatappclone.fragments.ContactFragment;

public class FragmentAdapter extends FragmentPagerAdapter {
    public FragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new ChatFragment();
            case 1:
                return new CallFragment();
            case 2:
                return new ContactFragment();
            default:
                return new ChatFragment();
        }


    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "CHAT";
            case 1:
                return "CALL";
            case 2:
                return "CONTACT";
            default:
                return null;
        }
    }
}
