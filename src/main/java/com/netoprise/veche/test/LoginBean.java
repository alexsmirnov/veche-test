package com.netoprise.veche.test;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.inject.Inject;
import javax.inject.Named;

import com.google.common.base.Optional;
import com.netoprise.veche.CallbackVerifier;
import com.netoprise.veche.OAuthSession;
import com.netoprise.veche.ProviderId;
import com.netoprise.veche.SessionOrDenied;
import com.netoprise.veche.cdi.VerifierCache;
import com.netoprise.veche.faces.CallbackAction;
import com.netoprise.veche.faces.CallbackMessages;
import com.netoprise.veche.faces.PageActionListener;
import com.netoprise.veche.faces.SessionAction;

@Model
public class LoginBean implements SessionAction<OAuthSession>, CallbackMessages {


    private String message = "INVOKE_APPLICATION phase missed";


    private CallbackAction<OAuthSession> callbackAction;
    
    public LoginBean() {
	// default constructor required by bean contract
    }

    @Inject
    public LoginBean(VerifierCache cache) {
	callbackAction = new CallbackAction<OAuthSession>(cache, this, this);
    }
    
    public String getMessage() {
	return this.message;
    }


    @Produces
    @Named("callbackAction")
    public CallbackAction<OAuthSession> getCallbackAction(){
	return callbackAction;
    }
    
    @Override
    public void denied(FacesContext facesContext, ProviderId providerId, String errorDescription) {
	    this.message = "oAuth error: " + errorDescription;
    }

    @Override
    public void missedVerifier(FacesContext facesContext) {
	this.message = "Missed verifier in cache";
    }

    @Override
    public void missedScope(FacesContext facesContext) {
	    this.message = "Missed scope param";
    }

    @Override
    public String action(OAuthSession session) {
	    this.message = "Verified " + session.getProviderId() + "user id: " + session.getClientId();
	    return "success";
    }

}
