package org.polarsys.capella.vp.midas_prototype.design.service;

//import java.io.IOException;
//import java.io.OutputStream;
//import java.io.PrintWriter;
//import java.util.ArrayList;
import java.util.Collection;
//import java.util.Collections;
import java.util.HashSet;
//import java.util.Iterator;
import java.util.List;
//import java.util.Set;
//import java.util.stream.Collector;
import java.util.stream.Collectors;

//import org.eclipse.core.resources.IFile;
//import org.eclipse.core.resources.ResourcesPlugin;
//import org.eclipse.core.runtime.Path;
//import org.eclipse.core.runtime.Platform;
//import org.eclipse.emf.common.EMFPlugin;
//import org.eclipse.emf.common.command.Command;
//import org.eclipse.emf.common.util.Diagnostic;
//import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
//import org.eclipse.emf.common.util.URI;
//import org.eclipse.emf.ecore.EAnnotation;
//import org.eclipse.emf.ecore.EAttribute;
//import org.eclipse.emf.ecore.EClass;
/*import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.ENamedElement;*/
import org.eclipse.emf.ecore.EObject;
/*import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.EMFEditPlugin;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;*/
import org.eclipse.sirius.business.api.dialect.DialectManager;
//import org.eclipse.sirius.business.api.query.EObjectQuery;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
/*import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.DEdge;
import org.eclipse.sirius.diagram.DNodeContainer;
import org.eclipse.sirius.diagram.DNodeList;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.EdgeArrows;
import org.eclipse.sirius.diagram.EdgeTarget;
import org.eclipse.sirius.diagram.business.api.query.DDiagramQuery;*/
//import org.eclipse.sirius.diagram.business.internal.helper.task.operations.CreateViewTask;
//import org.eclipse.sirius.diagram.business.internal.metamodel.helper.ContainerMappingHelper;
//import org.eclipse.sirius.diagram.business.internal.metamodel.helper.ContentHelper;
//import org.eclipse.sirius.diagram.business.internal.metamodel.spec.DSemanticDiagramSpec;
//import org.eclipse.sirius.diagram.business.internal.query.DDiagramInternalQuery;
//import org.eclipse.sirius.diagram.description.AbstractNodeMapping;
//import org.eclipse.sirius.diagram.description.DiagramElementMapping;
//import org.eclipse.sirius.diagram.description.tool.CreateView;
//import org.eclipse.sirius.diagram.description.tool.ToolFactory;
//import org.eclipse.sirius.ecore.extender.business.api.accessor.ModelAccessor;
//import org.eclipse.sirius.ecore.extender.business.api.accessor.exception.FeatureNotFoundException;
//import org.eclipse.sirius.ecore.extender.business.api.accessor.exception.MetaClassNotFoundException;
//import org.eclipse.sirius.tools.api.command.CommandContext;
import org.eclipse.sirius.viewpoint.DRepresentation;
//import org.eclipse.sirius.viewpoint.DSemanticDecorator;
//import org.eclipse.sirius.viewpoint.FontFormat;
//import org.eclipse.sirius.viewpoint.ViewpointPackage;
import org.eclipse.emf.ecoretools.design.service.EReferenceServices;
//import org.polarsys.capella.vp.midas_prototype.midas_prototype.Midas_prototypePackage;
import org.polarsys.capella.vp.midas_prototype.midas_prototype.OPM;
import org.polarsys.capella.vp.midas_prototype.midas_prototype.OpmEntity;
//import org.polarsys.capella.vp.midas_prototype.midas_prototype.OpmObject;
//import org.polarsys.capella.vp.midas_prototype.midas_prototype.OpmProcess;
import org.polarsys.capella.vp.midas_prototype.midas_prototype.OpmRelation;
//import org.polarsys.capella.vp.midas_prototype.midas_prototype.OpmState;
import org.polarsys.capella.vp.midas_prototype.midas_prototype.OpmStructuralRelation;
import org.polarsys.capella.vp.midas_prototype.midas_prototype.OpmThing;


public class DesignServices extends EReferenceServices {
	/**
	 * The union of: 1) Descendants (direct and indirect), collected through eAllContents, </br>
	 * 				 2) Parents (up to but not including the OPM), through eContainer, </br>
 	 * 				 3) Direct children of each parent, through eContents
 	 * <p>
 	 * This includes the object passed as the argument
 	 * </p>
	 * @param any A semantic element
	 * @return Descendants, parents, and children of parents
	 */
	public Collection<EObject> eAllContentsAndContainerContents(EObject any) {
		Collection<EObject> result = new HashSet<EObject>();
		
		TreeIterator<EObject> descendants = any.eAllContents();
		while (descendants.hasNext()) {
			result.add((EObject) descendants.next());
		}
		
		// Only 1 OPM as the root. Collecting all direct contents of the containers, including the OPM contents.
		EObject currContainer = any.eContainer();
		while (true) {
			result.addAll(currContainer.eContents());
			if (currContainer instanceof OPM) break;
			currContainer = currContainer.eContainer();
		}
		
		// Filtering out stuff like OpmState and OpmReference
		result = result.stream().filter(x -> x instanceof OpmThing).collect(Collectors.toSet());
		return result;
	}
	
	/**
	 * @param any A semantic element
	 * @return The direct children of the OPM
	 */
	public Collection<EObject> OPMContents(EObject any) {
		EObject opm = any.eContainer();
		while (true) {
			if (opm instanceof OPM) break;
			opm = opm.eContainer();
		}
		return opm.eContents();
	}
	
	/**
	 * Getting the OpmObjects and OpmProcesses directly connected to an OpmEntity. If an OpmState is
	 * connected to the entity, then the container of the OpmState will be returned instead.
	 * 
	 * @param any
	 * @return The OpmThings directly connected to the OpmEntity
	 */
	public Collection<EObject> getConnectedOpmThings(OpmEntity any) {
		Collection<EObject> result = new HashSet<EObject>();
		
//		EList<OpmRelation> references = any.getReferences();
//		for (OpmRelation r : references) {
//			OpmEntity candidate = r.getTo();
//			if (candidate instanceof OpmObject || candidate instanceof OpmProcess) {
//				result.add(candidate);
//			}
//			else if (candidate instanceof OpmState) {
//				result.add(candidate.eContainer());
//			}
//		}
//		
//		Collection<EObject> inverses = new EObjectQuery(any).getInverseReferences(Midas_prototypePackage.Literals.OPM_REFERENCE__TO);
//		for (EObject r : inverses) {
//			OpmEntity candidate = ((OpmReference)r).getFrom();
//			if (candidate instanceof OpmObject || candidate instanceof OpmProcess) {
//				result.add(candidate);
//			}
//			else if (candidate instanceof OpmState) {
//				result.add(candidate.eContainer());
//			}
//		}
		
		return result;
	}
	
	public EObject getToObject(EObject ref) {
//		if (ref instanceof OpmReference) {
//			return ((OpmReference)ref).getTo();
//		}
		return null;
	}
	
	public Collection<EObject> getChildReferences(EObject obj) {
		Collection<EObject> result = new HashSet<EObject>();
		
		OpmThing thing;
		if (obj instanceof OpmThing)
			thing = (OpmThing)obj;
		else 
			return null;
		
		List<OpmThing> composables = thing.getComposables();
		for (OpmThing c : composables) {
			List<OpmRelation> relations = c.getReferences();
			for (OpmRelation r : relations) {
				if (r instanceof OpmStructuralRelation)
					result.add(((OpmStructuralRelation)r).getTo());
			}
		}
		
		return result;
	}
	
	public EObject something(EObject newContainerView) {
		Session session = SessionManager.INSTANCE.getSession(newContainerView);
		Collection<DRepresentation> reps = DialectManager.INSTANCE.getLoadedRepresentations(newContainerView, session);
		return null;
	}
	
	public EObject moveTest(EObject element) {
		return null;
	}
}
