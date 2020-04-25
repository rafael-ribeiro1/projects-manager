package com.rafaribeiro.projectsmanager;

public class Requirement {

    private int idReq;
    private int projId;
    private int studyId;
    private String req;

    private final int PROJID_DEFAULT = 0;
    private final int STUID_DEFAULT = 0;
    private final String REQ_DEFAULT = "";

    public Requirement() {}
    public Requirement(int idReq, int projId, int studyId, String req) {
        this.idReq = idReq;
        this.projId = projId;
        this.studyId = studyId;
        this.req = req;
    }
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
    public Requirement(int idReq) {
        this.idReq = idReq;
        this.projId = PROJID_DEFAULT;
        this.studyId = STUID_DEFAULT;
        this.req = REQ_DEFAULT;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        Requirement r = (Requirement)o;
        return this.idReq == r.idReq;
    }

}
