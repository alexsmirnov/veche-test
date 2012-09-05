/**
 * 
 */
package com.netoprise.veche.test;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
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
import com.netoprise.veche.Provider;
import com.netoprise.veche.ProviderId;
import com.netoprise.veche.ServiceProvider;
import com.netoprise.veche.cdi.Api;
import com.netoprise.veche.cdi.Login;
import com.netoprise.veche.cdi.Register;
import com.netoprise.veche.cdi.VerifierCache;
import com.netoprise.veche.faces.OAuthController;
import com.netoprise.veche.util.OAuthKeys;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * @author asmirnov
 * 
 */

@Named("config")
@ApplicationScoped
public class Configuration {

    private static final Function<Config, ApplicationCredentials> CONFIG_TO_CREDENTIALS = new Function<Config, ApplicationCredentials>() {

	@Override
        public ApplicationCredentials apply(@Nullable Config config) {
	    String idString = config.getString("id");
	    Provider provider = Provider.valueOf(idString);
	    return new ApplicationCredentials(provider, config.getString("key"), config.getString("secret"));
        }
    };
    
//    private Map<ProviderId, OAuthKeys> oauthKeys;

    private List<ApplicationCredentials> credentials;

    /**
     * 
     */
    public Configuration() {
//	try {
//	    oauthKeys = OAuthKeys.readBySystemProperty("oauth");
//        } catch (OAuthException e) {
//	    throw new FacesException();
//        }
    }

    @PostConstruct
    public void loadConfiguration() {
	Config config = ConfigFactory.load();
	List<? extends Config> configList = config.getConfigList("providers");
	this.credentials = Lists.transform(configList, CONFIG_TO_CREDENTIALS);
    }
    
    @Produces
    @Register
    @Login
    @Api
    public List<ApplicationCredentials> loginKeys() {
	return credentials;//ImmutableList.<ApplicationCredentials>copyOf(oauthKeys.values());
    }


    @RequestScoped
    @Named("logins")
    @Produces
    @Login
    public List<OAuthController<OAuthSession>> logins(@Login List<ServiceProvider<OAuthSession>> providers,final VerifierCache cache){
	return Lists.transform(providers, new Function<ServiceProvider<OAuthSession>, OAuthController<OAuthSession>>() {

	    @Override
            public OAuthController<OAuthSession> apply(@Nullable ServiceProvider<OAuthSession> provider) {
	        return new OAuthController<OAuthSession>(cache, provider, "/login.xhtml");
            }
	});
    }
}
