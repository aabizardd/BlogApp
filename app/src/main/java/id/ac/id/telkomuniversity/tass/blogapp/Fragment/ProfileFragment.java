package id.ac.id.telkomuniversity.tass.blogapp.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;
import id.ac.id.telkomuniversity.tass.blogapp.Activities.RegisterActivity;
import id.ac.id.telkomuniversity.tass.blogapp.R;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    CircleImageView editFoto;
    TextInputLayout tilNama, tilEmail,tilPassword;
    Button btnSubmit;
    ProgressBar progressBar;

    static int PReqCode = 509;
    static int REQUESTCODE = 509;
    private Uri pickedImgUri = null;

    String nama, email, password = null;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_profile, container, false);
        final View fragmentView = inflater.inflate(R.layout.fragment_profile, container, false);
        editFoto = fragmentView.findViewById(R.id.editFoto);
        tilNama = fragmentView.findViewById(R.id.tilNama);
        tilEmail = fragmentView.findViewById(R.id.tilEmail);
        tilPassword = fragmentView.findViewById(R.id.tilPassword);
        progressBar = fragmentView.findViewById(R.id.progressBar);
        btnSubmit = fragmentView.findViewById(R.id.btnSubmit);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            Log.d(TAG, "onCreateView: "+user.getDisplayName()+" "+user.getEmail());
            Glide.with(this).load(user.getPhotoUrl()).into(editFoto);
            tilNama.getEditText().setText(user.getDisplayName());
            tilNama.getEditText().setSelection(user.getDisplayName().length());
            tilEmail.getEditText().setText(user.getEmail());
            tilEmail.getEditText().setSelection(user.getEmail().length());
        }

        progressBar.setVisibility(View.INVISIBLE);

//        editFoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openGallery();
//            }
//        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nama = tilNama.getEditText().getText().toString();
                email = tilEmail.getEditText().getText().toString();
                password = tilPassword.getEditText().getText().toString();

                progressBar.setVisibility(View.VISIBLE);
                btnSubmit.setVisibility(View.INVISIBLE);

//                masih buggy gamau jalan kalo password yang kosong
                if(tilNama.getEditText().getText().toString().isEmpty()){
                    Toast.makeText(getActivity(),"isi dulu semuanya",Toast.LENGTH_SHORT).show();
                }else if(tilEmail.getEditText().getText().toString().isEmpty()){
                    Toast.makeText(getActivity(),"isi dulu semuanya",Toast.LENGTH_SHORT).show();
                }else if(tilPassword.getEditText().getText().toString().isEmpty()){
                    Toast.makeText(getActivity(),"isi dulu semuanya",Toast.LENGTH_SHORT).show();
                }

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                        .setDisplayName(nama)
                        .build();

                currentUser.updateProfile(request).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar.setVisibility(View.INVISIBLE);
                        btnSubmit.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(),"hore nama",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

                currentUser.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.INVISIBLE);
                        btnSubmit.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(),"hore em",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

                currentUser.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.INVISIBLE);
                        btnSubmit.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(),"hore pw",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

                AuthCredential credential = EmailAuthProvider.getCredential(email,password);
                currentUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getActivity(),"reauth",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        return fragmentView;
    }

//    public void storageDemo(View view){
//        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
//        StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
//
//
//    }
}