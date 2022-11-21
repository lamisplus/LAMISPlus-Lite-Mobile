package org.lamisplus.datafi.activities.formdisplay;

import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class FormPageAdapter extends FragmentStatePagerAdapter {

    List<Integer> totalAccepted = new ArrayList<>();

    private String title[] = {"HIV Enrollment Form", "Client Intake Form", "Pre Test Counselling"};
    private SparseArray<Fragment> registeredFragments = new SparseArray<>();

    public FormPageAdapter(FragmentManager fm, String refValue) {
        super(fm);

    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return FormDisplayFragment.getInstance(position, "Page #" + position);
            case 1:
                return FormDisplayClientIntakeFragment.getInstance(position, "Page #" + position);
            default:
                return FormDisplayPostTest.getInstance(position, "Page #" + position);
        }
//        Log.v("Baron", "Logging getItem:"+position);
//        return FormDisplayFragment.getInstance(position, "Page #" + position);
    }


    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public int getItemPosition(Object object) {
        Log.v("Baron", "Item Position " + POSITION_NONE);
        return POSITION_NONE;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
