package com.example.resend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.resend.models.firestore.FireStoreUser;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class HomepageActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;

    Button AddMoneyBtn;
    ImageButton menu_icon;
    TextView profile_abb;

    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private final String TAG = "APP_TEST";

    private FireStoreUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();


        initElements();
        if(firebaseAuth.getCurrentUser() == null) {
            logout();
        }

        final MyAdapter adapter = new MyAdapter(this,getSupportFragmentManager(),
                tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        menu_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(HomepageActivity.this, menu_icon);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        int id = item.getItemId();
                        switch (id){
                            case R.id.buffer:
                                Toast.makeText(HomepageActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.logout:
                                logout();
                                break;
                        }
                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        }); //closing the setOnClickListener method
    }

    private void initElements() {
        AddMoneyBtn = findViewById(R.id.addMoneyBtn);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        profile_abb = findViewById(R.id.profile_abb);
        menu_icon = findViewById(R.id.menu_icon);
        AddMoneyBtn.setOnClickListener(v -> gotoAddMoney());
        tabLayout.addTab(tabLayout.newTab().setText("Accounts"));
        tabLayout.addTab(tabLayout.newTab().setText("Transactions"));
        tabLayout.addTab(tabLayout.newTab().setText("Friends"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        fetchUser();
    }

    private void logout() {
        // Todo Start screen loader here
        Log.v(TAG, "Clicked logout");
        firebaseAuth.signOut();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finishAffinity();
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
                        setUserAcronym();
                    }
                } else {
                    Log.v(TAG, "Error getting documents: ", task.getException());
                }
            });
        }
    }

    private void setUserAcronym() {
        String [] SepName = user.fullName.split(" ");
        char ch1, ch2;
        String sh1, sh2, acr="";
        ch1 = SepName[0].charAt(0);
        ch2 = SepName[1].charAt(0);

        sh1 = String.valueOf(ch1);
        sh2 = String.valueOf(ch2);
        acr = sh1.concat(sh2);
        profile_abb.setText(getString(R.string.profile_abb, acr));
    }

    private void gotoAddMoney() {
        Log.v(TAG, "Clicked Add money");
        Intent intent = new Intent(this, AddMoneyActivity.class);
        startActivity(intent);
    }
}