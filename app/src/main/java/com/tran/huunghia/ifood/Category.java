package com.tran.huunghia.ifood;

/**
 * Created by Thanh Hung on 5/15/2018.
 */

public class Category{
    private int idCategory;
    private String strCategory;
    private String strCategoryDescription;
    private String strCategoryThumb;

    public Category(int idCategory, String strCategory, String strCategoryDescription, String strCategoryThumb) {
        this.idCategory = idCategory;
        this.strCategory = strCategory;
        this.strCategoryDescription = strCategoryDescription;
        this.strCategoryThumb = strCategoryThumb;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public String getStrCategory() {
        return strCategory;
    }

    public void setStrCategory(String strCategory) {
        this.strCategory = strCategory;
    }

    public String getStrCategoryDescription() {
        return strCategoryDescription;
    }

    public void setStrCategoryDescription(String strCategoryDescription) {
        this.strCategoryDescription = strCategoryDescription;
    }

    public String getStrCategoryThumb() {
        return strCategoryThumb;
    }

    public void setStrCategoryThumb(String strCategoryThumb) {
        this.strCategoryThumb = strCategoryThumb;
    }
}
