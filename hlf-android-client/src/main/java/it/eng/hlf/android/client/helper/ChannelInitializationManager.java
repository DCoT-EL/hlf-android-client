package it.eng.hlf.android.client.helper;

import it.eng.hlf.android.client.config.ConfigManager;
import it.eng.hlf.android.client.config.OrdererConfig;
import it.eng.hlf.android.client.config.Organization;
import it.eng.hlf.android.client.config.PeerConfig;
import it.eng.hlf.android.client.exception.HLFClientException;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.EventHub;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @author ascatox
 */
public class ChannelInitializationManager {
    private final static Logger log = LoggerFactory.getLogger(ChannelInitializationManager.class);

    private ConfigManager configManager;
    private HFClient client;
    private Channel channel;
    private Organization organization;

    /**
     * Singleton used to keep the channel configuration.
     */
    private static ChannelInitializationManager ourInstance;

    public static ChannelInitializationManager getInstance(HFClient client, ConfigManager configManager, Organization organization) throws HLFClientException, InvalidArgumentException {
        if (ourInstance == null || !ourInstance.organization.equals(organization)) { //1
            synchronized (ChannelInitializationManager.class) {
                if (ourInstance == null) {  //2
                    ourInstance = new ChannelInitializationManager(client, configManager, organization);
                }
            }
        }
        return ourInstance;
    }


    private ChannelInitializationManager(HFClient client, ConfigManager configManager, Organization organization) throws HLFClientException, InvalidArgumentException {
        initializeChannel(client, configManager, organization);
    }

    private void setupEnv(HFClient client, ConfigManager configManager, Organization organization) throws InvalidArgumentException, HLFClientException {
        this.client = client;
        this.configManager = configManager;
        this.organization = organization;
        //Only peer Admin org
        client.setUserContext(organization.getLoggedUser());
        if (null == getChannel())
            this.channel = client.getChannel(configManager.getConfiguration().getChannelName());
        if (channel == null) {
            log.warn("Channel " + configManager.getConfiguration().getChannelName() + " not initialized...");
            channel = client.newChannel(configManager.getConfiguration().getChannelName());
        }
    }

    private void initializeChannel(HFClient client, ConfigManager configManager, Organization organization) throws HLFClientException, InvalidArgumentException {
        ////////////////////////////
        //Initialize the channel
        //
        setupEnv(client, configManager, organization);
        try {
            log.debug("Constructing channel java structures %s", channel.getName());

            buildOrderers(organization.getOrderers());

            buildPeers(organization.getPeers());

            buildEventHubs(organization.getPeers());

            channel.initialize(); //There's no need to initialize the channel we are only building the java
            // structures.
            log.debug("Finished initialization channel java structures %s", channel.getName());
        } catch (InvalidArgumentException | TransactionException e) {
            log.error(e.getMessage());
            throw new HLFClientException(e);
        }

    }

    //@ascatox Pay attention!!! EventHubs are the same as Peers with different urls
    private void buildEventHubs(List<PeerConfig> peerConfigList) throws InvalidArgumentException {
        for (PeerConfig peerConfigObj : peerConfigList) {

            final Properties eventHubProperties = configManager.getEventHubProperties(peerConfigObj.getName());

            setGRPCProperties(eventHubProperties);

            EventHub eventHub = client.newEventHub(peerConfigObj.getName(), configManager.grpcTLSify(peerConfigObj.getEventURL()), eventHubProperties);
            channel.addEventHub(eventHub);
        }
    }



    private void buildPeers(List<PeerConfig> peerConfigList) throws InvalidArgumentException {
        for (PeerConfig peerConfigObj : peerConfigList) {
            String peerLocation = peerConfigObj.getRequestURL();

            Properties peerProperties = configManager.getPeerProperties(peerConfigObj.getName()); //CaUser properties for
            // peer.. if
            // any.
            if (peerProperties == null) {
                peerProperties = new Properties();
            }
            //Example of setting specific options on grpc's NettyChannelBuilder
            peerProperties.put("grpc.NettyChannelBuilderOption.maxInboundMessageSize", 9000000);

            Peer peer = client.newPeer(peerConfigObj.getName(), configManager.grpcTLSify(peerLocation), peerProperties);
            //            newChannel.joinPeer(peer);
            channel.addPeer(peer);
            //org.addPeer(peer);
        }
    }

    private void buildOrderers(List<OrdererConfig> orderersConfig) throws InvalidArgumentException {
        List<org.hyperledger.fabric.sdk.Orderer> orderers = new LinkedList<>();
        for (OrdererConfig ordererConfig : orderersConfig) {

            Properties ordererProperties = configManager.getOrdererProperties(ordererConfig.getName());
            //example of setting keepAlive to avoid timeouts on inactive http2 connections.
            // Under 5 minutes would require changes to server side to accept faster ping rates.
            setGRPCProperties(ordererProperties);
            orderers.add(client.newOrderer(ordererConfig.getName(), configManager.grpcTLSify(ordererConfig.getUrl()), ordererProperties));
        }
        org.hyperledger.fabric.sdk.Orderer anOrderer = orderers.iterator().next();
        //Just pick the first orderer in the list to create the channel.
        channel.addOrderer(anOrderer);
        for (org.hyperledger.fabric.sdk.Orderer orderer : orderers) { //add remaining orderers if any.
            if (!orderer.equals(anOrderer))
                channel.addOrderer(orderer);
        }

    }

    private void setGRPCProperties(Properties properties) {
        properties.put("grpc.NettyChannelBuilderOption.keepAliveTime", new Object[]{10L, TimeUnit
                .MINUTES});
        properties.put("grpc.NettyChannelBuilderOption.keepAliveTimeout", new Object[]{16L, TimeUnit
                .SECONDS});
    }

    public Channel getChannel() {
        return channel;
    }
}
