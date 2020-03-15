package com.rafaribeiro.projectsmanager;

public class Requirement {

    private int idReq;
    private int projId;
    private int studyId;
    private String req;

    private final int STUID_DEFAULT = 0;
    private final String REQ_DEFAULT = "";

    public Requirement() {}
    public Requirement(int idReq, int projId, int studyId) {
        this.idReq = idReq;
        this.projId = projId;
        this.studyId = studyId;
        this.req = REQ_DEFAULT;
    }
    public Requirement(int idReq, int projId, String req) {
        this.idReq = idReq;
        this.projId = projId;
        this.studyId = STUID_DEFAULT;
        this.req = req;
    }
    public Requirement(int projId, int studyId) {
        this.projId = projId;
        this.studyId = studyId;
        this.req = REQ_DEFAULT;
    }
    public Requirement(int projId, String req) {
        this.projId = projId;
        this.studyId = STUID_DEFAULT;
        this.req = req;
    }

    public int getIdReq() {
        return idReq;
    }
    public void setIdReq(int idReq) {
        this.idReq = idReq;
    }
    public int getProjId() {
        return projId;
    }
    public void setProjId(int projId) {
        this.projId = projId;
    }
    public int getStudyId() {
        return studyId;
    }
    public void setStudyId(int studyId) {
        this.studyId = studyId;
    }
    public String getReq() {
        return req;
    }
    public void setReq(String req) {
        this.req = req;
    }
}
