package plt.wescheme;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class TestBuildApk {
    static public void main(String[] args) throws Exception {
	InputStream keyIs = new FileInputStream(args[0]);
	InputStream zipIs = new FileInputStream(args[1]);
	OutputStream os = new FileOutputStream(args[2]);
	BuildApk builder = new BuildApk(keyIs, os);
	builder.addZipInputStream(zipIs);
	builder.close();
	keyIs.close();
	zipIs.close();
	os.close();
    }
}