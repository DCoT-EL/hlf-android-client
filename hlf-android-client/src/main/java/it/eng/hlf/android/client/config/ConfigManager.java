package it.eng.hlf.android.client.config;

import android.content.Context;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.eng.hlf.android.client.exception.HLFClientException;

import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.helper.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;

import static java.lang.String.format;

public class ConfigManager {

    public static final String INVOKEWAITTIME = "100000";
    public static final String DEPLOYWAITTIME = "120000";
    public static final String PROPOSALWAITTIME = "120000";
    public static final String UTF_8 = "UTF-8";

    private final static Logger log = LoggerFactory.getLogger(ConfigManager.class);

    private Configuration configuration;
    private static ConfigManager ourInstance;

    private ConfigManager(InputStream configFabricNetwork) {
        this.configuration = loadConfigurationFromJSONFile(configFabricNetwork);
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }

    public static ConfigManager getInstance(InputStream configFabricNetwork) throws HLFClientException, InvalidArgumentException {
        if (ourInstance == null) { //1
            synchronized (ConfigManager.class) {
                if (ourInstance == null) {  //2
                    ourInstance = new ConfigManager(configFabricNetwork);
                }
            }
        }
        return ourInstance;
    }


    private Configuration loadConfigurationFromJSONFile(InputStream configFabricNetwork) {
        try {
            //InputStream resource = ExternalStorageReader.getConfigurationFile();
            ObjectMapper objectMapper = new ObjectMapper();
            Configuration configuration = objectMapper.readValue(configFabricNetwork, Configuration.class);
            //log.debug("Configuration JSON is\n" + resource.getPath());
            return configuration;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;

    }

    public Properties getPeerProperties(String name) {

        return getEndpointProperties("peer", name);

    }

    public Properties getOrdererProperties(String name) {

        return getEndpointProperties("orderer", name);

    }

    private Properties getEndpointProperties(final String type, final String name) {

        final String domainName = getDomainName(name);


        File certTLS = Paths.get(configuration.getCryptoconfigdir() + "/ordererOrganizations".replace("orderer", type), domainName, type + "s",
                name, "tls/server.crt").toFile();
        if (!certTLS.exists() && configuration.isTls()) {
            throw new RuntimeException(format("Missing cert file for: %s. Could not find at location: %s", name,
                    certTLS.getAbsolutePath()));
        }

        Properties ret = new Properties();
        ret.setProperty("pemFile", certTLS.getAbsolutePath());
        //      ret.setProperty("trustServerCertificate", "true"); //testing environment only NOT FOR PRODUCTION!
        ret.setProperty("hostnameOverride", name);
        ret.setProperty("sslProvider", "openSSL");
        ret.setProperty("negotiationType", "TLS");

        return ret;
    }

    public Properties getEventHubProperties(String name) {
        return getEndpointProperties("peer", name); //uses same as named peer
    }


    private String getDomainName(final String name) {
        int dot = name.indexOf(".");
        if (-1 == dot) {
            return null;
        } else {
            return name.substring(dot + 1);
        }

    }

    public String getTestChannelPath() {
        return configuration.getCryptoconfigdir();
    }


    public int getTransactionWaitTime() {
        return Integer.parseInt(INVOKEWAITTIME);
    }

    public int getDeployWaitTime() {
        return Integer.parseInt(DEPLOYWAITTIME);
    }

    public long getProposalWaitTime() {
        return Integer.parseInt(PROPOSALWAITTIME);
    }

    public String grpcTLSify(String location) {
        location = location.trim();
        Exception e = Utils.checkGrpcUrl(location);
        if (e != null) {
            throw new RuntimeException(String.format("Bad TEST parameters for grpc url %s", location), e);
        }
        return configuration.isTls() ?
                location.replaceFirst("^grpc://", "grpcs://") : location;

    }

    public String httpTLSify(String location) {
        location = location.trim();

        return configuration.isTls() ?
                location.replaceFirst("^http://", "https://") : location;
    }


}//end class