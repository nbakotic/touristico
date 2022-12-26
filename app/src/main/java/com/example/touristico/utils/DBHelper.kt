package com.example.touristico.utils

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    // create databases & tables
    override fun onCreate(db: SQLiteDatabase) {
        // below is a sqlite query, where column names
        // along with their data types is given
        val createBeachTable = ("CREATE TABLE " + BEACH_TABLE + " (" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_NAME + " TEXT," +
                KEY_ADDRESS + " TEXT," +
                KEY_DISTANCE + " TEXT," +
                KEY_TYPE + " TEXT," +
                KEY_EXTRA + " TEXT," +
                KEY_URL + " TEXT" + ")")

        db.execSQL(createBeachTable)

        val createImageTable = ("CREATE TABLE " + IMAGE_TABLE + " (" +
                KEY_URL + " TEXT PRIMARY KEY," +
                KEY_IMAGE + " BLOB" + ")")
        db.execSQL(createImageTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        // this method is to check if table already exists
        db.execSQL("DROP TABLE IF EXISTS $BEACH_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $IMAGE_TABLE")
        onCreate(db)
    }

    // add beach
    fun addBeach(name : String, address : String, distance: String, type: String, extra: String, url: String ){

        // content values variable
        val values = ContentValues()

        // insert values
        values.put(KEY_NAME, name)
        values.put(KEY_ADDRESS, address)
        values.put(KEY_DISTANCE, distance)
        values.put(KEY_TYPE, type)
        values.put(KEY_EXTRA, extra)
        values.put(KEY_URL, url)

        // insert value in database
        val db = this.writableDatabase

        // all values are inserted into database
        db.insert(BEACH_TABLE, null, values)

        // closing database
        db.close()
    }

    // get beach
    fun getBeach(): Cursor? {

        // readable db, no need to close
        val db = this.readableDatabase

        // query db
        return db.rawQuery("SELECT * FROM $BEACH_TABLE", null)
    }



    fun updateBeach(id : Int, name : String, address : String, distance: String, type: String, extra: String) {
        val values = ContentValues()

        values.put(KEY_NAME, name)
        values.put(KEY_ADDRESS, address)
        values.put(KEY_DISTANCE, distance)
        values.put(KEY_TYPE, type)
        values.put(KEY_EXTRA, extra)

        val db = this.writableDatabase
        db.update(BEACH_TABLE,  values, "$KEY_ID=$id", null)
        db.close()

    }

    fun getItemWithId(table : String, id : Int) : Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $table WHERE $KEY_ID=$id", null)
    }

    fun deleteItemWithId(table : String, id : Int) {
        val db = this.writableDatabase
        db.delete(table, "$KEY_ID=$id", null)
        db.close()
    }


    //add image
    fun addImage(url : String, image : Bitmap) {

        val imgData : ByteArray = Tools.bitmapTosByteArray(image)

        val values = ContentValues()

        values.put(KEY_URL, url)
        values.put(KEY_IMAGE, imgData)

        val db = this.writableDatabase

        db.insert(IMAGE_TABLE, null, values)

        db.close()

    }

    //get image
    fun getImage(url: String): Bitmap? {
        val db = this.readableDatabase

        val imgQuery = "SELECT $KEY_IMAGE FROM $IMAGE_TABLE WHERE $KEY_URL='$url'"

        val cursor : Cursor = db.rawQuery(imgQuery, null)

        if (cursor.moveToFirst()) {
            val imgByte : ByteArray = cursor.getBlob(0)
            cursor.close()
            return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.size)
        }
        if (!cursor.isClosed) {
            cursor.close()
        }
        return null
    }

    fun deleteImage(url : String) {
        val db = this.writableDatabase
        db.delete(IMAGE_TABLE, "$KEY_URL='$url'", null)
        db.close()
    }


    companion object{
        // database variables

        // database name
        private const val DATABASE_NAME = "TOURISTICO"

        // database version
        private const val DATABASE_VERSION = 1

        // table names
        //const val INFO_TABLE = "info"
        //const val ATTRACTION_TABLE = "attraction"
        const val BEACH_TABLE = "beach"
        //const val GUESTBOOK_TABLE = "guestbook"
        //const val DEVICE_TABLE = "device"
        //const val RESTAURANT_TABLE = "restaurant"
        //const val SHOPS_TABLE = "shops"
        const val IMAGE_TABLE = "image"

        const val KEY_ID = "id"
        const val KEY_NAME = "name"
        const val KEY_ADDRESS = "address"
        const val KEY_DISTANCE = "distance"
        const val KEY_TYPE = "type"
        const val KEY_EXTRA = "extra"
        const val KEY_URL = "url"
        const val KEY_IMAGE = "image"
    }
}