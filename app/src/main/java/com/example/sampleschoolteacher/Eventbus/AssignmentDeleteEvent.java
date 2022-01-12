package com.example.sampleschoolteacher.Eventbus;

import com.example.sampleschoolteacher.Model.AssignmentModel;

public class AssignmentDeleteEvent {
    private AssignmentModel assignmentModel;
    private String topic;
    private int position;

    public AssignmentDeleteEvent(AssignmentModel assignmentModel, String topic, int position) {
        this.assignmentModel = assignmentModel;
        this.topic = topic;
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public AssignmentModel getAssignmentModel() {
        return assignmentModel;
    }

    public void setAssignmentModel(AssignmentModel assignmentModel) {
        this.assignmentModel = assignmentModel;
    }
}
