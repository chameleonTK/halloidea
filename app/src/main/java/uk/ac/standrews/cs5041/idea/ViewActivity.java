package uk.ac.standrews.cs5041.idea;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.Image;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;

import uk.ac.standrews.cs5041.idea.db.Category;
import uk.ac.standrews.cs5041.idea.db.DatabaseAccess;
import uk.ac.standrews.cs5041.idea.db.Memo;

public class ViewActivity extends AppCompatActivity {

    private Memo memo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        Button btnSave = (Button) findViewById(R.id.btnSave);
        Button btnArchive = (Button) findViewById(R.id.btnArchive);
        Button btnDelete = (Button) findViewById(R.id.btnDelete);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            memo = (Memo) bundle.get("memo");
            if(memo == null) {
                finish();
            }
        }

        this.setTitle(memo.title.toUpperCase());
        TextView title = (TextView) findViewById(R.id.viewTitle);
        TextView categoryName = (TextView) findViewById(R.id.viewCategoryName);
        TextView detail = (TextView) findViewById(R.id.viewDetail);
        ImageView categoryImage = (ImageView) findViewById(R.id.viewCategory);


        title.setText(memo.title);
        categoryName.setText(Category.getName(memo.category));
        detail.setText(memo.detail);

        ImageView image = (ImageView) findViewById(R.id.viewImage);
        byte[] data = memo.imageBase64;
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inMutable = true;
        Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length, opt);

        image.setImageBitmap(bm);


        categoryImage.setImageDrawable(Category.getImage(memo.category, this));
        categoryImage.getBackground().setColorFilter(Color.parseColor(Category.getColor(memo.category)), PorterDuff.Mode.SRC);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditClicked();
            }
        });

        btnArchive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveClicked();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteClicked();
            }
        });
    }

    private void onEditClicked() {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("memo", memo);
        startActivity(intent);
    }

    public void onSaveClicked() {
//        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
//        databaseAccess.open();
////        memo.title = etText.getText().toString();
//        memo.update(databaseAccess);
//        databaseAccess.close();
        this.finish();
    }

    public void onDeleteClicked() {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        memo.delete(databaseAccess);
        databaseAccess.close();
        this.finish();
    }
}
