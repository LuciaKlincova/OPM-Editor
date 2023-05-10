package org.polarsys.capella.vp.midas_prototype.design.service;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecoretools.design.service.EReferenceServices;
import org.eclipse.sirius.business.api.dialect.DialectManager;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DSemanticDiagram;
//import org.eclipse.sirius.diagram.business.internal.metamodel.spec.DSemanticDiagramSpec;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.polarsys.capella.vp.midas_prototype.midas_prototype.OpmObject;
import org.polarsys.capella.vp.midas_prototype.midas_prototype.OpmProcess;
import org.polarsys.capella.vp.midas_prototype.midas_prototype.OpmRelation;
import org.polarsys.capella.vp.midas_prototype.midas_prototype.OpmState;
import org.polarsys.capella.vp.midas_prototype.midas_prototype.OpmThing;
import org.polarsys.capella.vp.midas_prototype.midas_prototype.AffiliationType;
import org.polarsys.capella.vp.midas_prototype.midas_prototype.EssenceType;

public class ConditionalServices extends EReferenceServices {
	/**
	 * @param diag The current diagram
	 * @param any A semantic element
	 * @return true if the EObject any is the target of the current diagram
	 */
	public boolean isTargetOfDiagram(DDiagram diag, EObject any) {
		return any.equals(((DSemanticDiagram)diag).getTarget());
	}
	
	/**
	 * Checks to see if the input view is also the diagram. Generally used when the Sirius interpreter does not provide the 
	 * "diagram" variable directly. If it's provided, it would be more efficient to do view == diagram instead.
	 * 
	 * Technically, this only checks if the view is any one diagram representation. However, in context of the Sirius editor, the 
	 * only selectable view that is a diagram would be the current diagram in the editor.
	 * 
	 * @param view (should be a DSemanticDiagram to work)
	 * @return true if the view is the diagram
	 */
	public boolean isViewTheDiagram(EObject view) {
		if (!(view instanceof DSemanticDiagram))
			return false;
		
		Session session = SessionManager.INSTANCE.getSession(view);
		EObject semantic = ((DSemanticDiagram)view).getTarget();
		Collection<DRepresentation> reps = DialectManager.INSTANCE.getLoadedRepresentations(semantic, session);
		
		for (DRepresentation r : reps) {
			if (r == view) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isOpmThing(EObject any) {
		return any instanceof OpmThing;
	}
	
	public boolean isOpmObject(EObject any) {
		return any instanceof OpmObject;
	}
	
	public boolean isOpmProcess(EObject any) {
		return any instanceof OpmProcess;
	}
	
	public boolean isOpmState(EObject any) {
		return any instanceof OpmState;
	}
	
	public boolean isOpmRelation(EObject any) {
		return any instanceof OpmRelation;
	}
	
	public boolean isEnvironmentalAndInformatical(EObject any) {
		OpmThing x = (OpmThing) any;
		return x.getAffiliation() == AffiliationType.ENVIRONMENTAL && x.getEssence() == EssenceType.INFORMATIONAL;
	}
	
	public boolean isEnvironmentalAndPhysical(EObject any) {
		OpmThing x = (OpmThing) any;
		return x.getAffiliation() == AffiliationType.ENVIRONMENTAL && x.getEssence() == EssenceType.PHYSICAL;
	}
	
	public boolean isSystemocAndPhysical(EObject any) {
		OpmThing x = (OpmThing) any;
		return x.getAffiliation() == AffiliationType.SYSTEMIC && x.getEssence() == EssenceType.PHYSICAL;
	}
}
