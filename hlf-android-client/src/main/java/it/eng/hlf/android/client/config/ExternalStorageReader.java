package it.eng.hlf.android.client.config;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import it.eng.hlf.android.client.exception.HLFClientException;
import it.eng.hlf.android.client.utils.Utils;

import static java.lang.String.format;

public class ExternalStorageReader {

    private final static String EXTERNAL_STORAGE_PATH =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/";

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static InputStream getConfigurationFile() throws FileNotFoundException, HLFClientException {
        if (!isExternalStorageReadable())
            throw new HLFClientException("External Storage not available!");
        FileInputStream fileInputStream = new FileInputStream(new File(EXTERNAL_STORAGE_PATH + "config-fabric-network.json"));
        return fileInputStream;
    }

    public static File getSkConfigPath(String domainName, User user, String cryptoDir) throws HLFClientException {
        if (!isExternalStorageReadable())
            throw new HLFClientException("External Storage not available!");
        String usersPath = format("/users/" + user + "@%s/msp/keystore/", domainName);
        if (Utils.isEmpty(cryptoDir)) {
            cryptoDir = EXTERNAL_STORAGE_PATH + "crypto-config";
            if (!user.isAdmin())
                cryptoDir = EXTERNAL_STORAGE_PATH + "crypto-users";
        }
        String path = cryptoDir + "/peerOrganizations/" + domainName + usersPath;
        if (!user.isAdmin())
            path = cryptoDir + "/" + domainName + "/" + user.getName() + "/keystore/";
        File dir = new File(path);
        return dir;
    }

    public static File getCertConfigPath(String domainName, User user, String cryptoDir) throws HLFClientException {
        if (!isExternalStorageReadable())
            throw new HLFClientException("External Storage not available!");
        String usersPath = format("/users/" + user + "@%s/msp/signcerts/" + user + "@%s-cert.pem", domainName,
                domainName);
        if (Utils.isEmpty(cryptoDir)) {
            cryptoDir = EXTERNAL_STORAGE_PATH + "crypto-config";
            if (!user.isAdmin())
                cryptoDir = EXTERNAL_STORAGE_PATH + "crypto-users";
        }
        String path = cryptoDir + "/peerOrganizations/" + domainName + usersPath;
        if (!user.isAdmin())
            path = cryptoDir + "/" + domainName + "/" + user.getName() + "/ca-cert.pem";
        File dir = new File(path);
        return dir;
    }


}
