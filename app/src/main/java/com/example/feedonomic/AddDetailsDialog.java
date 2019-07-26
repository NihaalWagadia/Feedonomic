package com.example.feedonomic;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddDetailsDialog extends AppCompatDialogFragment {
    private EditText editText_title;
    private EditText editText_api;
//    private AddDetailsListener listener;
    DatabaseReference mDatabaseReference;
    FirebaseUser user;
    FirebaseAuth auth;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(user.getUid()).child("api");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        builder.setView(view)
                .setTitle("Add Feed Details")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String title = editText_title.getText().toString();
                        String rssLink = editText_api.getText().toString();
                        Log.d("abcd",title);
                        ApiAdapter apiAdapter = new ApiAdapter(title, rssLink);
                        Log.d("abcdef",title);
                        mDatabaseReference.child(title).setValue(apiAdapter)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                   if(task.isSuccessful()){
                                       Log.d("You are now", "You are now");

                                   }
                                    }
                                });
                        Log.d("qaz",title);
                    }
                });

        editText_title = view.findViewById(R.id.edit_title);
        editText_api = view.findViewById(R.id.edit_api);

        return builder.create();

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Must implement Listener");

        }
    }

    public interface  AddDetailsListener{
        void applyTexts(String title, String rssLink);
    }



}
