package com.example.sampleschoolteacher.Model;

public class ClassJoinModel {
    private String userName, joinTime,leftTime;

    public ClassJoinModel() {
    }

    public ClassJoinModel(String userName, String joinTime, String leftTime) {
        this.userName = userName;
        this.joinTime = joinTime;
        this.leftTime = leftTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(String joinTime) {
        this.joinTime = joinTime;
    }

    public String getLeftTime() {
        return leftTime;
    }

    public void setLeftTime(String leftTime) {
        this.leftTime = leftTime;
    }
}
