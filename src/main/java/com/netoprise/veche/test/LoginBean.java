package com.netoprise.veche.test;

import javax.enterprise.inject.Model;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.inject.Inject;

import com.netoprise.veche.CallbackVerifier;
import com.netoprise.veche.OAuthSession;
import com.netoprise.veche.cdi.VerifierCache;

@Model
public class LoginBean implements PhaseListener {

    private int scope = Integer.MIN_VALUE;

    private String message = "INVOKE_APPLICATION phase missed";

    @Inject
    VerifierCache cache;

    /**
     * @return the scope
     */
    public int getScope() {
	return this.scope;
    }

    /**
     * @param scope
     *            the scope to set
     */
    public void setScope(int scope) {
	this.scope = scope;
    }

    public String getMessage() {
	return this.message;
    }

    @Override
    public void afterPhase(PhaseEvent event) {
    }

    @Override
    public void beforePhase(PhaseEvent event) {
	try {
	    FacesContext facesContext = event.getFacesContext();
	    if (!facesContext.isPostback()) {
		if (this.scope > Integer.MIN_VALUE) {
		    CallbackVerifier<OAuthSession> callbackVerifier = cache.getVerifier(scope);
		    OAuthSession oAuthSession = callbackVerifier.verify(facesContext.getExternalContext().getRequestParameterMap());
		    this.message = "Verified " + callbackVerifier.getProviderId() + "user id: " + oAuthSession.getClientId();
		} else {
		    this.message = "Missed scope param";
		}
	    } else {
		this.message = "Faces Postback";
	    }

	} catch (Exception e) {
	    this.message = "ERROR: " + e.getMessage();
	}
    }

    @Override
    public PhaseId getPhaseId() {
	return PhaseId.INVOKE_APPLICATION;
    }

}
