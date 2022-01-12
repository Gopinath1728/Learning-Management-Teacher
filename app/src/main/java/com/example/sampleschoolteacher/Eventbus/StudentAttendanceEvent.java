package com.example.sampleschoolteacher.Eventbus;

public class StudentAttendanceEvent {
    private String studentUid,status;

    public StudentAttendanceEvent(String studentUid, String status) {
        this.studentUid = studentUid;
        this.status = status;
    }

    public String getStudentUid() {
        return studentUid;
    }

    public void setStudentUid(String studentUid) {
        this.studentUid = studentUid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
