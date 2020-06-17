package com.example.dormitory.Student.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.dormitory.R;

import java.util.List;
public class GirdDropDownAdapter extends BaseAdapter {

    private Activity context;
    private List<String> list;
    private int checkItemPosition = 4;

    public void setCheckItem(int position) {
        checkItemPosition = position;
        notifyDataSetChanged();
    }

    public int getCheckItemPosition(){
        return checkItemPosition;
    }

    public GirdDropDownAdapter(List<String> list,Activity context) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_drop_down, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        fillValue(position, viewHolder);
        return convertView;
    }

    private void fillValue(int position, ViewHolder viewHolder) {
        viewHolder.mText.setText(list.get(position));
        if (checkItemPosition != -1) {
            if (checkItemPosition == position) {
                viewHolder.mText.setTextColor(context.getResources().getColor(R.color.drop_down_selected));
                //viewHolder.mText.setTextSize(17);
                viewHolder.mText.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            } else {
                viewHolder.mText.setTextColor(context.getResources().getColor(R.color.drop_down_unselected));
                viewHolder.mText.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                //viewHolder.mText.setTextSize(15);
            }
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView mText;
        ViewHolder(View view) {
            super(view);
            mText = (TextView)view.findViewById(R.id.text);
        }
    }
}
