package org.undp_iwomen.iwomen.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by thitoo on 5/14/15.
 */
public class SqliteHelper extends SQLiteOpenHelper{

    private  static final String DATABASE_NAME = "IWOMENDB";
    private  static int DATABASE_VERSION = 1;


   public SqliteHelper(Context context){
       super(context,DATABASE_NAME,null,DATABASE_VERSION);
   }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(TableAndColumnsName.UserUtil.CREATE_USER_TABLE);
        sqLiteDatabase.execSQL(TableAndColumnsName.PostUtil.CREATE_POST_TABLE);
        sqLiteDatabase.execSQL(TableAndColumnsName.CommentUtil.CREATE_COMMENT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldversion, int newversion) {


        sqLiteDatabase.execSQL("Drop Table If Exists "+ TableAndColumnsName.TableNames.USER);
        sqLiteDatabase.execSQL("Drop Table If Exists "+ TableAndColumnsName.TableNames.POST);
        sqLiteDatabase.execSQL("Drop Table If Exists "+ TableAndColumnsName.TableNames.COMMENT);

    }


}
