package cz.vsb.ekf.koj.teamtaskmanager;

public enum Message {
    NONE("", "#000000"),
    NAME_EXISTS("Uživatelské jméno již existuje, prosím vyberte jiné.", ""),
    PASSWORD_INCORRECT("Zadali jste nesprávné heslo", ""),
    NAME_NOT_EXIST("Uživatelské jméno neexistuje", "#D4AC0D"),
    MISS_USERNAME("Zadejte prosím uživatelské jméno", "#F300FF"),
    MISS_PASSWORD("Zadejte prosím heslo.", "#F300FF"),
    MISS_INPUT("Vyplňte prosím všechny údaje", ""),
    MISS_TEAM_NAME("Vyplňte prosím název týmu", ""),
    ACCOUNT_CREATED("Byl vytvořen nový účet. Můžete se přihlásit.", "#1AAE00"),
    USER_LOGOUT("Uživatel úspěšně odhlášen.", "#686868"),
    SESSION_TIMEOUT("Čas vypršel, prosím přihlašte se znovu", ""),
    ERROR_UNKNOWN("Vyskytla se chyba", ""),
    CANNOT_VIEW_TASK("Tento úkol byl smazán, nebo nemáte práva na jeho zobrazení", ""),
    CANNOT_VIEW_TEAM("Tento tým byl smazán, nebo nemáte práva na jeho zobrazení", ""),
    MISS_TASK_TITLE("Vyplňte prosím název úkolu", ""),
    MEMBER_NOT_FOUND("Uživatel se zadanými údaji neexistuje",""),
    MEM_NOT_FOUND_TEAM("Uživatel se zadanými údaji neexistuje nebo je již součástí týmu",""),
    EMAIL_INCONCLUSIVE("Zadaný email není vhodný pro vyhledání uživatele. Vyplňte ID hledaného uživatele",""),
    USER_CANNOT_BE_DELETED("Váš profil nelze vymyzat, v některém z týmů jste jediným správcem.", ""),
    USER_CANNOT_LEAVE("Nemůžete opustit tým, jste jediným správcem.", ""),
    CANNOT_LEAVE_TEAM("Nemůžete opustit tým, jste jediným správcem", ""),
    USER_DELETED("Váš profil byl úspěšně vymazán.", ""),
    CANNOT_ASSIGN("Úkol vám nemůže být přidělen", ""),
    TASK_ASSIGNED("Úkol byl úspěšně přidělen.", ""),
    TASK_DELETED("Úkol byl vymazán","");
    
    private final String description;
    private final String color;

    private Message(String description, String color) {
        this.description = description;
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public String getColor() {
        return color;
    }
        
}
