/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package internettech.manager;

import internettech.model.Association;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Christian
 */
public class AssManager {
    private static AssManager instance;
    private List<Association> asses;
    
    private AssManager() {
        asses = new ArrayList<>();
    }
    
    public static AssManager getInstance() {
        if (instance == null) {
            instance = new AssManager();
        }
        return instance;
    }
    
    public void store(Association ass) {
        asses.add(ass);
    }
    
    public Association retrieve(String assId) {
        for (Association ass : asses) {
            if(ass.getId().equals(assId)) {
                return ass;
            }
        }
        return null;
    }
    
    
}
