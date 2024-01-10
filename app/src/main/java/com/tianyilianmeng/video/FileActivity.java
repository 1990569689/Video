package com.tianyilianmeng.video;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

public class FileActivity extends Activity {
    //存储文件名称
    private ArrayList<String> Dirnames = null,filenames = null;
    //存储文件路径
    private ArrayList<String> Dirpaths = null,filepaths = null,video,content,banner=null;
    private ArrayList<Bitmap> icon = null,icons=null;
    private ArrayList<Integer>  videolist=null;
    String path="/storage/emulated/0/Download/3D影院",paths;
    GridView listView;
    private View view;
    private EditText editText;
    String title;
    MyAdapter list;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        //显示文件列表
        listView = (GridView) findViewById(R.id.list);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
            //这是状态栏文字反色
            setDarkStatusIcon(true);
        }
        show(path,false);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
      // showFileDir(path, false);
    }
     public void show(String path,Boolean value)
     {
         if (!value) {
             File folder = new File(path);
             Dirnames = new ArrayList<String>();
             Dirpaths = new ArrayList<String>();
             filenames = new ArrayList<String>();
             filepaths = new ArrayList<String>();
             video = new ArrayList<String>();
             content = new ArrayList<String>();
             banner = new ArrayList<String>();
             icon = new ArrayList<Bitmap>();
             icons = new ArrayList<Bitmap>();
             if (folder.exists()) {
                 File[] files = folder.listFiles();
                 Log.w("filee", files + "");
                 if (files != null) {
                     for (File f : files) {
                         // do something with the file
                         if (f.isDirectory()) {
                             title = f.getName();
                             File[] file = f.listFiles();
                             for (File fi : file) {
                                 if (fi.getName().equals("icon.png")) {
                                     Dirnames.add(f.getName());
                                     Dirpaths.add(f.getPath());
                                     Bitmap bitmap = BitmapFactory.decodeFile(fi.getAbsolutePath());
                                     icons.add(bitmap);
                                 } else {
                                 }
                                 if (fi.getName().equals("banner.png")) {
                                     banner.add(fi.getAbsolutePath());
                                 } else {
                                 }
                                 if (fi.getName().equals(title)) {
                                     try {
                                         FileInputStream fis = new FileInputStream(fi.getAbsolutePath());
                                        InputStreamReader isr = new InputStreamReader(fis);
                                        BufferedReader br = new BufferedReader(isr);
                                        StringBuilder sb = new StringBuilder();
                                        String line;
                                        while ((line = br.readLine()) != null) {
                                            sb.append(line);
                                            sb.append("\n");
                                        }
                                        br.close();
                                        isr.close();
                                        fis.close();
                                        content.add(sb.toString());
                                         } catch (IOException e) {e.printStackTrace();}
                                 } else {
                                 }
                                 if (fi.getName().endsWith(".mp4")||fi.getName().endsWith(".m3u8")||fi.getName().endsWith(".mpg")||fi.getName().endsWith(".m2v")) {
                                  video.add(fi.getAbsolutePath());
                                 } else {
                                 }
                             }
                         } else {
                             if (getMIMEType(f).equals("video/*")) {
                                 MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                                 retriever.setDataSource(f.getAbsolutePath());
                                 Bitmap firstFrame = retriever.getFrameAtTime(0);
                                 filenames.add(f.getName());
                                 filepaths.add(f.getPath());
                                 icon.add(firstFrame);
                             }
                         }
                     }
                     Dirnames.addAll(filenames);
                     Dirpaths.addAll(filepaths);
                     icons.addAll(icon);
                     list = new MyAdapter(this, Dirnames, Dirpaths, icons, videolist);
                     listView.setAdapter(list);
                     listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                         @Override
                         public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                             String path = Dirpaths.get(position);
                             String title=Dirnames.get(position);
                             File file = new File(path);
                             // 文件存在并可读
                             if (file.exists() && file.canRead()) {
                                 if (file.isDirectory()) {
                                     //显示子目录及文件
                                     paths = path;
                                     //show(path, true);
                                     Intent intent = new Intent(FileActivity.this, ContentActivity.class);
                                     intent.putExtra("bitmap", banner.get(position));
                                     intent.putExtra("title",title);
                                     intent.putExtra("video",video.get(position));
                                     intent.putExtra("content",content.get(position));
                                     startActivity(intent);

                                 } else {
                                     if (getMIMEType(file).equals("video/*")) {
                                         Intent intent = new Intent(FileActivity.this, PlayerActivity.class);
                                         intent.putExtra("name", file.getName());
                                         intent.putExtra("path", file.getAbsolutePath());
                                         startActivity(intent);
                                     } else {
                                     }

                                 }
                             }

                         }
                     });
                 } else {
                     Toast.makeText(this, "没有找到电影", Toast.LENGTH_SHORT).show();
                 }
             } else {
                 Toast.makeText(this, "SD卡没有被装载", Toast.LENGTH_SHORT).show();
             }
         }else
         {
             filepaths.clear();
             filenames.clear();
             icon.clear();
             icons.clear();
             File folder = new File(path);
             Dirnames = new ArrayList<String>();
             Dirpaths = new ArrayList<String>();
             filenames = new ArrayList<String>();
             filepaths = new ArrayList<String>();
             if (folder.exists()) {
                 File[] files = folder.listFiles();
                 for (File f : files) {
                     if (f.isDirectory()) {
                         Dirnames.add(f.getName());
                         Dirpaths.add(f.getPath());
                         icons.add(BitmapFactory.decodeResource(getResources(), R.drawable.d));
                     } else {
                         if (getMIMEType(f).equals("video/*")) {
                             MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                             retriever.setDataSource(f.getAbsolutePath());
                             Bitmap firstFrame = retriever.getFrameAtTime(0);
                             filenames.add(f.getName());
                             filepaths.add(f.getPath());
                             icon.add(firstFrame);
                         }
                     }
                 }

                 Dirnames.addAll(filenames);
                 Dirpaths.addAll(filepaths);
                 icons.addAll(icon);
                 list = new MyAdapter(this, Dirnames, Dirpaths, icons, videolist);
                 listView.setAdapter(list);
                 listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                     @Override
                     public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                         String path = Dirpaths.get(position);
                         File file = new File(path);
                         // 文件存在并可读
                         if (file.exists() && file.canRead()) {
                             if (file.isDirectory()) {
                                 //显示子目录及文件
                                 paths = path;
                                 show(path, true);
                             } else {
                                 if (getMIMEType(file).equals("video/*")) {
                                     Intent intent = new Intent(FileActivity.this, PlayerActivity.class);
                                     intent.putExtra("name", file.getName());
                                     intent.putExtra("path", file.getAbsolutePath());
                                     startActivity(intent);
                                 } else {
                                 }

                             }
                         }

                     }
                 });
             } else {
                 Toast.makeText(this, "SD卡没有被装载", Toast.LENGTH_SHORT).show();
             }

         }
         }
/*
    public void showFileDir(String path, Boolean value) {
      //  paths=Environment.getExternalStorageDirectory().getAbsoluteFile() + "/" + path;Environment.getExternalStorageDirectory().getAbsoluteFile()
        if (!value) {
            boolean isLoadSDCard = true;
            //Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
            if (isLoadSDCard) {
                // 获取SD卡的根目录
                File file = new File(path);
                if(!file.exists())
                {
                    file.mkdir();
                }
                Dirnames = new ArrayList<String>();
                Dirpaths = new ArrayList<String>();
                filenames = new ArrayList<String>();
                filepaths = new ArrayList<String>();
                icon=new ArrayList<Bitmap>();
                icons=new ArrayList<Bitmap>();
                File[] files = file.listFiles();
                //如果当前目录不是根目录
                //添加所有文件
                for (File f : files) {
                    if(f.isDirectory())
                    {
                        Dirnames.add(f.getName());
                        Dirpaths.add(f.getPath());
                        icons.add(BitmapFactory.decodeResource(getResources(),R.drawable.d));
                    }else {
                        if(getMIMEType(f).equals("video/*"))
                        {
                            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                            retriever.setDataSource(f.getAbsolutePath());
                            Bitmap firstFrame = retriever.getFrameAtTime(0);
                            filenames.add(f.getName());
                            filepaths.add(f.getPath());
                            icon.add(firstFrame);
                            //retriever.release();
                        }
                    }
                }
                Dirnames.addAll(filenames);
                Dirpaths.addAll(filepaths);
                icons.addAll(icon);
                list = new MyAdapter(this, Dirnames, Dirpaths,icons,videolist);
                listView.setAdapter(list);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        String path = Dirpaths.get(position);
                        File file = new File(path);
                        // 文件存在并可读
                        if (file.exists() && file.canRead()) {
                            if (file.isDirectory()) {
                                //显示子目录及文件
                                paths=path;
                                showFileDir(path, true);
                            } else {
                                if(getMIMEType(file).equals("video/*")) {
                                    Intent intent = new Intent(FileActivity.this,PlayerActivity.class);
                                    intent.putExtra("name", file.getName());
                                    intent.putExtra("path", file.getAbsolutePath());
                                    startActivity(intent);
                                }else
                                {

                                }

                            }
                        }

                    }
                });
            } else {
                Toast.makeText(this, "SD卡没有被装载", Toast.LENGTH_SHORT).show();
            }
        } else {
            filepaths.clear();
            filenames.clear();
            icon.clear();
            icons.clear();

            boolean isLoadSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
            if (isLoadSDCard) {
                // 获取SD卡的根目录
                File file = new File(path);
                Dirnames = new ArrayList<String>();
                Dirpaths = new ArrayList<String>();
                File[] files = file.listFiles();
                //如果当前目录不是根目录
                //添加所有文件
                for (File f : files) {
                    if(f.isDirectory())
                    {

                        Dirnames.add(f.getName());
                        Dirpaths.add(f.getPath());
                        icons.add(BitmapFactory.decodeResource(getResources(),R.drawable.d));
                    }else {
                       if(getMIMEType(f).equals("video/*"))
                       {

                           MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                           retriever.setDataSource(f.getAbsolutePath());
                           Bitmap firstFrame = retriever.getFrameAtTime(0);
                           filenames.add(f.getName());
                           filepaths.add(f.getPath());
                           icon.add(firstFrame);
                          // retriever.release();

                       }
                    }
                }
                Dirnames.addAll(filenames);
                Dirpaths.addAll(filepaths);
                icons.addAll(icon);

                list = new MyAdapter(this,  Dirnames,  Dirpaths,icons,videolist);

                listView.setAdapter(list);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        String path =  Dirpaths.get(position);
                        File file = new File(path);
                        // 文件存在并可读
                        if (file.exists() && file.canRead()) {
                            if (file.isDirectory()) {
                                //显示子目录及文件
                                paths=path;
                                showFileDir(path, true);
                                // showFileDir(path);
                            } else {
                                if(getMIMEType(file).equals("video/*")) {
                                    Intent intent = new Intent(FileActivity.this,PlayerActivity.class);
                                    intent.putExtra("name", file.getName());
                                    intent.putExtra("path", file.getAbsolutePath());
                                    startActivity(intent);
                                }else
                                {
                                }
                            }
                        }

                    }
                });
            }

        }

    }

 */
    //打开文件
    //获取文件mimetype
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
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void exit() {
        if(Objects.equals(paths, Environment.getExternalStorageDirectory().getAbsoluteFile() + "/" + path))
        {
            finish();
            System.exit(0);
        }else
        {
                show(path,false);
        }
    }
    protected void setDarkStatusIcon(boolean isDark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = getWindow().getDecorView();
            if (decorView != null) {
                int vis = decorView.getSystemUiVisibility();
                if (isDark) {
                    vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                } else {
                    vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                decorView.setSystemUiVisibility(vis);
            }
        }
    }

}