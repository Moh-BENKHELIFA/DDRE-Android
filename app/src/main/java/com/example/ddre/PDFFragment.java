package com.example.ddre;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.pspdfkit.configuration.PdfConfiguration;
import com.pspdfkit.configuration.page.PageLayoutMode;
import com.pspdfkit.configuration.page.PageScrollDirection;
import com.pspdfkit.configuration.page.PageScrollMode;
import com.pspdfkit.ui.PdfFragment;

public class PDFFragment extends Fragment {

    PdfFragment fragment;
    Uri documentUri;

    final PdfConfiguration configuration = new PdfConfiguration.Builder()
            .scrollDirection(PageScrollDirection.VERTICAL)
            .scrollMode(PageScrollMode.CONTINUOUS)
            .layoutMode(PageLayoutMode.SINGLE)
            .scrollOnEdgeTapEnabled(false)
            .restoreLastViewedPage(false)


            //.showThumbnailGrid()
            // .fitMode(PageFitMode.FIT_TO_WIDTH)
            .build();


    public PDFFragment(Uri uri) {
        documentUri = uri;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance) {
//        container.removeAllViews();
        View rootView = inflater.inflate(R.layout.fragment_pdf, container, false);

        fragment = (PdfFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);

        if (fragment == null) {
            fragment = PdfFragment.newInstance(documentUri, configuration);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, fragment)
                    .commit();
        }

        Log.i("PDFFRagment","On create View Method");

        return rootView;
    }
}

