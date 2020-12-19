package id.ac.id.telkomuniversity.tass.blogapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Set;

import id.ac.id.telkomuniversity.tass.blogapp.Models.Post;
import id.ac.id.telkomuniversity.tass.blogapp.R;

public class BottomSheet extends BottomSheetDialogFragment {

    private EditText update_title, update_desc;
    private Button edit_btn;

    public BottomSheet(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_update_dialog,container,false);
//        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);


//        bundle.putString("pict", pict);
//        bundle.putString("time", time);
//        bundle.putString("uid", uid);
//        bundle.putString("userPhoto", userPhoto);
//        bundle.putString("uname", uname);


        final String title = getArguments().getString("title");
        final String desc = getArguments().getString("desc");
        final String postKey = getArguments().getString("postKey");
        final String pict = getArguments().getString("pict");
        final String time = getArguments().getString("time");
        final String uid = getArguments().getString("uid");
        final String userPhoto = getArguments().getString("userPhoto");
        final String uname = getArguments().getString("uname");

        update_title = view.findViewById(R.id.et_update_title);
        update_title.setText(title);

        update_desc = view.findViewById(R.id.et_update_desc);
        update_desc.setText(desc);

        edit_btn = view.findViewById(R.id.btn_update);

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Post post = new Post();

                post.setPostKey(postKey);
                post.setTitle(update_title.getText().toString());
                post.setDescription(update_desc.getText().toString());
                post.setPicture(pict);
                post.setTimeStamp(time);
                post.setUserId(uid);
                post.setUserPhoto(userPhoto);
                post.setUsername(uname);

                updatePost(post);

            }
        });






        return view;


    }

    private void updatePost(Post post){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        myRef.child("postinganCerpen").child(post.getPostKey()).setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(getContext(), "Data Updated",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getContext(), Home.class);
                startActivity(i);
            }
        });



//        DatabaseReference ref = FirebaseStorage.getInstance().getReference().child("postignanCerpen").
//                child(post.getPostKey()).


    }
}
