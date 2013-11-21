/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package internettech.protocol;

/**
 *
 * @author Christian
 */
public class SaxResponse {
    
    private SaxStatus status;
    
    private String content;
    
    public SaxResponse(SaxStatus status) {
       this.status = status;
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
    
    public String toString() {
       String responseString = status.getStatusCode() + " " 
               + status.getStatusMessage();
       
       if(content != null) {
           responseString += " " + "content: " + content;
       }
       
       return responseString;
    }
}
