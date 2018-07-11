package com.example.arafatm.instagram.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.arafatm.instagram.Model.Post;
import com.example.arafatm.instagram.Utils.PostAdapter;
import com.example.arafatm.instagram.R;
import com.example.arafatm.instagram.Utils.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class TimelineActivity extends AppCompatActivity {
    public EditText descriptionInput;
    public Button createButton;
    public Button refreshButton;
    public static String imagePath = "path to the picture!";
    private static final String TAG = "TimelineActivity";
    private static final int ACTIVITY_NUM = 0;
    private Context context = TimelineActivity.this;
    PostAdapter postAdapter;
    ArrayList<Post> posts;
    RecyclerView rvPost;
    private final int CODE = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        setupBttomNavigationView();
        setupViewPager();



        //find the Recycle view
        rvPost = (RecyclerView) findViewById(R.id.rvPost);
        //init the arraylist (data source)
        posts = new ArrayList<>();
        //construct the adapter from this data source
        postAdapter = new PostAdapter(posts);
        //RecycleView Setup (Layout manager, use adapter
        rvPost.setLayoutManager(new LinearLayoutManager(this));


        //set the adapter
        rvPost.setAdapter(postAdapter);
        loadTopPosts();



//////        descriptionInput = (EditText) findViewById(R.id.);
//////        createButton = (Button) findViewById(R.id.);
//////        refreshButton = (Button) findViewById(R.id.);
////
////        createButton.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                final String description = descriptionInput.getText().toString();
////                final ParseUser user = ParseUser.getCurrentUser();
////
////                final File file = new File(imagePath);
////                final ParseFile parseFile = new ParseFile(file);
////
////                createPost(description, parseFile, user);
////            }
//        });
//        refreshButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                loadTopPosts();
//            }
//        });
//        loadTopPosts();
    }



    /*Adds the 3 tabs */
    private void setupViewPager() {
        SessionsPageAdaptor adapter = new SessionsPageAdaptor(getSupportFragmentManager());
        adapter.addFragment(new fragment_message());
        adapter.addFragment(new fragment_camera());
        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(1).setIcon(R.drawable.ic_camera);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_arraow);
    }

//        refreshButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                loadTopPosts();
//            }
//        });
//        loadTopPosts()
//    }


    private void loadTopPosts() {
        final Post.Query postQuery = new Post.Query();
        postQuery.getTop().withUser().getPostsForUser(ParseUser.getCurrentUser());
        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        Log.d("TimelineActivity", "Post[" + i + "]" + objects.get(i).getDescription() + "\nusername = " + objects.get(i).getUser().getUsername());
                        Post post = objects.get(i);
                        posts.add(0, post);
                        //notify adapter
                        postAdapter.notifyItemChanged(posts.size() - 1);
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * ButtomView SETUP
     **/
    private void setupBttomNavigationView() {
        Log.d(TAG, "Setting up Nav View");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupButtomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(context, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }


    @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == CODE) {
                if (resultCode == RESULT_OK) {
                    // by this point we have the camera photo on disk
                    Post post = (Post) Parcels.unwrap(data.getParcelableExtra("post"));

                    posts.add(0, post);

                    postAdapter.notifyItemChanged(0);
                    rvPost.scrollToPosition(0);
                }

            } else  { // Result was a failure
                    Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
                }
        }
}