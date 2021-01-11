/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.forms;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;
/**
 *
 * @author jmoreno
 */
public class FileUploadForm extends ActionForm{
    private FormFile myFile;

    public FormFile getMyFile() {
        return myFile;
    }

    public void setMyFile(FormFile myFile) {
        this.myFile = myFile;
    }

}
