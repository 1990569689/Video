package com.tianyilianmeng.video;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
public class MyAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Bitmap directory,file;
    //存储文件名称
    private ArrayList<String> names = null;
    //存储文件路径
    private ArrayList<String> paths = null;
    //参数初始化
    private ArrayList<Bitmap> icons = null;

    private ArrayList<Integer> list = null;
    public MyAdapter(Context context,ArrayList<String> na,ArrayList<String> pa,ArrayList<Bitmap> icon,ArrayList<Integer> list){
        names = na;
        paths = pa;
        icons=icon;
        directory = BitmapFactory.decodeResource(context.getResources(),R.drawable.d);
        file = BitmapFactory.decodeResource(context.getResources(),R.drawable.f);
        //缩小图片
        directory = small(directory,0.16f);
        file = small(file,0.1f);
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return names.size();
    }
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return names.get(position);
    }
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (null == convertView){
            convertView = inflater.inflate(R.layout.file, null);
            holder = new ViewHolder();
            holder.text = (TextView)convertView.findViewById(R.id.textView);
            holder.image = (ImageView)convertView.findViewById(R.id.imageView);
            holder.length=(TextView)convertView.findViewById(R.id.length);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        File f = new File(paths.get(position).toString());
            holder.text.setText(f.getName());

            if (f.isDirectory()){
                holder.image.setImageBitmap(icons.get(position));

//               holder.length.setText(list.get(position));
            }
            else if (f.isFile()){
                holder.image.setImageBitmap(icons.get(position));
            }
            else{
                System.out.println(f.getName());
            }

        return convertView;
    }
    private class ViewHolder{
        private TextView text,length;
        private ImageView image;

    }
    private Bitmap small(Bitmap map,float num){
        Matrix matrix = new Matrix();
        matrix.postScale(num, num);
        return Bitmap.createBitmap(map,0,0,map.getWidth(),map.getHeight(),matrix,true);
    }
    private String getMIMEType(File file){
        String type = "";
        String name = file.getName();
        //文件扩展名
        String end = name.substring(name.lastIndexOf(".") + 1, name.length()).toLowerCase();
        if (end.equals("m4a") || end.equals("mp3") || end.equals("wav")){
            type = "audio";
        }
        else if(end.equals("mp4") || end.equals("3gp")) {
            type = "video";
        }
        else if (end.equals("jpg") || end.equals("png") || end.equals("jpeg") || end.equals("bmp") || end.equals("gif")){
            type = "image";
        }
        else {
            //如果无法直接打开，跳出列表由用户选择
            type = "*";
        }
        type += "/*";
        return type;
    }
}
