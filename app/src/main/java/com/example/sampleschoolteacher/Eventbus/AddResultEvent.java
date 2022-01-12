package com.example.sampleschoolteacher.Eventbus;

public class AddResultEvent {
    private String studentUid,maxMarks,marksObtained;

    public AddResultEvent(String studentUid, String maxMarks, String marksObtained) {
        this.studentUid = studentUid;
        this.maxMarks = maxMarks;
        this.marksObtained = marksObtained;
    }

    public String getStudentUid() {
        return studentUid;
    }

    public void setStudentUid(String studentUid) {
        this.studentUid = studentUid;
    }

    public String getMaxMarks() {
        return maxMarks;
    }

    public void setMaxMarks(String maxMarks) {
        this.maxMarks = maxMarks;
    }

    public String getMarksObtained() {
        return marksObtained;
    }

    public void setMarksObtained(String marksObtained) {
        this.marksObtained = marksObtained;
    }
}
