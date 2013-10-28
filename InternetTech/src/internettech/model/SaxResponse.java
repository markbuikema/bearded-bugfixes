/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package internettech.model;

/**
 *
 * @author Christian
 */
public class SaxResponse {
    
    private SaxStatus status;
    
    private String content;
    
    public SaxResponse(SaxStatus status, String content) {
       this.status = status;
       this.content = content;
    }

    public SaxStatus getStatus() {
        return status;
    }

    public void setStatus(SaxStatus status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    public String format() {
       return status.getStatusCode() + "\t" + status.getStatusMessage() + "\n"
               + "content:" + "\t" + content;
    }
}
