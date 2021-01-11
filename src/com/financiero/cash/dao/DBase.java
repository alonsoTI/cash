/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.financiero.cash.dao;

import java.util.ResourceBundle;


/**
 *
 * @author sistemas
 */
public abstract class DBase {

    protected String url;
    protected String usuario;
    protected String password;

    public DBase() {

        ResourceBundle bundle = ResourceBundle.getBundle("com.financiero.cash.dao.database");
        url = bundle.getString("url").trim();
        usuario = bundle.getString("usuario").trim();
        password = bundle.getString("password").trim();

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
             //Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            ////System.out.println("Failed to get connection");
            e.printStackTrace();
        }

    }
}
