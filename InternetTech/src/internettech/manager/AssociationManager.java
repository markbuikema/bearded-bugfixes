/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package internettech.manager;

import internettech.model.Association;
import internettech.model.Share;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author Christian
 */
public class AssociationManager {
	private static AssociationManager instance;
	private List<Association> asses;

	private AssociationManager() {
		asses = new ArrayList<>();
		createAssociations();
	}

	public void createAssociations() {
		asses.add(new Association("Syntaxis"));
		asses.add(new Association("Link"));
		asses.add(new Association("Watt"));

		for (Association a : asses) {
			for (int i = 0; i < 5000; i++)
				ShareManager.getInstance().storeShare(new Share(a.getId()));
		}

		System.out.println("Associations created.");
	}

	public static AssociationManager getInstance() {
		if (instance == null) {
			instance = new AssociationManager();
		}
		return instance;
	}

	public void store(Association ass) {
		asses.add(ass);
	}

	public Association retrieve(String assId) {
		for (Association ass : asses) {
			if (ass.getId().equals(assId)) {
				return ass;
			}
		}
		return null;
	}

	public List<Association> getAssociations() {
		return Collections.unmodifiableList(asses);
	}

}
