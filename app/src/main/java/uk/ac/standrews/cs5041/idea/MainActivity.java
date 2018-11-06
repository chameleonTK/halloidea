package uk.ac.standrews.cs5041.idea;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.content.Intent;
import java.util.List;
import android.widget.ListView;

import uk.ac.standrews.cs5041.idea.db.Category;
import uk.ac.standrews.cs5041.idea.db.DatabaseAccess;
import uk.ac.standrews.cs5041.idea.db.Memo;
import android.widget.AdapterView;

public class MainActivity extends AppCompatActivity{

    NavigationController navCtrl;
    private ListView listView;
    private FloatingActionButton btnAdd;
    private DatabaseAccess databaseAccess;
    private List<Memo> memos;
    private MemoAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setTitle("All Ideas");
        toolbar.setTitleTextColor(Color.parseColor("#30122d"));
        toolbar.setSubtitleTextColor(Color.parseColor("#000000"));


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navCtrl = new NavigationController(this);
        btnAdd = (FloatingActionButton) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddClicked(view);
            }
        });

        this.databaseAccess = DatabaseAccess.getInstance(this);
        this.listView = (ListView) findViewById(R.id.listView);
        SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(this.listView, this.getDissmissCallback());
        this.listView.setOnTouchListener(touchListener);
        this.listView.setOnScrollListener(touchListener.makeScrollListener());
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onMemoItemClick(parent, view, position, id);
            }
        });
    }

    public void onMemoItemClick(AdapterView<?> parent, View view, int position, long id) {
        Memo memo = memos.get(position);
        Intent intent = new Intent(this, ViewActivity.class);
        intent.putExtra("memo", memo);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        databaseAccess.open();
        this.memos = Memo.getAllMemos(databaseAccess);

        databaseAccess.close();
        adapter = new MemoAdapter(this, this, memos);
        this.listView.setAdapter(adapter);
    }

    public void onAddClicked(View view) {
        Intent intent = new Intent(this, EditActivity.class);
        startActivity(intent);
    }

    public DismissCallbacks getDissmissCallback() {
        return new DismissCallbacks() {
            @Override
            public boolean canDismiss(int position) {
                return true;
            }

            @Override
            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                MemoAdapter adapter = (MemoAdapter) listView.getAdapter();

                for (int position : reverseSortedPositions) {
                    Snackbar.make(MainActivity.this.listView, "Delete memo successfully", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    Memo memo = adapter.getItem(position);
                    adapter.remove(memo);

                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(MainActivity.this);
                    databaseAccess.open();
                    memo.delete(databaseAccess);
                    databaseAccess.close();
                }
                adapter.notifyDataSetChanged();
            }
        };
    }
}