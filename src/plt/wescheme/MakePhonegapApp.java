package plt.wescheme;

import com.android.jarutils.SignedJarBuilder;

import java.io.InputStream;



public class MakePhonegapApp {
    SignedJarBuilder builder;
    DebugKeyProvider keyProvider;

    public MakePhonegapApp(InputStream keyInputStream) {
	this.keyProvider = new DebugKeyProvider(keyInputStream,
						null,
						null);
    }

    public void addInputStream(InputString is,
			       String path) {
    }

    public void addZipInputStream(InputString is) {
    }


    // build: OutputStream -> void
    // Write out the built .apk file
    public void build(OutputStream os) {
    }

}