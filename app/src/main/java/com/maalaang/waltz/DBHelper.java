package com.maalaang.waltz;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by User on 2015-02-03.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DBNAME = Constants.DBNAME;
    public static final int VERSION = 1;

    public DBHelper(Context context) {
        super(context,DBNAME,null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

       String historySql = "CREATE TABLE history (" +
                    "  `_id` INTEGER," +
                    "  `pnum` VARCHAR(45) NOT NULL DEFAULT ''," +
                    "  `name` VARCHAR(45) NOT NULL DEFAULT ''," +
                    "  `time` DATETIME NOT NULL DEFAULT 0," +
                    "  `callingtime` INTEGER NOT NULL DEFAULT 0," +
                    "  `type` INTEGER UNSIGNED NOT NULL DEFAULT 0," +
                    "  PRIMARY KEY(`_id`)" +
                    ");";
        String contactSql = "CREATE TABLE contact (" +
                "  `_id` INTEGER," +
                "  `pnum` VARCHAR(45) NOT NULL DEFAULT ''," +
                "  `name` VARCHAR(45) NOT NULL DEFAULT ''," +
                "  `photo` VARCHAR(150) NOT NULL DEFAULT ''," +
                "  `bio` VARCHAR(150) NOT NULL DEFAULT ''," +
                "  `isreg` INTEGER UNSIGNED NOT NULL DEFAULT 0," +
                "  PRIMARY KEY(`_id`)" +
                ");";
            db.execSQL(historySql);
            db.execSQL(contactSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
    }
}