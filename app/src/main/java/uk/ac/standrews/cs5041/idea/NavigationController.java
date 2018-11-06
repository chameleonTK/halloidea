package uk.ac.standrews.cs5041.idea;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class NavigationController implements NavigationView.OnNavigationItemSelectedListener {
    AppCompatActivity app;
    public NavigationController(AppCompatActivity app) {
        this.app = app;
        NavigationView navigationView = (NavigationView) app.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.all_archives) {

        }

        DrawerLayout drawer = (DrawerLayout) app.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
