package com.netoprise.veche.test;

import javax.enterprise.inject.Model;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.inject.Inject;

import com.google.common.base.Optional;
import com.netoprise.veche.CallbackVerifier;
import com.netoprise.veche.OAuthSession;
import com.netoprise.veche.SessionOrDenied;
import com.netoprise.veche.cdi.VerifierCache;

@Model
public class LoginBean extends PageActionListener {

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
    public String action() {
	String result;
	if (this.scope > Integer.MIN_VALUE) {
	    FacesContext facesContext = FacesContext.getCurrentInstance();
	    Optional<CallbackVerifier<OAuthSession>> callbackVerifierOption = cache.getVerifier(scope);
	    if (callbackVerifierOption.isPresent()) {
		CallbackVerifier<OAuthSession> callbackVerifier = callbackVerifierOption.get();
		SessionOrDenied<OAuthSession> sessionOrDenied = callbackVerifier.verify(facesContext.getExternalContext()
		        .getRequestParameterMap());
		if (sessionOrDenied.isAuthtorized()) {
		    OAuthSession oAuthSession = sessionOrDenied.getSession();
		    this.message = "Verified " + callbackVerifier.getProviderId() + "user id: " + oAuthSession.getClientId();
		    result = "success";
		} else {
		    this.message = "oAuth error: " + sessionOrDenied.getErrorDescription();
		    result = "denied";
		}
	    } else {
		this.message = "Missed verifier in cache";
		result = "verifierError";
	    }
	} else {
	    this.message = "Missed scope param";
	    result = "missedScope";
	}
        return result;
    }

}
