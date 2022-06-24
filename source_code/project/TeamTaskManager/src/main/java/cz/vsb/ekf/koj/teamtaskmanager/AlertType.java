package cz.vsb.ekf.koj.teamtaskmanager;

public enum AlertType {
    ADDED_TO_TEAM(1,"#008000", 1, "Byl/a jste přidán/a do nového týmu."),
    TASK_COMPLETED(2,"#008000", 1, "Byl splněn úkol v týmu"),
    NEW_TASK(3,"#FFA500", 2, "V týmu přibyl nový úkol."),
    RIGHTS_CHANGE(4,"#FFA500", 2, "Byla vám změněna práva v týmu"),
    REMOVED_FROM_TEAM(5,"#FF0000", 3, "Byl/a jste odstraněn/a z týmu."),
    TASK_REMOVED(6,"#FF0000", 3, "Byl Vám odebrán úkol."),
    NEW_ASSIGNMENT(7,"#FF0000", 3, "Byl Vám přidělen nový úkol");
    
    private final int id;
    private final String color;
    private final int priority;
    private final String text;

    private AlertType(int id,String color, int priority, String text) {
        this.id = id;
        this.color = color;
        this.priority = priority;
        this.text = text;
    }

    public int getId() {
        return id;
    }
    
    public String getColor() {
        return color;
    }

    public int getPriority() {
        return priority;
    }

    public String getText() {
        return text;
    }
   
    
}
