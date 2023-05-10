package org.polarsys.capella.vp.midas_prototype.ui.menu.handlers;

import java.util.Collection;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.sirius.business.api.dialect.DialectManager;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.diagram.ui.edit.api.part.IDiagramElementEditPart;
import org.eclipse.sirius.viewpoint.description.RepresentationDescription;
import org.eclipse.sirius.viewpoint.description.Viewpoint;
import org.polarsys.capella.core.sirius.ui.actions.NewRepresentationAction;

import org.polarsys.capella.vp.midas_prototype.midas_prototype.OpmThing;

// Simplification of: org.polarsys.capella.core.platform.sirius.ui.navigator.actions.provider.NewRepresentationActionProvider.java
// Also see: https://wiki.eclipse.org/Capella/Tutorials/Extensibility/Add_Menu
public class CreateOPDDiagramEditorHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		EObject target = getSelectedEObject(event);
		if (target instanceof OpmThing) {
			createDiagram((OpmThing) target);
		}
		return null;
	}
	
	private EObject getSelectedEObject(ExecutionEvent event) {
		EObject result = null;
		
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		Object selectedElement = ((IStructuredSelection) selection).getFirstElement();
		
		if (selectedElement instanceof IDiagramElementEditPart) {
			result = ((IDiagramElementEditPart) selectedElement).resolveTargetSemanticElement();
		}
		return result;
	}
	
	private void createDiagram(OpmThing target) {
		Session session = SessionManager.INSTANCE.getSession(target);
		Collection<Viewpoint> selectedViewpoints = session.getSelectedViewpoints(false);
        Collection<RepresentationDescription> availableDescriptions = DialectManager.INSTANCE.getAvailableRepresentationDescriptions(selectedViewpoints, target);
        
        RepresentationDescription description = null;
        for (RepresentationDescription d: availableDescriptions) {
        	if (d.getName().equals("OPD")) {
        		description = d;
        	}
        }
        
        if (description != null) {
            NewRepresentationAction action = new NewRepresentationAction(description, target, session);
            action.run();
        }
	}
}
