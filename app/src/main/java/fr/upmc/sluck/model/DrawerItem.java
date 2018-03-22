package fr.upmc.sluck.model;

/**
 * Created by labib on 21/03/2018.
 */
public class DrawerItem {
    private int icon;
    private String title;



    public DrawerItem(String title, int icon) {
        this.icon = icon;
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}