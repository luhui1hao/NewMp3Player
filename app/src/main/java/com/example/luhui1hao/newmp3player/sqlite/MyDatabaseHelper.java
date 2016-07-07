package com.example.luhui1hao.newmp3player.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.luhui1hao.newmp3player.model.Mp3Info;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luhui on 2016/6/21.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_MP3INFOS = "create table Mp3Infos("
            + "title text, "
            + "duration integer,"
            + "artist text,"
            + "id integer,"
            + "displayName text,"
            + "url text,"
            + "albumId integer,"
            + "album text,"
            + "size integer)";

    Context mContext;
    private static MyDatabaseHelper dbHelper = null;
    private static int version = 1;
    public static final String TAG = "MyDatabaseHelper";
    public List<Mp3Info> list = new ArrayList<>();

    private MyDatabaseHelper(Context context, String name,
                             SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    public static MyDatabaseHelper getInstance(Context context) {
        if (dbHelper == null) {
            dbHelper = new MyDatabaseHelper(context, "GroupChatSrv.db", null,
                    version);
            return dbHelper;
        } else {
            return dbHelper;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MP3INFOS);
        Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        version = newVersion;
    }

    /**
     * 将一条完整的Mp3信息插入数据表中
     * @param mp3Info
     * @return
     */
    public long insertMp3Info(Mp3Info mp3Info) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", mp3Info.getTitle());
        values.put("duration", mp3Info.getDuration());
        values.put("artist", mp3Info.getArtist());
        values.put("id", mp3Info.getId());
        values.put("displayName", mp3Info.getDisplayName());
        values.put("url", mp3Info.getUrl());
        values.put("albumId", mp3Info.getAlbumId());
        values.put("album", mp3Info.getAlbum());
        values.put("size", mp3Info.getSize());
        return db.insert("Mp3Infos", null, values);
    }

    /**
     * 插入一组完整的Mp3信息
     */
    public void insertMp3Infos(List<Mp3Info> list){
        for(Mp3Info mp3Info : list){
            //只取以mp3结尾的文件
            if(mp3Info.getDisplayName().endsWith(".mp3")){
                insertMp3Info(mp3Info);
            }
        }
    }

    /**
     * 清空数据表
     */
    public void clear() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("Mp3Infos", null, null);
    }

    /**
     * 返回本地歌曲列表
     */
    public List<Mp3Info> getMp3Infos(){
        //首先清空list
        list.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("Mp3Infos",null ,null ,null ,null ,null ,null);
        while(cursor.moveToNext()){
            String title = cursor.getString(cursor.getColumnIndex("title"));
            long duration = cursor.getLong(cursor.getColumnIndex("duration"));
            String artist = cursor.getString(cursor.getColumnIndex("artist"));
            long id = cursor.getLong(cursor.getColumnIndex("id"));
            String displayName = cursor.getString(cursor.getColumnIndex("displayName"));
            String url = cursor.getString(cursor.getColumnIndex("url"));
            long albumId = cursor.getLong(cursor.getColumnIndex("albumId"));
            String album = cursor.getString(cursor.getColumnIndex("album"));
            long size = cursor.getLong(cursor.getColumnIndex("size"));

            Mp3Info mp3Info = new Mp3Info();
            mp3Info.setTitle(title);
            mp3Info.setDuration(duration);
            mp3Info.setArtist(artist);
            mp3Info.setId(id);
            mp3Info.setDisplayName(displayName);
            mp3Info.setUrl(url);
            mp3Info.setAlbumId(albumId);
            mp3Info.setAlbum(album);
            mp3Info.setSize(size);

            list.add(mp3Info);
        }
        return list;
    }
}
