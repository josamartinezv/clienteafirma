/* Copyright (C) 2011 [Gobierno de Espana]
 * This file is part of "Cliente @Firma".
 * "Cliente @Firma" is free software; you can redistribute it and/or modify it under the terms of:
 *   - the GNU General Public License as published by the Free Software Foundation; 
 *     either version 2 of the License, or (at your option) any later version.
 *   - or The European Software License; either version 1.1 or (at your option) any later version.
 * Date: 11/01/11
 * You may contact the copyright holder at: soporte.afirma5@mpt.es
 */

package es.gob.afirma.signers.pkcs7;

import java.util.Enumeration;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.BERSequence;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

/** Clase base para la implementaci&oacute;n del tipo DigestedData La Estructura
 * del mensaje es la siguiente:<br>
 *
 * <pre>
 * <code>
 *  DigestedData ::= SEQUENCE {
 *        version CMSVersion,
 *        digestAlgorithm DigestAlgorithmIdentifier,
 *        encapContentInfo EncapsulatedContentInfo,
 *        digest Digest }
 *
 *  Digest ::= OCTET STRING
 * </code>
 * </pre>
 *
 * La implementaci&oacute;n del c&oacute;digo ha seguido los pasos necesarios
 * para crear un mensaje DigestedData de BouncyCastle: <a
 * href="http://www.bouncycastle.org/">www.bouncycastle.org</a> */
public final class DigestedData extends ASN1Encodable {
    private final DERInteger version;
    private final AlgorithmIdentifier digestAlgorithm;
    private final ContentInfo contentInfo;
    private final ASN1OctetString digest;

    static DigestedData getInstance(final Object o) {
        if (o instanceof DigestedData) {
            return (DigestedData) o;
        }
        else if (o instanceof ASN1Sequence) {
            return new DigestedData((ASN1Sequence) o);
        }

        throw new IllegalArgumentException("Objeto desconocido: " + o.getClass().getName()); //$NON-NLS-1$
    }

    /** Crea un objeto CMS DigestedData.
     * @param digestAlgo ALgoritmo de huella digital
     * @param contentInfo ContentInfo
     * @param digest Valor de la huella digital
     */
    public DigestedData(final AlgorithmIdentifier digestAlgo, final ContentInfo contentInfo, final ASN1OctetString digest) {
        this.version = new DERInteger(0);
        this.digestAlgorithm = digestAlgo;
        this.contentInfo = contentInfo;
        this.digest = digest;
    }

    /** Crea un object CMS DigestedData a partir de una Secuencia ASN.1.
     * @param seq Secuencia origen
     */
    public DigestedData(final ASN1Sequence seq) {
        final Enumeration<?> e = seq.getObjects();

        this.version = (DERInteger) e.nextElement();
        this.digestAlgorithm = new AlgorithmIdentifier((ASN1Sequence) e.nextElement());
        this.contentInfo = new ContentInfo((ASN1Sequence) e.nextElement());
        this.digest = ((ASN1OctetString) (e.nextElement()));

    }

    DERInteger getVersion() {
        return this.version;
    }

    AlgorithmIdentifier getDigestAlgorithm() {
        return this.digestAlgorithm;
    }

    ASN1OctetString getDigest() {
        return this.digest;
    }

    ContentInfo getContentInfo() {
        return this.contentInfo;
    }

    /** Produce an object suitable for an ASN1OutputStream.
     *
     * <pre>
     * DigestedData ::= SEQUENCE {
     *     version CMSVersion,
     *     digestAlgorithms DigestAlgorithmIdentifiers,
     *     encapContentInfo EncapsulatedContentInfo,
     *     digest  Digest
     *   }
     *
     * Digest ::= OCTET STRING
     * </pre> */
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector v = new ASN1EncodableVector();

        v.add(this.version);
        v.add(this.digestAlgorithm);
        v.add(this.contentInfo);
        v.add(this.digest);

        return new BERSequence(v);
    }
}
