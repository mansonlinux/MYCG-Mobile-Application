package com.example.hisyam.mycgmobileapps;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;


public class dota2_bracket_fragment  extends Fragment {

    View view;


    public dota2_bracket_fragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        view = inflater.inflate(R.layout.dota2_bracket_fragment,container,false);

        Button livebracketbtn = view.findViewById(R.id.livebracketbtn);

        livebracketbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),dota2_live_bracket.class));
            }
        });

        return view;

    }
}
