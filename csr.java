public static KeyPair generateRSAKeyPair() {
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    keyPairGenerator.initialize(2048);
    return keyPairGenerator.generateKeyPair();
}

public static PKCS10CertificationRequest generateCSR(KeyPair pair) {
    PKCS10CertificationRequestBuilder p10Builder = new JcaPKCS10CertificationRequestBuilder(
      new X500Principal("CN=Requested Test Certificate"), pair.getPublic());
    JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder("SHA256withRSA");
    ContentSigner signer = csBuilder.build(pair.getPrivate());
    return p10Builder.build(signer);
}

public X509Certificate sign(PKCS10CertificationRequest inputCSR, PrivateKey caPrivate, KeyPair pair) {
    AlgorithmIdentifier sigAlgId = new DefaultSignatureAlgorithmIdentifierFinder().find("SHA1withRSA");
    AlgorithmIdentifier digAlgId = new DefaultDigestAlgorithmIdentifierFinder().find(sigAlgId);

    AsymmetricKeyParameter foo = PrivateKeyFactory.createKey(caPrivate.getEncoded());
    SubjectPublicKeyInfo keyInfo = SubjectPublicKeyInfo.getInstance(pair.getPublic().getEncoded());

    X509v3CertificateBuilder myCertificateGenerator = new X509v3CertificateBuilder(
      new X500Name("CN=issuer"), 
      new BigInteger("1"), 
      new Date(System.currentTimeMillis()), 
      new Date(System.currentTimeMillis() + 30L * 365 * 24 * 60 * 60 * 1000), 
      inputCSR.getSubject(), 
      keyInfo);

    ContentSigner sigGen = new BcRSAContentSignerBuilder(sigAlgId, digAlgId).build(foo);

    X509CertificateHolder holder = myCertificateGenerator.build(sigGen);
    Certificate eeX509CertificateStructure = holder.toASN1Structure();

    CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");

    InputStream is1 = new ByteArrayInputStream(eeX509CertificateStructure.getEncoded());
    X509Certificate theCert = (X509Certificate) cf.generateCertificate(is1);
    is1.close();
    return theCert;
}

@Test
public void givenCSR_whenSignWithBC_thenSuccess() {
    SignCSRBouncyCastle signCSRBouncyCastle = new SignCSRBouncyCastle();
    KeyPair pair = SignCSRBouncyCastle.generateRSAKeyPair();
    PKCS10CertificationRequest csr = SignCSRBouncyCastle.generateCSR(pair);
    KeyPair caPair = SignCSRBouncyCastle.generateRSAKeyPair();
    X509Certificate signedCert = signCSRBouncyCastle.signCSR(csr, caPair.getPrivate(), pair);

    assertThat(signedCert).isNotNull();
    assertThat(signedCert.getSubjectDN().getName()).isEqualTo("CN=Requested Test Certificate");
    assertDoesNotThrow(() -> signedCert.verify(caPair.getPublic()));
}
