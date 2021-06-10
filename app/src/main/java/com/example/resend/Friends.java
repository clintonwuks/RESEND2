package com.example.resend;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.resend.models.firestore.FireStoreUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Friends extends Fragment {
    private ListView lv_mainlist;
    private EditText editText;
    private ArrayList<FriendItem> al_items;
    private CustomArrayAdapter caa;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private final String TAG = "APP_TEST";

    private FireStoreUser user;

    public Friends() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        initElements();

        editText.setOnEditorActionListener((view1, actionid, event) -> {
// if the user is done entering in a new string then add it to
// the array list. this then notifies the adapter that the data has
// changed and that the list view needs to be updated
//            if(actionid == EditorInfo.IME_NULL
//                    && event.getAction() == KeyEvent.ACTION_DOWN) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionid == EditorInfo.IME_ACTION_DONE)) {
                al_items.add(new FriendItem(editText.getText().toString()));
                Log.d(TAG, "array list: " + al_items.toString());
                caa.notifyDataSetChanged();
                editText.getText().clear();
                //tv_display.setText("added item: " + et_new_strings.getText());
                //return true;
            }
// if we get to this point then the event has not been handled thus
// return false
            return false;
        });


    }

    private void initElements() {
        if (getActivity() != null) {
            lv_mainlist = getActivity().findViewById(R.id.lv_mainlist);
            editText = getActivity().findViewById(R.id.editText);
            al_items = new ArrayList<>();
// create an array adapter for al_strings and set it on the listview
            caa = new CustomArrayAdapter(requireContext(), al_items);
            lv_mainlist.setAdapter(caa);
            //fetchUser();
        }
    }

    private void fetchUser() {
        FirebaseUser fsUser = firebaseAuth.getCurrentUser();
        if (fsUser != null) {
            String uuid = fsUser.getUid();
            Query query = db.collection("Users").whereEqualTo("uuid", uuid);
            query.get().addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    QuerySnapshot res = task.getResult();

                    if (res != null && !res.isEmpty()) {
                        DocumentSnapshot userSnapshot = res.getDocuments().get(0);
                        String documentId = userSnapshot.getId();
                        FireStoreUser user = userSnapshot.toObject(FireStoreUser.class);
                        if(user != null) this.user = user;
                        //setUserDetails();
                    }
                } else {
                    Log.v(TAG, "Error getting documents: ", task.getException());
                }
            });
        }
    }
}