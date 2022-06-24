package cz.vsb.ekf.koj.teamtaskmanager;

public class Subtask {
    private int id;
    private boolean isComplete;
    private String title;

    public Subtask() {
    }

    public Subtask(int id, boolean isComplete, String title) {
        this.id = id;
        this.isComplete = isComplete;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
