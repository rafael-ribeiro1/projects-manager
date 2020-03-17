package com.rafaribeiro.projectsmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "db_pmanager";

    private static final String TB_PROJECT = "tb_project";
    private static final String COL_IDP = "idProj";
    private static final String COL_NAMEP = "nameProj";
    private static final String COL_ABSP = "abstractProj";
    private static final String COL_DESCP = "descProj";
    private static final String COL_FUNCP = "funcProj";
    private static final String COL_STATE = "state";
    private static final String COL_NREQ = "nReq";

    private static final String COL_IDR = "idReq";
    private static final String COL_PROJID = "projId";
    private static final String COL_STUDYID = "studyId";
    private static final String COL_REQ = "req";

    private static final String TB_STUDY = "tb_study";
    private static final String COL_IDS = "idStu";
    private static final String COL_NAMES = "nameStu";
    private static final String COL_FROM = "fromStu";
    private static final String COL_LINK = "link";
    private static final String COL_NMOD = "nMod";

    private static final String COL_IDM = "idMod";
    private static final String COL_TITLE = "title";

    public DataBase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //Create Database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String QUERY_PROJECT = "CREATE TABLE " + TB_PROJECT + "(" + COL_IDP + " INTEGER PRIMARY KEY, "
                + COL_NAMEP + " TEXT, " + COL_ABSP + " TEXT, " + COL_DESCP + " TEXT, "
                + COL_FUNCP + " TEXT, " + COL_STATE + " INTEGER, " + COL_NREQ + " INTEGER)";
        db.execSQL(QUERY_PROJECT);
        String QUERY_STUDY = "CREATE TABLE " + TB_STUDY + "(" + COL_IDS + " INTEGER PRIMARY KEY, "
                + COL_NAMES + " TEXT, " + COL_FROM + " TEXT, " + COL_LINK + " TEXT, "
                + COL_NMOD + " INTEGER)";
        db.execSQL(QUERY_STUDY);
    }

    //Upgrade Database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public void createTableReq(int projId) {
        String TB_REQ = "REQ" + projId;
        SQLiteDatabase db = this.getWritableDatabase();
        String QUERY_REQ = "CREATE TABLE " + TB_REQ + "(" + COL_IDR + " INTEGER PRIMARY KEY, "
                + COL_PROJID + " INTEGER, " + COL_STUDYID + " INTEGER, " + COL_REQ + " TEXT)";
        db.execSQL(QUERY_REQ);
        db.close();
    }

    public void deleteTableReq(int projId) {
        String TB_REQ = "REQ" + projId;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TB_REQ);
        db.close();
    }

    public void createTableMod(int studyId) {
        String TB_MOD = "MOD" + studyId;
        SQLiteDatabase db = this.getWritableDatabase();
        String QUERY_MOD = "CREATE TABLE " + TB_MOD + "(" + COL_IDM + " INTEGER PRIMARY KEY, "
                + COL_STUDYID + " INTEGER, " + COL_TITLE + " TEXT, " + COL_STATE + " INTEGER)";
        db.execSQL(QUERY_MOD);
        db.close();
    }

    public void deleteTableMod(int studyId) {
        String TB_MOD = "MOD" + studyId;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TB_MOD);
        db.close();
    }

    /* -----------------------------------
       -------------- CRUD ---------------
       ----------------------------------- */



}
