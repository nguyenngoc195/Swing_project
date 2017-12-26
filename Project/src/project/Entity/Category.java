/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.Entity;

import java.util.HashMap;

/**
 *
 * @author Lan
 */
public class Category {

    private String idCate;

    private String category;

    private String describe;

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getIDCate() {
        return idCate;
    }

    public void setIDCate(String IDCate) {
        this.idCate = IDCate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public HashMap<String, String> validate() {
        HashMap<String, String> errors = new HashMap<>();

        if (this.idCate == null || this.idCate.length() == 0) {
            errors.put("idCate", "Category's ID cannot be empty");
        }
        if (this.category == null || this.category.length() == 0) {
            errors.put("urlSource", "category's cannot be empty");
        }
        if (this.describe == null || this.describe.length() == 0) {
            errors.put("describe", "describe cannot be empty");
        }
        return errors;
    }

}
