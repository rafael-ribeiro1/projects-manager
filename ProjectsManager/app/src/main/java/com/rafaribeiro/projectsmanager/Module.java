package com.rafaribeiro.projectsmanager;

public class Module implements State {

    private int idMod;
    private int studyId;
    private String title;
    private int state;

    public Module() {}
    public Module(int idMod, int studyId, String title, int state) {
        this.idMod = idMod;
        this.studyId = studyId;
        this.title = title;
        this.state = state;
    }
    public Module(int studyId, String title, int state) {
        this.studyId = studyId;
        this.title = title;
        this.state = state;
    }

    public int getIdMod() {
        return idMod;
    }
    public void setIdMod(int idMod) {
        this.idMod = idMod;
    }
    public int getStudyId() {
        return studyId;
    }
    public void setStudyId(int studyId) {
        this.studyId = studyId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }
}
