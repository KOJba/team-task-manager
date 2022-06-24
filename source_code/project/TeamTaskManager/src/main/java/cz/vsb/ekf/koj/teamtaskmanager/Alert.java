package cz.vsb.ekf.koj.teamtaskmanager;

public class Alert {
    private int id;
    private int idTeam;
    private AlertType type;

    public Alert() {
    }

    public Alert(int id, int idTeam, AlertType type) {
        this.id = id;
        this.idTeam = idTeam;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(int idTeam) {
        this.idTeam = idTeam;
    }

    public AlertType getType() {
        return type;
    }

    public void setType(AlertType type) {
        this.type = type;
    }

    public String getColor() {
        return type.getColor();
    }
    
    public int getPriority() {
        return type.getPriority();
    }
    
    public String getText(){
        return type.getText();
    }
}
