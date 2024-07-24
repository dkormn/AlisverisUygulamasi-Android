package com.example.aksam;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SearchBarFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_bar, container, false);

        EditText searchBar = view.findViewById(R.id.search_bar);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // İhtiyaç yok
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MainActivity activity = (MainActivity) getActivity();
                if (activity != null) {
                    activity.updateProductList(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // İhtiyaç yok
            }
        });

        return view;
    }
}
