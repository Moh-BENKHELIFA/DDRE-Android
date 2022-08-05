package com.example.ddre;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class FloatMenu extends Fragment {

    private Button btnServerSettings;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance){
        View rootView = inflater.inflate(R.layout.fragment_float_menu, container, true);

        btnServerSettings = (Button) rootView.findViewById(R.id.btnServerSettings);

        btnServerSettings.setOnClickListener(btnServerSettingsListener);



        return rootView;
    }


    private View.OnClickListener btnServerSettingsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            btnServerSettings.setText("MACHIN CHOSE");

            Intent intent = new Intent(getActivity(), SettingsActivity.class);
//                intent.putExtra()
            Toast.makeText(getActivity(), "Server Settings", Toast.LENGTH_SHORT).show();
            Log.i("DEBUG", "Hi!");
            startActivityForResult(intent, MainActivity.SETTINGS_ACTIVITY);
        }
    };

}