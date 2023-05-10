package org.polarsys.capella.vp.midas_prototype.design.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecoretools.design.service.EReferenceServices;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.business.api.componentization.DiagramMappingsManager;
import org.eclipse.sirius.diagram.business.api.componentization.DiagramMappingsManagerRegistry;
import org.eclipse.sirius.diagram.description.ContainerMapping;
import org.eclipse.sirius.diagram.tools.api.command.view.CreateDDiagramElementCommand;
import org.polarsys.capella.vp.midas_prototype.midas_prototype.Midas_prototypeFactory;
import org.polarsys.capella.vp.midas_prototype.midas_prototype.OPM;
import org.polarsys.capella.vp.midas_prototype.midas_prototype.OpmConsumeResultRelation;
import org.polarsys.capella.vp.midas_prototype.midas_prototype.OpmEntity;
import org.polarsys.capella.vp.midas_prototype.midas_prototype.OpmEffectLink;
import org.polarsys.capella.vp.midas_prototype.midas_prototype.OpmInputRelation;
import org.polarsys.capella.vp.midas_prototype.midas_prototype.OpmObject;
import org.polarsys.capella.vp.midas_prototype.midas_prototype.OpmOutputRelation;
import org.polarsys.capella.vp.midas_prototype.midas_prototype.OpmAgentLink;
import org.polarsys.capella.vp.midas_prototype.midas_prototype.OpmUndirectionalLink;
import org.polarsys.capella.vp.midas_prototype.midas_prototype.OpmBidirectionalLink;
import org.polarsys.capella.vp.midas_prototype.midas_prototype.OpmReciprocalLink;
import org.polarsys.capella.vp.midas_prototype.midas_prototype.OpmInstrumentLink;
import org.polarsys.capella.vp.midas_prototype.midas_prototype.OpmAggregationLink;
import org.polarsys.capella.vp.midas_prototype.midas_prototype.OpmExhibitionLink;
import org.polarsys.capella.vp.midas_prototype.midas_prototype.OpmGeneralizationLink;
import org.polarsys.capella.vp.midas_prototype.midas_prototype.OpmClassificationLink;
import org.polarsys.capella.vp.midas_prototype.midas_prototype.OpmProcess;
import org.polarsys.capella.vp.midas_prototype.midas_prototype.OpmRelation;
import org.polarsys.capella.vp.midas_prototype.midas_prototype.OpmState;
import org.polarsys.capella.vp.midas_prototype.midas_prototype.OpmStructuralRelation;
import org.polarsys.capella.vp.midas_prototype.midas_prototype.OpmThing;

public class CreationServices extends EReferenceServices{

	/**
	 * Used by edge creation tool to create a transformation relation depending on the types of the src and dst.
	 * @param src
	 * @param dst
	 * @return
	 */
	public EObject createTransformationRelation(EObject src, EObject dst) {
		// Create the relation and its end points
		OpmRelation relation;
		if (src instanceof OpmState && dst instanceof OpmProcess) 
		{
			relation = Midas_prototypeFactory.eINSTANCE.createOpmInputRelation();
			((OpmInputRelation)relation).setFrom((OpmState)src);
			((OpmInputRelation)relation).setTo((OpmProcess)dst);
		}
		else if (src instanceof OpmProcess && dst instanceof OpmState) 
		{
			relation = Midas_prototypeFactory.eINSTANCE.createOpmOutputRelation();
			((OpmOutputRelation)relation).setFrom((OpmProcess)src);
			((OpmOutputRelation)relation).setTo((OpmState)dst);
		}
		else if ((src instanceof OpmObject && dst instanceof OpmProcess) ||
				(src instanceof OpmProcess && dst instanceof OpmObject)) 
		{
			relation = Midas_prototypeFactory.eINSTANCE.createOpmConsumeResultRelation();
			((OpmConsumeResultRelation)relation).setFrom((OpmThing)src);
			((OpmConsumeResultRelation)relation).setTo((OpmThing)dst);
		}
		else return null;
		
		// Add the relation to the src object because it must be contained in something
		((OpmEntity)src).getReferences().add(relation);
		
		return relation;
	}
	
	public EObject createEffectLink(EObject src, EObject dst) {
		// Create the relation and its end points
		OpmRelation relation;
		if ((src instanceof OpmObject && dst instanceof OpmProcess) ||
				(src instanceof OpmProcess && dst instanceof OpmObject)) 
		{
			relation = Midas_prototypeFactory.eINSTANCE.createOpmEffectLink();
			((OpmEffectLink)relation).setFrom((OpmThing)src);
			((OpmEffectLink)relation).setTo((OpmThing)dst);
		}
		else return null;
		
		// Add the relation to the src object because it must be contained in something
		((OpmEntity)src).getReferences().add(relation);
		
		return relation;
	}
	
	public EObject createUndirectionalLink(EObject src, EObject dst) {
		// Create the relation and its end points
		OpmRelation relation;
		if ((src instanceof OpmObject && dst instanceof OpmObject) ||
				(src instanceof OpmProcess && dst instanceof OpmProcess)) 
		{
			relation = Midas_prototypeFactory.eINSTANCE.createOpmUndirectionalLink();
			((OpmUndirectionalLink)relation).setFrom((OpmThing)src);
			((OpmUndirectionalLink)relation).setTo((OpmThing)dst);
		}
		else return null;
		
		// Add the relation to the src object because it must be contained in something
		((OpmEntity)src).getReferences().add(relation);
		
		return relation;
	}
	
	public EObject createBidirectionalLink(EObject src, EObject dst) {
		// Create the relation and its end points
		OpmRelation relation;
		if ((src instanceof OpmObject && dst instanceof OpmObject) ||
				(src instanceof OpmProcess && dst instanceof OpmProcess)) 
		{
			relation = Midas_prototypeFactory.eINSTANCE.createOpmBidirectionalLink();
			((OpmBidirectionalLink)relation).setFrom((OpmThing)src);
			((OpmBidirectionalLink)relation).setTo((OpmThing)dst);
		}
		else return null;
		
		// Add the relation to the src object because it must be contained in something
		((OpmEntity)src).getReferences().add(relation);
		
		return relation;
	}
	
	public EObject createReciprocalLink(EObject src, EObject dst) {
		// Create the relation and its end points
		OpmRelation relation;
		if ((src instanceof OpmObject && dst instanceof OpmObject) ||
				(src instanceof OpmProcess && dst instanceof OpmProcess)) 
		{
			relation = Midas_prototypeFactory.eINSTANCE.createOpmReciprocalLink();
			((OpmReciprocalLink)relation).setFrom((OpmThing)src);
			((OpmReciprocalLink)relation).setTo((OpmThing)dst);
		}
		else return null;
		
		// Add the relation to the src object because it must be contained in something
		((OpmEntity)src).getReferences().add(relation);
		
		return relation;
	}
	
	public EObject createAggregationLink(EObject src, EObject dst) {
		// Create the relation and its end points
		OpmRelation relation;
		if ((src instanceof OpmObject && dst instanceof OpmObject) ||
				(src instanceof OpmProcess && dst instanceof OpmProcess)) 
		{
			relation = Midas_prototypeFactory.eINSTANCE.createOpmAggregationLink();
			((OpmAggregationLink)relation).setFrom((OpmThing)src);
			((OpmAggregationLink)relation).setTo((OpmThing)dst);
		}
		else return null;
		
		// Add the relation to the src object because it must be contained in something
		((OpmEntity)src).getReferences().add(relation);
		
		return relation;
	}
	
	public EObject createExhibitionLink(EObject src, EObject dst) {
		// Create the relation and its end points
		OpmRelation relation;
		if ((src instanceof OpmObject && dst instanceof OpmObject) ||
				(src instanceof OpmProcess && dst instanceof OpmProcess)) 
		{
			relation = Midas_prototypeFactory.eINSTANCE.createOpmExhibitionLink();
			((OpmExhibitionLink)relation).setFrom((OpmThing)src);
			((OpmExhibitionLink)relation).setTo((OpmThing)dst);
		}
		else return null;
		
		// Add the relation to the src object because it must be contained in something
		((OpmEntity)src).getReferences().add(relation);
		
		return relation;
	}
	
	public EObject createGeneralizationLink(EObject src, EObject dst) {
		// Create the relation and its end points
		OpmRelation relation;
		if ((src instanceof OpmObject && dst instanceof OpmObject) ||
				(src instanceof OpmProcess && dst instanceof OpmProcess)) 
		{
			relation = Midas_prototypeFactory.eINSTANCE.createOpmGeneralizationLink();
			((OpmGeneralizationLink)relation).setFrom((OpmThing)src);
			((OpmGeneralizationLink)relation).setTo((OpmThing)dst);
		}
		else return null;
		
		// Add the relation to the src object because it must be contained in something
		((OpmEntity)src).getReferences().add(relation);
		
		return relation;
	}
	
	public EObject createClassificationLink(EObject src, EObject dst) {
		// Create the relation and its end points
		OpmRelation relation;
		if ((src instanceof OpmObject && dst instanceof OpmObject) ||
				(src instanceof OpmProcess && dst instanceof OpmProcess)) 
		{
			relation = Midas_prototypeFactory.eINSTANCE.createOpmClassificationLink();
			((OpmClassificationLink)relation).setFrom((OpmThing)src);
			((OpmClassificationLink)relation).setTo((OpmThing)dst);
		}
		else return null;
		
		// Add the relation to the src object because it must be contained in something
		((OpmEntity)src).getReferences().add(relation);
		
		return relation;
	}
	
	public EObject createAgentLink(EObject src, EObject dst) {
		// Create the relation and its end points
		OpmRelation relation;
		if (src instanceof OpmObject && dst instanceof OpmProcess) 
		{
			relation = Midas_prototypeFactory.eINSTANCE.createOpmAgentLink();
			((OpmAgentLink)relation).setFrom((OpmObject)src);
			((OpmAgentLink)relation).setTo((OpmProcess)dst);
		}
		else return null;
		
		// Add the relation to the src object because it must be contained in something
		((OpmEntity)src).getReferences().add(relation);
		
		return relation;
	}
	
	public EObject createInstrumentLink(EObject src, EObject dst) {
		// Create the relation and its end points
		OpmRelation relation;
		if (src instanceof OpmObject && dst instanceof OpmProcess) 
		{
			relation = Midas_prototypeFactory.eINSTANCE.createOpmInstrumentLink();
			((OpmInstrumentLink)relation).setFrom((OpmObject)src);
			((OpmInstrumentLink)relation).setTo((OpmProcess)dst);
		}
		else return null;
		
		// Add the relation to the src object because it must be contained in something
		((OpmEntity)src).getReferences().add(relation);
		
		return relation;
	}
	
	public EObject createStructuralRelation(EObject src, EObject dst) {
		// Create the relation and its end points
		OpmRelation relation;
		if (src instanceof OpmProcess && dst instanceof OpmProcess) 
		{
			relation = Midas_prototypeFactory.eINSTANCE.createOpmStructuralRelation();
			((OpmStructuralRelation)relation).setFrom((OpmProcess)src);
			((OpmStructuralRelation)relation).setTo((OpmProcess)dst);
		}
		else if (src instanceof OpmObject && dst instanceof OpmObject) 
		{
			relation = Midas_prototypeFactory.eINSTANCE.createOpmStructuralRelation();
			((OpmStructuralRelation)relation).setFrom((OpmObject)src);
			((OpmStructuralRelation)relation).setTo((OpmObject)dst);
		}
		else return null;
		
		// Add the relation to the src object because it must be contained in something
		((OpmEntity)src).getReferences().add(relation);
		
		return relation;
	}
	
	public EObject moveContainer(EObject element, EObject newSemanticContainer, EObject oldSemanticContainer) {
		return null;
	}
	
	public EObject dropPrecondition(EObject element) {
		return null;
	}
	
	public boolean containerViewCondition(EObject view) {
		return true;
	}
	
	/**
	 * Create an object. Not used for now because there is no way to find the view that was just created
	 * 
	 * See: https://www.eclipse.org/forums/index.php/t/1074382/
	 * 
	 * Parameters correspond to the variables available when calling a create node tool
	 * @param diagram
	 * @param container
	 * @param containerView
	 * @return
	 */
	public EObject createObject(DDiagram diagram, EObject container, DDiagram containerView) 
	{
		OpmObject newObj = Midas_prototypeFactory.eINSTANCE.createOpmObject();
		EObject parent = container.eContainer();
		
		// This means that the object was placed on the blank space outside the central OpmThing
		// (A non-root diagram is created based on the central OpmThing, so the diagram corresponds to the central OpmThing)
		if (diagram.equals(containerView))
		{
			// After creating the object, a view needs to be created to see it in the diagram.
			Session session = SessionManager.INSTANCE.getSession(container);
			TransactionalEditingDomain domain = session.getTransactionalEditingDomain();
			
			// Finding the right view mapping to use
			String mappingId = "OPD.ObjectReference_CM";
			DiagramMappingsManager mappingManager = DiagramMappingsManagerRegistry.INSTANCE.getDiagramMappingsManager(session, diagram);
			Collection<ContainerMapping> containerMappings = mappingManager.getContainerMappings();
			List<ContainerMapping> mappings = containerMappings.stream().filter(m -> m.getName().equals(mappingId)).collect(Collectors.toList());
			if (mappings.isEmpty())
				return null;
			ContainerMapping mapping = mappings.get(0);
			
			// The parent could either be a root(OPM) EObject or a non-root(OpmThing) EObject, affecting where the EObject is stored
			if (parent instanceof OPM) 
			{
				((OPM)parent).getElements().add(newObj);
				CreateDDiagramElementCommand cmd = new CreateDDiagramElementCommand(domain, newObj, mapping, containerView);
				cmd.canExecute();
				cmd.execute();
			}
			else if (parent instanceof OpmThing)
			{
				((OpmThing)parent).getComposables().add(newObj);
				CreateDDiagramElementCommand cmd = new CreateDDiagramElementCommand(domain, newObj, mapping, containerView);
				cmd.canExecute();
				cmd.execute();
			}
		}
		// This means that the object was placed inside some element(container)
		else 
		{
			if (container instanceof OpmThing)
			{
				((OpmThing)container).getComposables().add(newObj);
			}
		}
		return null;
	}
}
