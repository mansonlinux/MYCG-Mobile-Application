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

public class events3_register_fragment extends Fragment {
    View view;


    public events3_register_fragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.events3_register_fragment,container,false);

        Button registerteambtn = view.findViewById(R.id.Registerteambtn);
        Uri uri;

        registerteambtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://wa.me/60175512601?text=Welcome+to+Ticket+Service+%0D%0A-Name%0D%0A-IC%0D%0A-Address%0D%0A-Events+Game");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        return view;
    }
}