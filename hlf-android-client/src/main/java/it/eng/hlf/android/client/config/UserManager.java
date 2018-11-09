package it.eng.hlf.android.client.config;

import it.eng.hlf.android.client.exception.HLFClientException;
import it.eng.hlf.android.client.helper.ChannelInitializationManager;
import it.eng.hlf.android.client.utils.Utils;

import org.android.apache.commons.codec.binary.StringUtils;
import org.apache.commons.io.IOUtils;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric_ca.sdk.exception.EnrollmentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Set;

/**
 * @author ascatox
 */
public class UserManager {
    private final static Logger log = LoggerFactory.getLogger(UserManager.class);

    private static UserManager instance;
    private Configuration configuration;
    private Organization organization;

    private UserManager(Configuration configuration, Organization organization) {
        this.configuration = configuration;
        this.organization = organization;
    }

    public static UserManager getInstance(Configuration configuration, Organization organization) throws HLFClientException, InvalidArgumentException {
        if (instance == null || !instance.organization.equals(organization)) { //1
            synchronized (ChannelInitializationManager.class) {
                if (instance == null || !instance.organization.equals(organization)) {  //2
                    instance = new UserManager(configuration, organization);
                }
            }
        }
        return instance;
    }


    public void completeUsers(File cert, File keystore) throws HLFClientException {
        try {
            Set<User> users = organization.getUsers();
            for (User user : users) {
                doCompleteUser(user, keystore, cert);
            }
        } catch (IOException | NoSuchProviderException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error(e.getMessage());
            throw new HLFClientException(e);
        }
    }

    private void doCompleteUser(User user, File cert, File keystore) throws IOException, NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException, HLFClientException {
        user.setMspId(organization.getMspID());
        // if (user.isAdmin()) {
        //File certConfigPath = ExternalStorageReader.getCertConfigPath(organization.getDomainName(), user, configuration.getCryptoconfigdir());
        String certificate = new String(IOUtils.toByteArray(new FileInputStream(cert)), ConfigManager.UTF_8);
        //File fileSk = Utils.findFileSk(organization.getDomainName(), user, configuration.getCryptoconfigdir());
        PrivateKey privateKey = Utils.getPrivateKeyFromBytes(IOUtils.toByteArray(new FileInputStream(keystore)));
        user.setEnrollment(new Enrollment(privateKey, certificate));
        // }
        /*else {
            enrollUser(user, organization.getCa());
        }*/
    }

    private void enrollUser(User user, Ca ca) throws HLFClientException {
        try {
            if (null == user.getSecret() || "".equals(user.getSecret()) || null == ca) {
                throw new HLFClientException("Secret for user: " + user.getName() + " not given or error in CA retrieving!!!");
            }
            final org.hyperledger.fabric.sdk.Enrollment enrollment = ca.getCaClient().enroll(user.getName(), user.getSecret());
            if (null == enrollment) {
                throw new HLFClientException("User: " + user.getName() + " not correctly enrolled or problems enrolling!!!");

            }
            user.setEnrollment(new Enrollment(enrollment.getKey(), enrollment.getCert()));
        } catch (EnrollmentException | org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException e) {
            throw new HLFClientException(e);
        }
    }


}
