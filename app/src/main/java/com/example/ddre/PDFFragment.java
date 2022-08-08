package com.example.ddre;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.pspdfkit.configuration.PdfConfiguration;
import com.pspdfkit.configuration.page.PageLayoutMode;
import com.pspdfkit.configuration.page.PageScrollDirection;
import com.pspdfkit.configuration.page.PageScrollMode;
import com.pspdfkit.datastructures.TextSelection;
import com.pspdfkit.ui.PdfFragment;
import com.pspdfkit.ui.special_mode.controller.TextSelectionController;
import com.pspdfkit.ui.special_mode.manager.TextSelectionManager;
import com.pspdfkit.ui.toolbar.ContextualToolbar;
import com.pspdfkit.ui.toolbar.ContextualToolbarMenuItem;
import com.pspdfkit.ui.toolbar.ToolbarCoordinatorLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class PDFFragment extends Fragment implements TextSelectionManager.OnTextSelectionChangeListener, TextSelectionManager.OnTextSelectionModeChangeListener{
//public class PDFFragment extends Fragment  implements ToolbarCoordinatorLayout.OnContextualToolbarLifecycleListener {

    PdfFragment fragment;
    Uri documentUri;

    final PdfConfiguration configuration = new PdfConfiguration.Builder()
            .scrollDirection(PageScrollDirection.VERTICAL)
            .scrollMode(PageScrollMode.CONTINUOUS)
            .layoutMode(PageLayoutMode.SINGLE)
            .scrollOnEdgeTapEnabled(false)
            .restoreLastViewedPage(false)
            .textSelectionPopupToolbarEnabled(false)


            //.showThumbnailGrid()
            // .fitMode(PageFitMode.FIT_TO_WIDTH)
            .build();


    public PDFFragment() {
//        documentUri = uri;
    }

    private ContextualToolbarMenuItem customTextSelectionAction;

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


        fragment.addOnTextSelectionChangeListener(this);
        fragment.addOnTextSelectionModeChangeListener(this);

        return rootView;
    }


    private String textSelected = "";

    @Override
    public boolean onBeforeTextSelectionChange(@Nullable TextSelection textSelection, @Nullable TextSelection textSelection1) {
        Log.i("TEXT_SELECTION", "onBeforeTextSelectionChange");



        return true;
    }

    @Override
    public void onAfterTextSelectionChange(@Nullable TextSelection textSelection, @Nullable TextSelection textSelection1) {

        if (textSelection != null) {
//            Toast toast = Toast.makeText(getApplicationContext(),
//                    String.format("Selected text was : %s", textSelection.text),
//                    Toast.LENGTH_LONG);
//
//            toast.show();
            textSelected = textSelection.text;

        } else {
            Toast.makeText(getContext(),
                    "Text selection is cleared",
                    Toast.LENGTH_SHORT).show();


        }

        Log.i("TEXT_SELECTION", "onAfterTextSelectionChange");



    }


    @Override
    public void onEnterTextSelectionMode(@NonNull TextSelectionController textSelectionController) {
        System.out.println("ON ENTER");
        Log.i("TEXT_SELECTION", "onEnterTextSelectionMode");
    }

    @Override
    public void onExitTextSelectionMode(@NonNull TextSelectionController textSelectionController) {
        System.out.println("ON EXIT");
        Log.i("TEXT_SELECTION", "onExitTextSelectionMode");

        try {
            JSONObject obj = new JSONObject();
            obj.put("messageCode", ServerCode.NEW_TEXT_DECAL.value());
            obj.put("title", "Selected text");
            obj.put("text", textSelected.replace("\n", "").replace("\r", ""));

            MainActivity mActivity = (MainActivity) requireActivity();

            mActivity.client.write(obj.toString().getBytes("utf-8"));
        }catch (JSONException | UnsupportedEncodingException e){

        }
    }

    public PdfFragment getPdfFragment(){
        return fragment;
    }

    public void setDocumentUri(Uri uri){
        this.documentUri = uri;


//        fragment = (PdfFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
//
//        if (fragment == null) {
//            fragment = PdfFragment.newInstance(documentUri, configuration);
//            getActivity().getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.fragmentContainerView, fragment)
//                    .commit();
//        }
    }

//    @Override
//    public void onPrepareContextualToolbar(@NonNull ContextualToolbar contextualToolbar) {
//
//    }
//
//    @Override
//    public void onDisplayContextualToolbar(@NonNull ContextualToolbar contextualToolbar) {
//
//    }
//
//    @Override
//    public void onRemoveContextualToolbar(@NonNull ContextualToolbar contextualToolbar) {
//
//    }

    public boolean onTextSelectionChange(@Nullable TextSelection newTextSelection, @Nullable TextSelection currentTextSelection) {
        if (newTextSelection != null) {
            Log.i("TEXT_SELECTION", String.format("Selected text was: %s", newTextSelection.text));
        } else {
            Log.i("TEXT_SELECTION", "Text selection is cleared.");
        }

        // You can also return `false` to prevent changes to the current selection state.
        return true;
    }
}

