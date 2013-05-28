package test.java;
import de.butatopanto.xmlsig.PrivateKeyData;
import de.butatopanto.xmlsig.XmlSigner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.net.URL;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class XmlSignTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    private XmlSigner signer;

    @Before
    public void createSignerWithKeyData() throws Exception {
        PrivateKeyData keyData = createKeyData();
        this.signer = new XmlSigner(keyData);
    }

    private PrivateKeyData createKeyData() {
        String pathToKeystore = getPathToFileOnClasspath("certificate.p12");
        String passphraseForKeystore = "pass";
        String passphraseForKey = "pass";
        return new PrivateKeyData(pathToKeystore, passphraseForKeystore, passphraseForKey);
    }

    @Test
    public void canValidateAFileItSignedItself() throws Exception {
        String pathToInputFile = getPathToInputFile();
        String pathToOutputFile = getPathToOutputFile();
        sign(pathToInputFile, pathToOutputFile);
    }

    private void sign(String pathToInputFile, String pathToOutputFile) throws Exception {
        signer.sign(pathToInputFile, pathToOutputFile);
    }

    private String getPathToInputFile() {
        return getPathToFileOnClasspath("nota.xml");
    }

    private String getPathToFileOnClasspath(String name) {
        URL unsignedXmlUrl = getClass().getClassLoader().getResource(name);
        return unsignedXmlUrl.getFile();
    }

    private String getPathToOutputFile() throws Exception {
        File outputFile = folder.newFile("outputFile");
        return outputFile.getAbsolutePath();
    }
}
