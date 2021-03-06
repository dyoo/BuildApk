/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


// This is a fork of com.android.jarutils.DebugKeyProvider that lets
// us pass in an inputstream to the keystore, rather than a path.


package plt.wescheme;


import com.android.prefs.AndroidLocation;
import com.android.prefs.AndroidLocation.AndroidLocationException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

/**
 * A provider of a dummy key to sign Android application for debugging purpose.
 * <p/>This provider uses a custom keystore to create and store a key with a known password.
 */
public class DebugKeyProvider {
    
    public interface IKeyGenOutput {
        public void out(String message);
        public void err(String message);
    }
    
    private static final String PASSWORD_STRING = "android";
    private static final char[] PASSWORD_CHAR = PASSWORD_STRING.toCharArray();
    private static final String DEBUG_ALIAS = "AndroidDebugKey";
    
    // Certificate CN value. This is a hard-coded value for the debug key.
    // Android Market checks against this value in order to refuse applications signed with
    // debug keys.
    private static final String CERTIFICATE_DESC = "CN=Android Debug,O=Android,C=US";
    
    private KeyStore.PrivateKeyEntry mEntry;
    
    public static class KeytoolException extends Exception {
        /** default serial uid */
        private static final long serialVersionUID = 1L;
        private String mJavaHome = null;
        private String mCommandLine = null;
        
        KeytoolException(String message) {
            super(message);
        }

        KeytoolException(String message, String javaHome, String commandLine) {
            super(message);
            
            mJavaHome = javaHome;
            mCommandLine = commandLine;
        }
        
        public String getJavaHome() {
            return mJavaHome;
        }
        
        public String getCommandLine() {
            return mCommandLine;
        }
    }
    
    /**
     * Creates a provider using a keystore at the given location.
     * <p/>The keystore.
     * <p/>Password for the store/key is <code>android</code>, and the key alias is
     * <code>AndroidDebugKey</code>.
     * @param osKeyStorePath the input stream to the keystore.
     * @param storeType an optional keystore type, or <code>null</code> if the default is to
     * be used.
     * @param output an optional {@link IKeyGenOutput} object to get the stdout and stderr
     * of the keytool process call.
     * @throws KeytoolException If the creation of the debug key failed.
     * @throws AndroidLocationException 
     */
    public DebugKeyProvider(InputStream osKeyStore, String storeType, IKeyGenOutput output)
            throws KeyStoreException, NoSuchAlgorithmException, CertificateException,
            UnrecoverableEntryException, IOException, KeytoolException, AndroidLocationException {
                
        if (loadKeyEntry(osKeyStore, storeType) == false) {
	    throw new RuntimeException("Cannot load the key store");
        }
    }


    /**
     * Returns the debug {@link PrivateKey} to use to sign applications for debug purpose.
     * @return the private key or <code>null</code> if its creation failed.
     */
    public PrivateKey getDebugKey() throws KeyStoreException, NoSuchAlgorithmException,
            UnrecoverableKeyException, UnrecoverableEntryException {
        if (mEntry != null) {
            return mEntry.getPrivateKey();
        }
        
        return null;
    }

    /**
     * Returns the debug {@link Certificate} to use to sign applications for debug purpose.
     * @return the certificate or <code>null</code> if its creation failed.
     */
    public Certificate getCertificate() throws KeyStoreException, NoSuchAlgorithmException,
            UnrecoverableKeyException, UnrecoverableEntryException {
        if (mEntry != null) {
            return mEntry.getCertificate();
        }

        return null;
    }
    
    /**
     * Loads the debug key from the keystore.
     * @param osKeyStorePath the input stream to the keystore.
     * @param storeType an optional keystore type, or <code>null</code> if the default is to
     * be used.
     * @return <code>true</code> if success, <code>false</code> if the keystore does not exist.
     */
    private boolean loadKeyEntry(InputStream osKeyStore, String storeType) throws KeyStoreException,
            NoSuchAlgorithmException, CertificateException, IOException,
            UnrecoverableEntryException {
        try {
            KeyStore keyStore = KeyStore.getInstance(
                    storeType != null ? storeType : KeyStore.getDefaultType());
            keyStore.load(osKeyStore, PASSWORD_CHAR);
            mEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(
                    DEBUG_ALIAS, new KeyStore.PasswordProtection(PASSWORD_CHAR));
        } catch (FileNotFoundException e) {
            return false;
        }
        
        return true;
    }

}
