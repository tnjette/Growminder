package com.tylerjette.growmindv05;

import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class descriptionExpansionFragment extends Fragment {

    private OnFragmentInteractionListener listener; //for communication with the activity

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.test_fragment, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        String descriptionText = getArguments().getString("text");
        TextView t1 = view.findViewById(R.id.t1);
        t1.setText(descriptionText);
        int backgroundImage = getArguments().getInt("background_image");
        ImageView iv = view.findViewById(R.id.fragmentBackgroundImage);
        iv.setClipToOutline(true);
        Drawable d = getResources().getDrawable(backgroundImage);
        iv.setImageDrawable(d);
        Button backButton = view.findViewById(R.id.fragment_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFragment(v);
            }
        });
    }

    private void closeFragment(View view) {
        listener.onFragmentInteraction(0);
        getActivity().getFragmentManager().beginTransaction().remove(this).commit();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int state);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        }else{
            throw new RuntimeException(context.toString());
        }
    }
}
