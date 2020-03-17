package com.rafaribeiro.projectsmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

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

    public long insertProject(Project project) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAMEP, project.getNameProj());
        values.put(COL_ABSP, project.getAbstractProj());
        values.put(COL_DESCP, project.getDescProj());
        values.put(COL_FUNCP, project.getFuncProj());
        values.put(COL_STATE, project.getState());
        values.put(COL_NREQ, project.getnReq());

        long id = db.insert(TB_PROJECT, null, values);
        db.close();
        return id;
    }
    public void deleteProject(int idProj) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TB_PROJECT, COL_IDP + " = ?", new String[] {String.valueOf(idProj)});
        ///////////////////
        // Delete requirements
        ///////////////////
        db.close();
    }
    public Project selectProject(int idProj) {
        Project project;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TB_PROJECT + " WHERE " + COL_IDP + " = ?", new String[] {Integer.toString(idProj)});
        if (cursor.moveToFirst()) {
            project = new Project(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), Integer.parseInt(cursor.getString(5)),
                    Integer.parseInt(cursor.getString(6)));
        } else {
            project = null;
            Log.e("error not found", "result not found or database empty");
        }
        db.close();
        return project;
    }
    public void updateProject(Project project) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAMEP, project.getNameProj());
        values.put(COL_ABSP, project.getAbstractProj());
        values.put(COL_DESCP, project.getDescProj());
        values.put(COL_FUNCP, project.getFuncProj());
        values.put(COL_STATE, project.getState());
        values.put(COL_NREQ, project.getnReq());

        db.update(TB_PROJECT, values, COL_IDP + " = ?", new String[] {String.valueOf(project.getIdProj())});
        db.close();
    }
    public ArrayList<Project> selectAllProjects() {
        ArrayList<Project> projects = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TB_PROJECT, null);
        if (cursor.moveToFirst()) {
            do {
                Project project = new Project(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), Integer.parseInt(cursor.getString(5)),
                        Integer.parseInt(cursor.getString(6)));
                projects.add(project);
            } while (cursor.moveToNext());
        }
        db.close();
        return projects;
    }

    public long insertStudy(Study study) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAMES, study.getNameStu());
        values.put(COL_FROM, study.getFromStu());
        values.put(COL_LINK, study.getLink());
        values.put(COL_NMOD, study.getnMod());

        long id = db.insert(TB_STUDY, null, values);
        db.close();
        return id;
    }
    public void deleteStudy(int idStu) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TB_STUDY, COL_IDS + " = ?", new String[] {String.valueOf(idStu)});
        db.close();
    }
    public Study selectStudy(int idStu) {
        Study study;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TB_STUDY + " WHERE " + COL_IDS + " = ?", new String[] {Integer.toString(idStu)});
        if (cursor.moveToFirst()) {
            study = new Study(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3), Integer.parseInt(cursor.getString(4)));
        } else {
            study = null;
            Log.e("error not found", "result not found or database empty");
        }
        db.close();
        return study;
    }
    public void updateStudy(Study study) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAMES, study.getNameStu());
        values.put(COL_FROM, study.getFromStu());
        values.put(COL_LINK, study.getLink());
        values.put(COL_NMOD, study.getnMod());

        db.update(TB_STUDY, values, COL_IDS + " = ?", new String[] {String.valueOf(study.getIdStu())});
        db.close();
    }
    public ArrayList<Study> selectAllStudies() {
        ArrayList<Study> studies = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TB_STUDY, null);
        if (cursor.moveToFirst()) {
            do {
                Study study = new Study(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), Integer.parseInt(cursor.getString(4)));
                studies.add(study);
            } while (cursor.moveToNext());
        }
        db.close();
        return studies;
    }

    public long insertReq(Requirement requirement) {
        String TB_REQ = "REQ" + requirement.getProjId();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PROJID, requirement.getProjId());
        values.put(COL_STUDYID, requirement.getStudyId());
        values.put(COL_REQ, requirement.getReq());

        long id = db.insert(TB_REQ, null, values);
        db.close();
        return id;
    }
    public void deleteReq(int idReq, int projId) {
        String TB_REQ = "REQ" + projId;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TB_REQ, COL_IDR + " = ?", new String[] {String.valueOf(idReq)});
        db.close();
    }
    public Requirement selectReq(int idReq, int projId) {
        String TB_REQ = "REQ" + projId;
        Requirement requirement;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TB_REQ + " WHERE " + COL_IDR + " = ?", new String[] {Integer.toString(idReq)});
        if (cursor.moveToFirst()) {
            requirement = new Requirement(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)),
                    Integer.parseInt(cursor.getString(2)), cursor.getString(3));
        } else {
            requirement = null;
            Log.e("error not found", "result not found or database empty");
        }
        db.close();
        return requirement;
    }
    public void updateReq(Requirement requirement) {
        String TB_REQ = "REQ" + requirement.getProjId();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PROJID, requirement.getProjId());
        values.put(COL_STUDYID, requirement.getStudyId());
        values.put(COL_REQ, requirement.getReq());

        db.update(TB_REQ, values, COL_IDR + " = ?", new String[] {String.valueOf(requirement.getIdReq())});
        db.close();
    }

    public long insertMod(Module module) {
        String TB_MOD = "MOD" + module.getStudyId();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_STUDYID, module.getStudyId());
        values.put(COL_TITLE, module.getTitle());
        values.put(COL_STATE, module.getState());

        long id = db.insert(TB_MOD, null, values);
        db.close();
        return id;
    }
    public void deleteMod(int idMod, int studyId) {
        String TB_MOD = "MOD" + studyId;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TB_MOD, COL_IDM + " = ?", new String[] {String.valueOf(idMod)});
        db.close();
    }
    public Module selectModule(int idMod, int studyId) {
        String TB_MOD = "MOD" + studyId;
        Module module;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TB_MOD + " WHERE " + COL_IDM + " = ?", new String[] {Integer.toString(idMod)});
        if (cursor.moveToFirst()) {
            module = new Module(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)),
                    cursor.getString(2), Integer.parseInt(cursor.getString(3)));
        } else {
            module = null;
            Log.e("error not found", "result not found or database empty");
        }
        db.close();
        return module;
    }
    public void updateMod(Module module) {
        String TB_MOD = "MOD" + module.getStudyId();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_STUDYID, module.getStudyId());
        values.put(COL_TITLE, module.getTitle());
        values.put(COL_STATE, module.getState());

        db.update(TB_MOD, values, COL_IDM + " = ?", new String[] {String.valueOf(module.getIdMod())});
        db.close();
    }

}
