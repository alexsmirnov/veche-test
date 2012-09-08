package com.netoprise.veche.test;

import javax.enterprise.context.RequestScoped;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

public  class PageActionListener implements PhaseListener, PageAction {

    /**
     * 
     */
    private static final long serialVersionUID = -675022794443003585L;

    private PageAction pageAction = this;
    
    private String fromAction = null;
    
    public void setPageAction(PageAction pageAction) {
	this.pageAction = pageAction;
    }
    
    public void setFromAction(String fromAction) {
	this.fromAction = fromAction;
    }

    @Override
    public void afterPhase(PhaseEvent event) {
    }

    @Override
    public void beforePhase(PhaseEvent event) {
	try {
	    FacesContext facesContext = event.getFacesContext();
	    if (!facesContext.isPostback()) {
		String outcome = performAction();
		Application application = facesContext.getApplication();
		NavigationHandler navigationHandler = application.getNavigationHandler();
		navigationHandler.handleNavigation(facesContext, fromAction, outcome);
		facesContext.renderResponse();
		}
	} catch (Exception e) {
	    throw new FacesException(e);
	}
    }

    private String performAction(){
	return pageAction.action();
    }
    
    @Override
    public PhaseId getPhaseId() {
	return PhaseId.INVOKE_APPLICATION;
    }

    public String action() {
	return null;
    }
}
