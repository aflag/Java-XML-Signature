package de.butatopanto.xmlsig;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.ParseException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;
import static javax.xml.crypto.dsig.CanonicalizationMethod.INCLUSIVE;
import static javax.xml.crypto.dsig.SignatureMethod.RSA_SHA1;
import static javax.xml.crypto.dsig.Transform.ENVELOPED;

public class XmlSigner extends DomValidationOperator {

    private final PrivateKeyProvider provider;

    public XmlSigner(PrivateKeyData keyData) throws IOException, NoSuchAlgorithmException, UnrecoverableEntryException, KeyStoreException, CertificateException {
        this.provider = new Pkcs12KeyProvider(factory, keyData);
    }

    public void sign(String pathToUnsignedDocument, String pathToSignedDocument) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, KeyStoreException, IOException, UnrecoverableEntryException, CertificateException, ParserConfigurationException, SAXException, MarshalException, XMLSignatureException, TransformerException {
        Document document = new DocumentReader(pathToUnsignedDocument).loadDocument();
        sign(document);
        new DocumentWriter(pathToSignedDocument).writeDocument(document);
    }
    
    private void sign(Document document) throws MarshalException, XMLSignatureException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
    	NodeList elements = document.getElementsByTagName("Rps");
    	for (int i = 0; i < elements.getLength(); ++i) {
    		Element rps = (Element) elements.item(i);
    		Element infRps = (Element) rps.getElementsByTagName("InfRps").item(0);
    		infRps.setIdAttribute("Id", true);
            SignedInfo signedInfo = createSignature("#"+infRps.getAttribute("Id"));
            XMLSignature signature = factory.newXMLSignature(signedInfo, provider.loadKeyInfo());
    		DOMSignContext signContext = new DOMSignContext(provider.loadPrivateKey(), rps);
            signature.sign(signContext);
    	}
    	Element enviarLoteRpsEnvio = (Element) document.getElementsByTagName("EnviarLoteRpsEnvio").item(0);
 		Element loteRps = (Element) enviarLoteRpsEnvio.getElementsByTagName("LoteRps").item(0);
 		loteRps.setIdAttribute("Id", true);
        SignedInfo signedInfo = createSignature("#"+loteRps.getAttribute("Id"));
        XMLSignature signature = factory.newXMLSignature(signedInfo, provider.loadKeyInfo());
		DOMSignContext signContext = new DOMSignContext(provider.loadPrivateKey(), enviarLoteRpsEnvio);
        signature.sign(signContext);    	
    }

    /**
     * @param uri referência ao elemento que será assinado
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     */
    private SignedInfo createSignature(String uri) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        DigestMethod digestMethod = factory.newDigestMethod(DigestMethod.SHA1, null);
        List<Transform> transformList = new ArrayList<Transform>();
        Transform enveloped_transform = factory.newTransform(
        		ENVELOPED, (TransformParameterSpec) null);
        Transform c14NTransform = factory.newTransform(
        		"http://www.w3.org/TR/2001/REC-xml-c14n-20010315", (TransformParameterSpec) null);
        transformList.add(enveloped_transform);
        transformList.add(c14NTransform);
        Reference reference = factory.newReference(uri, digestMethod, transformList, null, null);
        SignatureMethod signatureMethod = factory.newSignatureMethod(RSA_SHA1, null);
        CanonicalizationMethod canonicalizationMethod = factory.newCanonicalizationMethod(INCLUSIVE, (C14NMethodParameterSpec) null);
        return factory.newSignedInfo(canonicalizationMethod, signatureMethod, singletonList(reference));
    }
    
    public static void main(String args[]) throws NoSuchAlgorithmException, UnrecoverableEntryException, KeyStoreException, CertificateException, IOException, InvalidAlgorithmParameterException, ParserConfigurationException, SAXException, MarshalException, XMLSignatureException, TransformerException {
    	Options options = new Options();
    	options.addOption("h", "help", false, "exibe esta mensagem");
    	options.addOption("k", "key", true, "chave PKCS12 no formato pfx");
    	options.addOption("p", "password", true, "senha");
    	options.addOption("i", "input", true, "arquivo de entrada (não assinado)");
    	options.addOption("o", "output", true, "arquivo de saida (assinado)");
    	CommandLineParser parser = new BasicParser();
    	CommandLine line;
    	HelpFormatter formatter = new HelpFormatter();
    	try {
			line = parser.parse( options, args );
		} catch (ParseException e) {
			formatter.printHelp("rps-sign", options, true);
			return;
		}
    	if (line.hasOption("h")) {
    		formatter.printHelp("rps-sign", options, true);
			return;
    	}
    	if (!line.hasOption("k") || !line.hasOption("p") || !line.hasOption("i") || !line.hasOption("o")) {
    		formatter.printHelp("rps-sign", options, true);
			return;
    	}
    	String key = line.getOptionValue("k");
    	String pass = line.getOptionValue("p");
    	String input = line.getOptionValue("i");
    	String output = line.getOptionValue("o");
    	new XmlSigner(new PrivateKeyData(key, pass, pass)).sign(input, output);
    }
}
