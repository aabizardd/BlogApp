package id.ac.id.telkomuniversity.tass.blogapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.text.LineBreaker;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.amar.library.ui.StickyScrollView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

//import id.ac.id.telkomuniversity.tass.blogapp.Models.LoadDialog;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import id.ac.id.telkomuniversity.tass.blogapp.Adapters.CommentAdapter;
import id.ac.id.telkomuniversity.tass.blogapp.Models.CommentPost;
import id.ac.id.telkomuniversity.tass.blogapp.Models.Post;
import id.ac.id.telkomuniversity.tass.blogapp.R;

public class PostDetailActivity extends AppCompatActivity {

    ImageView imgPost,imgUserPost, imgCurrentUser;
    TextView txtPostDesc, txtPostDateName, txtPostTitle;
    EditText editTextComment;
    Button btnAddComment, edit_post_btn;
    ImageButton deletePost, play_btn,stop_btn;
    VideoView vidPost;
    private MediaController ctlr;
    String postKey;

    TextView show_more, minim;

    
    StickyScrollView nstd_view;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseDatabase firebaseDatabase;
    RecyclerView rvComment;
    CommentAdapter commentAdapter;
    List<CommentPost> listComment;

    int jumlahNotif=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);


        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        getSupportActionBar().hide();

//        getActionBar().hide();
//        getSupportActionBar().hide();

        backToHome();

        deletePost = findViewById(R.id.delete_post);
        edit_post_btn = findViewById(R.id.post_edit_btn);





        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        postKey = getIntent().getExtras().getString("postKey");

        String currUser = currentUser.getUid();
        String userIdPost = getIntent().getExtras().getString("userId");

        if(!currUser.equals(userIdPost) ){
            deletePost.setVisibility(View.INVISIBLE);
        }else{
            deletePost.setVisibility(View.VISIBLE);
            edit_post_btn.setVisibility(View.VISIBLE);
        }

        deletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String postKey = getIntent().getExtras().getString("postKey");
                String postImg = getIntent().getExtras().getString("postImage");

                Post post = new Post();
                post.setPostKey(postKey);
                post.setPicture(postImg);

                deleteAndAlert(post);
//                deleteAllCommentByIdPost(post);

            }
        });


        nstd_view = findViewById(R.id.screen_detail);

        //ini

        show_more = findViewById(R.id.showMore);
        minim = findViewById(R.id.minimize);


        rvComment = findViewById(R.id.rv_comment);
        initRVComment();

        imgPost = findViewById(R.id.post_detail_img);
        imgUserPost = findViewById(R.id.post_detail_user_img);
        imgCurrentUser = findViewById(R.id.post_detail_currentuser_img);

        txtPostTitle = findViewById(R.id.post_detail_title);
        txtPostDesc = findViewById(R.id.post_detail_desc);
        txtPostDateName = findViewById(R.id.post_detail_date_name);

        editTextComment = findViewById(R.id.post_detail_comment);
        btnAddComment = findViewById(R.id.post_detail_add_comment_btn);

        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String isiComment = editTextComment.getText().toString();
                String idUser = currentUser.getUid();
                String namaUser = currentUser.getDisplayName();
                String fotoUser = currentUser.getPhotoUrl().toString();
                String postKey = getIntent().getExtras().getString("postKey");
                String userIdHasPost = getIntent().getExtras().getString("userId");

                CommentPost commentPost = new CommentPost(postKey, isiComment,idUser,fotoUser,namaUser, userIdHasPost);

                addComment(commentPost);
            }
        });

        vidPost = findViewById(R.id.videoView);
        play_btn = findViewById(R.id.play_btnn);
        stop_btn = findViewById(R.id.stopBtn);


        String username = getIntent().getExtras().getString("username");
        txtPostDateName.setText("| by "+username);
        final String postImage = getIntent().getExtras().getString("postImage");

        if(postImage.contains("video")){
            vidPost.setVisibility(View.VISIBLE);
            play_btn.setVisibility(View.VISIBLE);
            play_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    play_btn.setVisibility(View.INVISIBLE);
                    Uri uri = Uri.parse(postImage);
                    vidPost.setVideoURI(uri);
                    vidPost.start();
                    ctlr = new MediaController(PostDetailActivity.this);
                    ctlr.hide();
                    vidPost.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.setLooping(true);
                        }
                    });

//                    ctlr.setMediaPlayer(vidPost);
//
//                    vidPost.setMediaController(ctlr);
//                    vidPost.requestFocus();

                    stop_btn.setVisibility(View.VISIBLE);
                    int duration =vidPost.getDuration();
                    int currentPosition = vidPost.getCurrentPosition();
                    int hasil = duration + currentPosition;
                    if(hasil/2 == duration) {
                        stop_btn.setVisibility(View.INVISIBLE);
                    }
                    stop_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri uri = Uri.parse(postImage);
                            vidPost.setVideoURI(uri);
                            vidPost.stopPlayback();

                            stop_btn.setVisibility(View.INVISIBLE);
                            play_btn.setVisibility(View.VISIBLE);

//                            int currentPosition = vidPost.getCurrentPosition();
//                            int duration =vidPost.getDuration();
//
//                            if((currentPosition + duration) / 2 == duration){
//                                play_btn.setVisibility(View.VISIBLE);
//                            }
                        }
                    });

                }
            });
        }else{
            vidPost.setVisibility(View.INVISIBLE);
            play_btn.setVisibility(View.INVISIBLE);
            Glide.with(this).load(postImage).into(imgPost);
        }



        String postTitle = getIntent().getExtras().getString("title");
        txtPostTitle.setText(postTitle);

        String userPostImage = getIntent().getExtras().getString("userPhoto");
        Glide.with(this).load(userPostImage).into(imgUserPost);

        Glide.with(this).load(currentUser.getPhotoUrl()).into(imgCurrentUser);

        final String postDesc = getIntent().getExtras().getString("description");

        String desc_cut = "";

        if(postDesc.length() > 150) {
            show_more.setVisibility(View.VISIBLE);
            desc_cut = postDesc.substring(0, 150) + " .....";

            show_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String new_desc_cut = postDesc;

                    txtPostDesc.setText(new_desc_cut);
                    txtPostDesc.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);

                    show_more.setVisibility(View.INVISIBLE);
                    minim.setVisibility(View.VISIBLE);

                    minim.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String new_desc_cut = postDesc.substring(0, 150) + " .....";;

                            txtPostDesc.setText(new_desc_cut);
                            txtPostDesc.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);

                            show_more.setVisibility(View.VISIBLE);
                            minim.setVisibility(View.INVISIBLE);

                        }
                    });


                }
            });


        }else{
//            show_more.setVisibility(View.INVISIBLE);
            desc_cut = postDesc;
        }




        txtPostDesc.setText(desc_cut);
        txtPostDesc.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);

        String postKey = getIntent().getExtras().getString("postKey");

        String timeStamp = getIntent().getExtras().getString("postDate");




//        update_title.set(postTitle);
//        update_desc.setText(postDesc);







        //userPhoto
        //username

        click_edit_btn(postTitle, postDesc,postKey, postImage, timeStamp, currentUser.getUid(), userPostImage, username);


    }

    private void initRVComment() {
        rvComment.setLayoutManager(new LinearLayoutManager(this));
        DatabaseReference commentRef = firebaseDatabase.getReference("Comments").child(postKey);
        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listComment = new ArrayList<>();
                for(DataSnapshot snap:snapshot.getChildren()){
                    CommentPost commentPost = snap.getValue(CommentPost.class);
                    listComment.add(0,commentPost);
                }
                commentAdapter = new CommentAdapter(getApplicationContext(),listComment);
                rvComment.setAdapter(commentAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void showMessage(String s) {
        Toast.makeText((this), s, Toast.LENGTH_SHORT).show();
    }


    private void click_edit_btn(final String title, final String desc, final String postKey, final String pict, final String time, final String uid, final String userPhoto, final String uname){
        edit_post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheet bottomSheet = new BottomSheet();
                Bundle bundle = new Bundle();
//                bundle.putString(title, "key");
                bundle.putString("title", title);
                bundle.putString("desc", desc);
                bundle.putString("postKey", postKey);
                bundle.putString("pict", pict);
                bundle.putString("time", time);
                bundle.putString("uid", uid);
                bundle.putString("userPhoto", userPhoto);
                bundle.putString("uname", uname);
                bottomSheet.setArguments(bundle);
                bottomSheet.show(getSupportFragmentManager(),"TAG");
            }

        });
    }



    private void backToHome() {
        findViewById(R.id.back_to_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(PostDetailActivity.this, Home.class);
                startActivity(home);
                finish();
            }
        });
    }

    public void addComment(CommentPost commentPost){
        DatabaseReference commentReference = firebaseDatabase.getReference("Comments").child(postKey).push();
        String key = commentReference.getKey();
        commentPost.setCommentKey(key);
        final String isiComment = commentPost.getIsiComment();
        commentReference.setValue(commentPost).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                notifComment("Yay comment anda berhasil! 😀👌", "Berikut isi Comment anda: "+ isiComment);
                editTextComment.setText("");
                btnAddComment.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                notifComment("Yah comment anda gagal masuk ☹", e.getMessage());
            }
        });
    }



    public void deleteAndAlert(final Post post){
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);

        builder.setTitle("Want to delete it?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
//                StorageReference desertRef = storageRef.child("gambarCerpen/"+postImg);
//
//                desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        // File deleted successfully
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        // Uh-oh, an error occurred!
//                    }
//                });

//                nstd_view.setBackgroundColor(getColor(R.color.colorPrimary));
                final LoadDialog dial = new LoadDialog(PostDetailActivity.this);
                dial.startLoadingDialog();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("postinganCerpen").child(post.getPostKey());
                ref.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        deleteAllCommentByIdPost(post);
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference photoRef = storage.getReferenceFromUrl(post.getPicture());
                        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override


                            public void onSuccess(Void aVoid) {
                                // File deleted successfully
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dial.dismissDialog();
                                    }
                                },5000);
                                Toast.makeText(PostDetailActivity.this, "Delete Success",Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(), Home.class);
                                startActivity(i);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Uh-oh, an error occurred!

                            }
                        });
                    }
                });

            }
        });
        builder.setNegativeButton("No", null);
        builder.setIcon(R.drawable.ic_baseline_delete_24);
        builder.show();
    }

    private void deleteAllCommentByIdPost(Post post){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Comments").child(post.getPostKey());
        ref.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }

    private void notifComment(String action, String message) {
        int NOTIFICATION_ID = 234;
        String CHANNEL_ID = "my_channel_01";
        Intent intent = new Intent(this, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.aalogo);
        builder.setContentTitle(action);
        builder.setContentText(message);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

}