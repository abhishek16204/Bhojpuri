package com.example.utsav.bhojpuri;

import android.graphics.Bitmap;

class ModuleList {
    private String modulename;
    private String moduleorder;

    public String getModulename() {
        return modulename;
    }

    public void setModulename(String modulename) {
        this.modulename = modulename;
    }

    public String getModuleorder() {
        return moduleorder;
    }

    public void setModuleorder(String moduleorder) {
        this.moduleorder = moduleorder;
    }

    public Bitmap getModuleImage() {
        return moduleImage;
    }

    public void setModuleImage(Bitmap moduleImage) {
        this.moduleImage = moduleImage;
    }

    private Bitmap moduleImage;
}
