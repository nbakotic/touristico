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
        // BEACH TABLE
        val createBeachTable = ("CREATE TABLE " + BEACH_TABLE + " (" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_NAME + " TEXT," +
                KEY_ADDRESS + " TEXT," +
                KEY_DISTANCE + " TEXT," +
                KEY_TYPE + " TEXT," +
                KEY_EXTRA + " TEXT," +
                KEY_URL + " TEXT" + ")")
        db.execSQL(createBeachTable)

        val createAttractionTable = ("CREATE TABLE " + ATTRACTION_TABLE + " (" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_NAME + " TEXT," +
                KEY_ADDRESS + " TEXT," +
                KEY_DISTANCE + " TEXT," +
                KEY_HOURS + " TEXT," +
                KEY_DESC + " TEXT," +
                KEY_URL + " TEXT" + ")")
        db.execSQL(createAttractionTable)

        val createDeviceTable = ("CREATE TABLE " + DEVICE_TABLE + " (" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_NAME + " TEXT," +
                KEY_DESC + " TEXT," +
                KEY_URL + " TEXT" + ")")
        db.execSQL(createDeviceTable)

        val createInfoTable = ("CREATE TABLE " + INFO_TABLE + " (" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_GUESTNAME + " TEXT," +
                KEY_GUESTCOUNTRY + " TEXT," +
                KEY_APPARTMENTNAME+ " TEXT," +
                KEY_APPARTMENTADDRESS + " TEXT," +
                KEY_WIFINAME + " TEXT," +
                KEY_WIFIPASSWORD + " TEXT" + ")")
        db.execSQL(createInfoTable)

        val createRestaurantTable = ("CREATE TABLE " + RESTAURANT_TABLE + " (" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_NAME + " TEXT," +
                KEY_ADDRESS + " TEXT," +
                KEY_DISTANCE + " TEXT," +
                KEY_HOURS + " TEXT," +
                KEY_FOOD + " TEXT," +
                KEY_URL + " TEXT" + ")")
        db.execSQL(createRestaurantTable)

        val createShopTable = ("CREATE TABLE " + SHOP_TABLE + " (" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_NAME + " TEXT," +
                KEY_ADDRESS + " TEXT," +
                KEY_DISTANCE + " TEXT" + ")")
        db.execSQL(createShopTable)

        val createGuestbookTable = ("CREATE TABLE " + GUESTBOOK_TABLE + " (" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_GUESTNAME + " TEXT," +
                KEY_GUESTCOUNTRY + " TEXT," +
                KEY_TIME + " TEXT," +
                KEY_POSITIVE + " TEXT," +
                KEY_NEGATIVE + " TEXT," +
                KEY_STARS + " TEXT" + ")")
        db.execSQL(createGuestbookTable)

        val createImageTable = ("CREATE TABLE " + IMAGE_TABLE + " (" +
                KEY_URL + " TEXT PRIMARY KEY," +
                KEY_IMAGE + " BLOB" + ")")
        db.execSQL(createImageTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        // this method is to check if table already exists
        db.execSQL("DROP TABLE IF EXISTS $BEACH_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $ATTRACTION_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $DEVICE_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $INFO_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $RESTAURANT_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $SHOP_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $GUESTBOOK_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $IMAGE_TABLE")
        onCreate(db)
    }


    fun addBeach(name : String, address : String, distance: String, type: String, extra: String, url: String ){
        val values = ContentValues()

        values.put(KEY_NAME, name)
        values.put(KEY_ADDRESS, address)
        values.put(KEY_DISTANCE, distance)
        values.put(KEY_TYPE, type)
        values.put(KEY_EXTRA, extra)
        values.put(KEY_URL, url)

        val db = this.writableDatabase

        db.insert(BEACH_TABLE, null, values)

        db.close()
    }

    fun getBeach(): Cursor? {
        val db = this.readableDatabase

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


    fun addAttraction(name : String, address : String, distance: String, hours: String, desc: String, url: String ){
        val values = ContentValues()

        values.put(KEY_NAME, name)
        values.put(KEY_ADDRESS, address)
        values.put(KEY_DISTANCE, distance)
        values.put(KEY_HOURS, hours)
        values.put(KEY_DESC, desc)
        values.put(KEY_URL, url)

        val db = this.writableDatabase

        db.insert(ATTRACTION_TABLE, null, values)

        db.close()
    }

    fun getAttraction(): Cursor? {
        val db = this.readableDatabase

        return db.rawQuery("SELECT * FROM $ATTRACTION_TABLE", null)
    }

    fun updateAttraction(id : Int, name : String, address : String, distance: String, hours: String, desc: String) {
        val values = ContentValues()

        values.put(KEY_NAME, name)
        values.put(KEY_ADDRESS, address)
        values.put(KEY_DISTANCE, distance)
        values.put(KEY_HOURS, hours)
        values.put(KEY_DESC, desc)

        val db = this.writableDatabase
        db.update(ATTRACTION_TABLE,  values, "$KEY_ID=$id", null)
        db.close()
    }


    fun addShop(name : String, address : String, distance: String){
        val values = ContentValues()

        values.put(KEY_NAME, name)
        values.put(KEY_ADDRESS, address)
        values.put(KEY_DISTANCE, distance)

        val db = this.writableDatabase

        db.insert(SHOP_TABLE, null, values)

        db.close()
    }

    fun getShop(): Cursor? {
        val db = this.readableDatabase

        return db.rawQuery("SELECT * FROM $SHOP_TABLE", null)
    }

    fun updateShop(id : Int, name : String, address : String, distance: String) {
        val values = ContentValues()

        values.put(KEY_NAME, name)
        values.put(KEY_ADDRESS, address)
        values.put(KEY_DISTANCE, distance)

        val db = this.writableDatabase
        db.update(SHOP_TABLE,  values, "$KEY_ID=$id", null)
        db.close()
    }


    fun addRestaurant(name : String, address : String, distance: String, hours: String, food: String, url: String ){
        val values = ContentValues()

        values.put(KEY_NAME, name)
        values.put(KEY_ADDRESS, address)
        values.put(KEY_DISTANCE, distance)
        values.put(KEY_HOURS, hours)
        values.put(KEY_FOOD, food)
        values.put(KEY_URL, url)

        val db = this.writableDatabase

        db.insert(RESTAURANT_TABLE, null, values)

        db.close()
    }

    fun getRestaurant(): Cursor? {
        val db = this.readableDatabase

        return db.rawQuery("SELECT * FROM $RESTAURANT_TABLE", null)
    }

    fun updateRestaurant(id : Int, name : String, address : String, distance: String, hours: String, food: String) {
        val values = ContentValues()

        values.put(KEY_NAME, name)
        values.put(KEY_ADDRESS, address)
        values.put(KEY_DISTANCE, distance)
        values.put(KEY_HOURS, hours)
        values.put(KEY_DESC, food)

        val db = this.writableDatabase
        db.update(RESTAURANT_TABLE,  values, "$KEY_ID=$id", null)
        db.close()
    }


    fun addDevice(name : String, desc: String, url: String ){
        val values = ContentValues()

        values.put(KEY_NAME, name)
        values.put(KEY_DESC, desc)
        values.put(KEY_URL, url)


        val db = this.writableDatabase

        db.insert(DEVICE_TABLE, null, values)

        db.close()
    }

    fun getDevice(): Cursor? {
        val db = this.readableDatabase

        return db.rawQuery("SELECT * FROM $DEVICE_TABLE", null)
    }

    fun updateDevice(id : Int, name : String, desc: String) {
        val values = ContentValues()

        values.put(KEY_NAME, name)
        values.put(KEY_DESC, desc)

        val db = this.writableDatabase
        db.update(DEVICE_TABLE,  values, "$KEY_ID=$id", null)
        db.close()
    }


    fun getGuestbook(): Cursor? {
        val db = this.readableDatabase

        return db.rawQuery("SELECT * FROM $GUESTBOOK_TABLE", null)
    }


    fun initInfo() {
        val values = ContentValues()

        values.put(KEY_GUESTNAME, "")
        values.put(KEY_GUESTCOUNTRY, "")
        values.put(KEY_APPARTMENTNAME, "")
        values.put(KEY_APPARTMENTADDRESS, "")
        values.put(KEY_WIFINAME, "")
        values.put(KEY_WIFIPASSWORD, "")

        val db = this.writableDatabase

        db.insert(INFO_TABLE, null, values)

        db.close()
    }

    fun getInfo(): Cursor? {
        val db = this.readableDatabase

        return db.rawQuery("SELECT * FROM $INFO_TABLE", null)
    }

    fun updateInfo(key1 : String, value1: String, key2: String, value2: String) {
        val values = ContentValues()

        values.put(key1, value1)
        values.put(key2, value2)

        val db = this.writableDatabase
        db.update(INFO_TABLE,  values, "$KEY_ID=1", null)
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
        const val INFO_TABLE = "info"
        const val ATTRACTION_TABLE = "attraction"
        const val BEACH_TABLE = "beach"
        const val GUESTBOOK_TABLE = "guestbook"
        const val DEVICE_TABLE = "device"
        const val RESTAURANT_TABLE = "restaurant"
        const val SHOP_TABLE = "shops"
        const val IMAGE_TABLE = "image"

        const val KEY_ID = "id"
        const val KEY_NAME = "name"
        const val KEY_ADDRESS = "address"
        const val KEY_DISTANCE = "distance"
        const val KEY_TYPE = "type"
        const val KEY_EXTRA = "extra"
        const val KEY_URL = "url"
        const val KEY_IMAGE = "image"
        const val KEY_HOURS = "hours"
        const val KEY_DESC = "description"
        const val KEY_GUESTNAME = "guestName"
        const val KEY_GUESTCOUNTRY = "guestCountry"
        const val KEY_APPARTMENTNAME = "appName"
        const val KEY_APPARTMENTADDRESS = "appAddress"
        const val KEY_WIFINAME = "wifiName"
        const val KEY_WIFIPASSWORD = "wifiPass"
        const val KEY_FOOD = "food"
        const val KEY_TIME = "time"
        const val KEY_POSITIVE = "positive"
        const val KEY_NEGATIVE = "negative"
        const val KEY_STARS = "stars"
    }
}