package plt.wescheme;

import java.io.InputStream;

public class MakePhonegapApp {
    public MakePhonegapApp(InputStream keyInputStream,
			   InputStream javascriptSource,
			   InputStream androidManifest,
			   InputStream applicationTemplateZip) {
	this.keyInputStream = keyInputStream;
    }

    public void addInputStream(InputString is,
			       String path) {
    }

    public void build(OutputStream os) {
    }

}