package com.example.ddre;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class HomeFragment extends Fragment {
    private Button btnConnectServer;
    protected static Button btnImageDecal;
    protected static Button btnTextDecal;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance){
        View rootView = inflater.inflate(R.layout.fragment_home, container, true);

        btnConnectServer = (Button) rootView.findViewById(R.id.btnConnectServer);

//        btnConnectServer.setOnClickListener(btnConnectServerListener);

        btnImageDecal = (Button) rootView.findViewById(R.id.imageDecal);
        btnImageDecal.setOnClickListener(btnImageDecalListener);

        btnTextDecal = (Button) rootView.findViewById(R.id.textDecal);
        btnTextDecal.setOnClickListener(btnTextDecalListener);

        MainActivity.connectionInfo = (TextView) rootView.findViewById(R.id.textConnectionState);
        MainActivity.chat = (TextView) rootView.findViewById(R.id.textChat);

//        MainActivity.btnSendMessage = (Button) rootView.findViewById(R.id.textDecal);


        return rootView;
    }


    private View.OnClickListener btnImageDecalListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                JSONObject obj = new JSONObject();
                obj.put("messageCode", ServerCode.NEW_IMAGE_DECAL.value());
                obj.put("image", "Lego");
                obj.put("width", 200);
                obj.put("height", 250);

                MainActivity mActivity = (MainActivity) requireActivity();

                mActivity.client.write(obj.toString().getBytes("utf-8"));
            }catch (JSONException | UnsupportedEncodingException e){

            }
        }
    };

    private View.OnClickListener btnTextDecalListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                JSONObject obj = new JSONObject();
                obj.put("messageCode", ServerCode.NEW_TEXT_DECAL.value());
                obj.put("title", "Abstract");
//                obj.put("text", " \n" +
//                        "\n" +
//                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum consectetur hendrerit mi, nec elementum dolor viverra sed. Donec hendrerit nisl iaculis dolor luctus posuere. Donec pharetra est vel eros rhoncus tempus nec eu erat. Nunc pellentesque sagittis augue vitae venenatis. Donec iaculis magna viverra, venenatis libero vel, varius urna. Sed vitae efficitur diam. Ut pellentesque, est at cursus varius, metus felis mollis odio, eget ullamcorper nisi justo ut nunc. Sed tincidunt viverra libero, vel pharetra mi posuere at.\n" +
//                        "\n" +
//                        "Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Donec quis neque ultricies, convallis urna et, iaculis nulla. Quisque at condimentum mi, sit amet convallis justo. Cras non sem non leo sagittis tempus quis ut est. Nullam vel risus a odio maximus pulvinar at id risus. Pellentesque fringilla orci nec orci malesuada fermentum. Fusce bibendum molestie lorem sed euismod. Nullam sit amet erat in ex tincidunt egestas eu semper sapien. Donec leo lorem, bibendum nec mi eget, luctus facilisis nisl. Proin enim metus, tincidunt vitae mi posuere, maximus mattis risus. Nullam consequat sollicitudin nibh. Maecenas augue elit, semper quis malesuada et, pharetra at ipsum. Nam sit amet tincidunt justo.\n" +
//                        "\n" +
//                        "Vestibulum orci velit, gravida nec tempus eu, dignissim at nisl. Aenean accumsan, arcu sit amet maximus accumsan, leo ipsum laoreet felis, sit amet laoreet lectus eros non mi. Proin et neque a ipsum dictum dignissim. Pellentesque et suscipit lectus. Etiam sapien tellus, tincidunt at sollicitudin molestie, sagittis a eros. Vestibulum id libero mollis, gravida quam eget, maximus nibh. Nunc varius mattis magna consectetur congue.\n" +
//                        "\n" +
//                        "Cras hendrerit felis pulvinar porta fringilla. Morbi sollicitudin, dui blandit pellentesque pharetra, urna velit hendrerit massa, id aliquam turpis urna et lacus. Pellentesque consequat eros ut massa semper, vel ultrices leo auctor. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis ut mauris at felis pulvinar bibendum id nec ante. Nulla suscipit tellus nisl. Duis quam ex, imperdiet a facilisis sed, blandit in tortor. Quisque erat justo, aliquam id augue vel, consectetur gravida eros. Integer rutrum consequat laoreet. Donec hendrerit nisl at enim volutpat, a mattis lacus porta.\n" +
//                        "\n" +
//                        "Maecenas tincidunt suscipit nibh, non mattis metus volutpat ut. Mauris vitae metus tincidunt, dignissim leo sit amet, feugiat dui. Nunc ornare dui id posuere viverra. Vivamus a ultricies purus, a fermentum ligula. Quisque ligula diam, pellentesque non gravida eu, ullamcorper eu purus. Duis iaculis ligula in congue rutrum. Integer id venenatis justo. Ut hendrerit sapien id lectus viverra sagittis. Nullam porttitor, mauris eget ultrices malesuada, elit magna vehicula odio, a congue nibh diam a nisl. Curabitur mauris nulla, facilisis at ullamcorper in, lobortis sit amet felis. Curabitur felis neque, sagittis et dapibus mollis, accumsan molestie tellus. Aliquam viverra, elit nec gravida porttitor, erat augue auctor massa, nec volutpat augue nisl in nibh. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Duis ac odio quis lacus posuere varius vitae et sapien. Nunc sit amet dui arcu. ");

                obj.put("text", "We propose the use of Hand-to-Face input, a method to interact with head-worn displays (HWDs) that involves contact with the face. We explore Hand-to-Face interaction to find suitable techniques for common mobile tasks.  We evaluate this form of interaction with document navigation tasks and examine its social acceptability. In a first study, users identify the cheek and forehead as predominant areas for interaction and agree on gestures for tasks involving continuous input, such as document navigation. These results guide the design of several Hand-to-Face navigation techniques and reveal that gestures performed on the cheek are more efficient and less tiring than interactions directly on the HWD. Initial results on the social acceptability of Hand-to-Face input allow us to further refine our design choices, and reveal unforeseen results: some gestures are considered culturally inappropriate and gender plays a role in selection of specific Hand-to-Face interactions. From our overall results, we provide a set of guidelines for developing effective Hand-to-Face interaction techniques. ");
                MainActivity mActivity = (MainActivity) requireActivity();

                mActivity.client.write(obj.toString().getBytes("utf-8"));
            }catch (JSONException | UnsupportedEncodingException e){

            }
        }
    };
}