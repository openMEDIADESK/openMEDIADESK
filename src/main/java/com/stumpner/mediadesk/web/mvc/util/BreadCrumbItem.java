package com.stumpner.mediadesk.web.mvc.util;

import com.stumpner.mediadesk.image.category.Folder;

/**
 * Created by IntelliJ IDEA.
 * User: franz.stumpner
 * Date: 23.07.2008
 * Time: 19:41:10
 * To change this template use File | Settings | File Templates.
 */
public class BreadCrumbItem {

    private String url = "";
    private int id = 0;
    private String title = "";
    private boolean showFolder = false;

    public BreadCrumbItem(Object obj) {
        if (obj instanceof Folder) {
            Folder folder = (Folder)obj;
            url = "/index/cat";
            id = folder.getCategoryId();
            title = folder.getCatTitle();
            showFolder = true;
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isShowFolder() {
        return showFolder;
    }

    public void setShowFolder(boolean showFolder) {
        this.showFolder = showFolder;
    }
}
