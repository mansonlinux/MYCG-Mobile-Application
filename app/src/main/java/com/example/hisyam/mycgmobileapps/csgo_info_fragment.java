package com.example.hisyam.mycgmobileapps;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class csgo_info_fragment  extends Fragment {
    View view;


    public csgo_info_fragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.csgo_info_fragment,container,false);

        Button registerteambtn = view.findViewById(R.id.Registerteambtn);
        Uri uri;

        registerteambtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://wa.me/60175512601?text=Register+CsGo+Amateur+Tournament+%0D%0A-Team+Name%0D%0A-List+Player%285max%29");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        return view;
    }
}
