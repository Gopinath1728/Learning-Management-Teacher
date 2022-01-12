package com.example.sampleschoolteacher.Model;

public class TeachingClassModel {
    private String className,subjectName;

    public TeachingClassModel() {
    }

    public TeachingClassModel(String className, String subjectName) {
        this.className = className;
        this.subjectName = subjectName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
