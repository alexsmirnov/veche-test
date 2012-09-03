/**
 * 
 */
package com.netoprise.veche.test;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.FacesException;
import javax.inject.Named;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.netoprise.veche.ApplicationCredentials;
import com.netoprise.veche.OAuthException;
import com.netoprise.veche.OAuthSession;
import com.netoprise.veche.ProviderId;
import com.netoprise.veche.ServiceProvider;
import com.netoprise.veche.cdi.Api;
import com.netoprise.veche.cdi.Login;
import com.netoprise.veche.cdi.Register;
import com.netoprise.veche.cdi.VerifierCache;
import com.netoprise.veche.faces.OAuthController;
import com.netoprise.veche.util.OAuthKeys;

/**
 * @author asmirnov
 * 
 */

@Named("config")
@ApplicationScoped
public class Configuration {

    private Map<ProviderId, OAuthKeys> oauthKeys;

    /**
     * 
     */
    public Configuration() {
	try {
	    oauthKeys = OAuthKeys.readBySystemProperty("oauth");
        } catch (OAuthException e) {
	    throw new FacesException();
        }
    }

    @Produces
    @Register
    @Login
    @Api
    public List<ApplicationCredentials> loginKeys() {
	return ImmutableList.<ApplicationCredentials>copyOf(oauthKeys.values());
    }


    @RequestScoped
    @Named("logins")
    @Produces
    @Login
    List<OAuthController<OAuthSession>> logins(@Login List<ServiceProvider<OAuthSession>> providers,final VerifierCache cache){
	return Lists.transform(providers, new Function<ServiceProvider<OAuthSession>, OAuthController<OAuthSession>>() {

	    @Override
            public OAuthController<OAuthSession> apply(@Nullable ServiceProvider<OAuthSession> provider) {
	        return new OAuthController<OAuthSession>(cache, provider, "/login.xhtml");
            }
	});
    }
}
