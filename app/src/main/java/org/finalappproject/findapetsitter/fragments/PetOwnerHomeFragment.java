package org.finalappproject.findapetsitter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

import org.finalappproject.findapetsitter.R;
import org.finalappproject.findapetsitter.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/*
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PetOwnerHomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PetOwnerHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PetOwnerHomeFragment extends Fragment {

    /**
     * PetOwnerHomeFragmentListener must be implemented by activities that contain this fragment
     *
     * <p>
     * See the Android Training lesson <a href="http://developer.android.com/training/basics/fragments/communicating.html">Communicating with Other Fragments</a> for more information.
     * </p>
     */
    public interface PetOwnerHomeFragmentListener {
        void onPetSitterClicked(User petSitter);
    }

    public class PetOwnerPageAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 2;

        private String titles[] = {getString(R.string.page_pet_sitters), getString(R.string.page_nearby_sitters)};

        public PetOwnerPageAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1:
                    return NearbySittersFragment.newInstance();
                case 0:
                default:
                    return new PetSittersFragment();
            }
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }
    }

    //Views
    @BindView(R.id.viewpager)
    ViewPager vpPager;

    @BindView(R.id.tabs)
    PagerSlidingTabStrip psTabStrip;

    // Members
    private Unbinder mUnbinder;
    private PetOwnerPageAdapter mPetOwnerPageAdapter;
    // private OnFragmentInteractionListener mListener;

    /**
     * Required empty public constructor,
     * use newInstance() factory method instead
     */
    @Deprecated
    public PetOwnerHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Factory method used to create PetOwnerHomeFragment
     * with parameters, use this method instead of
     * new PetOwnerHomeFragment()
     *
     * @return A new instance of fragment PetOwnerHomeFragment.
     */
    public static PetOwnerHomeFragment newInstance() {
        PetOwnerHomeFragment fragment = new PetOwnerHomeFragment();
        // Currently this fragment doesn't require parameters, in the future we may want to
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        setRetainInstance(true);

        // Currently there are no arguments, in the

        if (getArguments() != null) {
            //
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pet_owner_home, container, false);
        // Bind view
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViewPager(view);
    }

    /*
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    */

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Unbind views
        mUnbinder.unbind();
    }




    private void setupViewPager(View parentView) {
        // Instantiate the adapter
        mPetOwnerPageAdapter = new PetOwnerPageAdapter(getFragmentManager());
        // Setup view pager with TweetsPageAdapter
        vpPager = (ViewPager) parentView.findViewById(R.id.viewpager);
        vpPager.setAdapter(mPetOwnerPageAdapter);
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    //NearbySittersFragment f = (NearbySittersFragment) mPetOwnerPageAdapter.getItem(position);
                    //f.setupMapFragment();
                }
                //setupMapFragment()
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // Setup PagerSlidingTabString with ViewPager
        psTabStrip = (PagerSlidingTabStrip) parentView.findViewById(R.id.tabs);
        psTabStrip.setViewPager(vpPager);
    }
}