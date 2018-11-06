package uk.ac.standrews.cs5041.idea;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import uk.ac.standrews.cs5041.idea.db.Category;
import uk.ac.standrews.cs5041.idea.db.DatabaseAccess;
import uk.ac.standrews.cs5041.idea.db.Memo;

public class EditActivity extends AppCompatActivity {
    private static final int PICK_PHOTO = 1;
    private EditText etTitle, etDetail, etTags;
    private int category;
    private Memo memo;
    private List<ImageView> categories;
    private ImageView image;
    private byte[] imageBase64;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        this.etTitle = (EditText) findViewById(R.id.edit_title);
        this.etDetail = (EditText) findViewById(R.id.edit_detail);
        this.etTags = (EditText) findViewById(R.id.edit_tags);

        categories = new ArrayList<ImageView>();
        categories.add((ImageView) findViewById(R.id.category_candy));
        categories.add((ImageView) findViewById(R.id.category_meal));
        categories.add((ImageView) findViewById(R.id.category_pumpkin));
        categories.add((ImageView) findViewById(R.id.category_frankenstein));
        categories.add((ImageView) findViewById(R.id.category_mummy));
        this.showAsColour(null, 128);
        for(final ImageView imv: this.categories) {
            imv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    category = imv.getId();
                    showAsColour(imv, 255);
                }
            });
        }

        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveClicked();
            }
        });

        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelClicked();
            }
        });

        this.image = (ImageView) findViewById(R.id.edit_image);
        this.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            memo = (Memo) bundle.get("memo");
            if(memo != null) {
                this.etTitle.setText(memo.title);
                this.etDetail.setText(memo.detail);
                this.etTags.setText(memo.tags);
                ImageView imv = (ImageView) (findViewById(memo.category));
                this.category = imv.getId();
                showAsColour(imv, 255);

                byte[] data = memo.imageBase64;
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inMutable = true;
                Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length, opt);
                this.imageBase64 = this.imageToByteArray(bm);
                this.image.setImageBitmap(bm);
            }
        }

    }

    public void onSaveClicked() {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        String title = this.etTitle.getText().toString();
        String detail = this.etDetail.getText().toString();
        String tags = this.etTags.getText().toString();
        if(memo == null) {
            // Add new memo
            Memo temp = new Memo(title, detail);
            temp.tags = tags;
            temp.category = category;
            temp.imageBase64 = imageBase64;
            temp.save(databaseAccess);
        } else {
            // Update the memo
            memo.title = title;
            memo.detail = detail;
            memo.tags = tags;
            memo.category = category;
            memo.imageBase64 = imageBase64;
            memo.update(databaseAccess);
        }
        databaseAccess.close();
        this.finish();
    }

    public void  showAsColour(ImageView v, int scale)
    {
        for(ImageView imv: this.categories) {
            this.grayScale(imv, scale);
        }

        if (v == null) {
            return;
        }
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(1);
        ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
        v.setColorFilter(cf);
        v.setImageAlpha(scale);

        v.getBackground().setColorFilter(Color.parseColor(Category.getColor(v.getId())), PorterDuff.Mode.SRC);
    }

    public void  grayScale(ImageView v, int scale)
    {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
        v.setColorFilter(cf);
        v.setImageAlpha(scale);
        Drawable grayBg = ContextCompat.getDrawable(this, R.drawable.category_gray_bg);
        v.setBackground(grayBg);

    }

    public void onCancelClicked() {
        this.finish();
    }

    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Snackbar.make(this.image, "Something went wrong", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                return;
            }

            try {
                Bitmap resized = this.getImage(data);
                this.imageBase64 = this.imageToByteArray(resized);
                this.image.setImageBitmap(resized);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Snackbar.make(EditActivity.this.image, "Something went wrong", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        } else if (requestCode == PICK_PHOTO) {
            Snackbar.make(this.image, "Something went wrong", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    private byte[] imageToByteArray(Bitmap resized) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        resized.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    public Bitmap getImage(Intent data) throws FileNotFoundException {
        final Uri imageUri = data.getData();
        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
        int inWidth = selectedImage.getWidth();
        int inHeight = selectedImage.getHeight();
        int outWidth, outHeight;
        if(inWidth > inHeight){
            outWidth = 150;
            outHeight = (inHeight * 150) / inWidth;
        } else {
            outHeight = 150;
            outWidth = (inWidth * 150) / inHeight;
        }

        final Bitmap resized = Bitmap.createScaledBitmap(selectedImage, outWidth, outHeight, true);
        return resized;
    }
}