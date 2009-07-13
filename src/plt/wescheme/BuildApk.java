package plt.wescheme;


import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;


public class BuildApk {
    SignedJarBuilder builder;
    DebugKeyProvider keyProvider;

    public MakePhonegapApp(InputStream keyInputStream,
			   OutputStream os) {
	try {
	    this.keyProvider = new DebugKeyProvider(keyInputStream,
						    null,
						    null);
	    PrivateKey key = keyProvider.getDebugKey();
	    X509Certificate certificate = (X509Certificate)keyProvider.getCertificate();
	    
	    this.builder = new SignedJarBuilder(os, key, certificate);
	    
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }

    public void addInputStream(InputStream is, String jarPath) throws IOException {
	this.builder.writeInputStream(is, jarPath);
    }

    public void addZipInputStream(InputStream is) throws IOException {
	this.builder.writeZip(is, null);
    }


    // build: -> void
    // Finalize the building of the .apk file
    public void build() {
    }

}