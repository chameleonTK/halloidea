package uk.ac.standrews.cs5041.idea;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;


import android.content.Context;
//        import android.content.Intent;
//        import android.os.Bundle;
//        import android.support.v7.app.ActionBarActivity;
//        import android.view.View;
import android.view.ViewGroup;
//        import android.widget.AdapterView;
import android.widget.ArrayAdapter;
//        import android.widget.Button;
//        import android.widget.ImageView;
//
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

import uk.ac.standrews.cs5041.idea.db.Category;
import uk.ac.standrews.cs5041.idea.db.Memo;

public class MemoAdapter extends ArrayAdapter<Memo> {

    private AppCompatActivity app;
    private List<Memo> memos;
    public MemoAdapter(AppCompatActivity app, Context context, List<Memo> objects) {
        super(context, 0, objects);
        this.app = app;
        this.memos = objects;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = this.app.getLayoutInflater().inflate(R.layout.layout_list_item, parent, false);
            holder.itemName = (TextView) view.findViewById(R.id.listTitle);
            holder.tagsContainer = (LinearLayout)(view.findViewById(R.id.listTags));
            holder.categoryImage = (ImageView) (view.findViewById(R.id.listCategoryImage));
            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }

//        ImageView btnEdit = (ImageView) convertView.findViewById(R.id.btnEdit);
//        ImageView btnDelete = (ImageView) convertView.findViewById(R.id.btnDelete);
//        TextView txtDate = (TextView) convertView.findViewById(R.id.txtDate);
        final Memo memo = this.memos.get(position);
        holder.itemName.setText(memo.title.toUpperCase());
        showTags(holder, memo);

        String colour = Category.getColor(memo.category);
        Drawable image = Category.getImage(memo.category, this.app);
        holder.categoryImage.setImageDrawable(image);
        holder.categoryImage.getBackground().setColorFilter(Color.parseColor(colour), PorterDuff.Mode.SRC);
        return view;
    }

    public void showTags(ViewHolder holder, Memo memo) {
        String rawTags = memo.tags;
        if (rawTags == null) {
            return;
        }

        String[] tags = rawTags.split(",");
        for (String tagContent: tags) {
            TextView tag = new TextView(app);

            tagContent = tagContent.trim();
            if (!tagContent.startsWith("#")) {
                tag.setText("#" + tagContent);
            } else {
                tag.setText(tagContent);
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMarginEnd(10);
            tag.setLayoutParams(params);
//        Drawable tokenBg = ContextCompat.getDrawable(app, R.drawable.token_background);
//        tag.setBackground(tokenBg);
            tag.setTextColor(Color.parseColor("#ffffff"));
            tag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            tag.setTypeface(null, Typeface.ITALIC);
            holder.tagsContainer.addView(tag);
        }
    }
    /**
     * Static class used to avoid the calling of "findViewById" every time the getView() method is called,
     * because this can impact to your application performance when your list is too big. The class is static so it
     * cache all the things inside once it's created.
     */
    private static class ViewHolder {
        protected TextView itemName;
        protected LinearLayout tagsContainer;
        protected ImageView categoryImage;
    }
}