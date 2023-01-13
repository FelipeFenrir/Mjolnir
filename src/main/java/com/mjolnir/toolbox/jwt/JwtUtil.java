/*
 * Copyright (c) 2020. Fenrir Solucoes em Tecnologia. All rights reserved.
 *  Fenrir Systems, Odin System and All the Programing Code of this softwares are private.
 */
package com.mjolnir.toolbox.jwt;

//import com.mjolnir.toolbox.jwt.ConfigJwt;
//import com.mjolnir.toolbox.jwt.UserClaims;

import java.util.Date;
//import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import javax.crypto.SecretKey;

//import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <br>
 * <p>
 * Classe utilitária para trabalhar com JWT (Jason Web Token).
 * <br>
 * <p>
 * O JWT é um padrão (RFC-7519) de mercado que define como transmitir e
 * armazenar objetos JSON de forma compacta e segura entre diferentes
 * aplicações. Os dados nele contidos podem ser validados a qualquer momento
 * pois o token é assinado digitalmente.
 * <br>
 * <p>
 * Ele é formado por três seções: <strong>Header</strong>,
 * <strong>Payload</strong>
 * e <strong>Signature</strong>.
 * <br>
 * <p>
 * <strong>Header</strong>
 * <p>
 * O Header é um objeto JSON que define informações sobre o tipo do token (typ),
 * nesse caso JWT, e o algorítmo de criptografia usado em sua assinatura (alg),
 * normalmente HMAC SHA256 ou RSA.
 * <br>
 * <p>
 * <strong>Payload</strong>
 * <p>
 * O Payload é um objeto JSON com as Claims (informações) da entidade tratada,
 * normalmente o usuário autenticado. Essas claims podem ser de 3 tipos:
 * <br>
 * <p>
 * <strong>Reserved claims:</strong> atributos não obrigatórios (mas
 * recomendados) que são usados na validação do token pelos protocolos de
 * segurança das APIs.
 * <br>
 * <p>
 * sub (subject) = Entidade à quem o token pertence, normalmente o ID do
 * usuário;
 * <p>
 * iss (issuer) = Emissor do token;
 * <p>
 * exp (expiration) = Timestamp de quando o token irá expirar;
 * <p>
 * iat (issued at) = Timestamp de quando o token foi criado;
 * <p>
 * aud (audience) = Destinatário do token, representa a aplicação que irá
 * usá-lo.
 * <br>
 * <p>
 * Geralmente os atributos mais utilizados são: sub, iss e exp.
 * <br>
 * <p>
 * <strong>Public claims:</strong> atributos que usamos em nossas aplicações.
 * Normalmente armazenamos as informações do usuário autenticado na aplicação.
 * <br>
 * <p>
 * name
 * <p>
 * roles
 * <p>
 * permissions
 * <br>
 * <p>
 * <strong>Private claims:</strong> atributos definidos especialmente para
 * compartilhar informações entre aplicações.
 * <br>
 * <p>
 * <strong>Signature</strong>
 * <p>
 * A assinatura é a concatenação dos hashes gerados a partir do Header e Payload
 * usando base64UrlEncode, com uma chave secreta ou certificado RSA.
 * <br>
 * A Chave Secreta deve respeitar algumas regras, como abaixo:
 * <br>
 * <p>
 * HMAC-SHA</p>
 * <br>
 * <p>
 * JWT HMAC-SHA algoritmos de assinatura HS256, HS384e HS512exigem uma chave
 * secreta que é , pelo menos, tantos bits como a assinatura do algoritmo
 * (digerir) comprimento por RFC 7512 Seção 3.2 . Isso significa:</p>
 * <br>
 * <p>
 * HS256 é HMAC-SHA-256, e que produz digests com 256 bits (32 bytes),
 * portanto, HS256 requer que você use uma chave secreta com pelo menos 32
 * bytes de comprimento.</p>
 * <br>
 * <p>
 * HS384 é HMAC-SHA-384, e que produz digests com 384 bits (48 bytes),
 * portanto, HS384 requer que você use uma chave secreta com pelo menos 48
 * bytes de comprimento.</p>
 * <br>
 * <p>
 * HS512 é HMAC-SHA-512, e isso produz digests com 512 bits (64 bytes),
 * portanto, HS512 requer que você use uma chave secreta com pelo menos 64
 * bytes de comprimento.</p>
 * <br>
 *
 * @author Felipe de Andrade Batista
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtUtil {

    private static final long EXPIRATIONTIME = 3600000L;

    /**
     * This generate a JWT (Json Web Token) for Authentication.
     *
     * @param config is a ConfigAuth object {@link ConfigJwt}.
     * @param claims is a UserAuth object {@link UserClaims}.
     * @param isTimeless is a flag that represents whether the token has
     * expired.
     * @return JWT token String.
     */
    public static String generateToken(ConfigJwt config, UserClaims claims, Boolean isTimeless) {
        SignatureAlgorithm algValue;
        SecretKey key;
        String token = null;
        try {
            algValue = geSignaturetAlgorithm(config.getAlgorithm());
            byte[] keyBytes = DatatypeConverter.parseBase64Binary(config.getSecretKey());
            key = Keys.hmacShaKeyFor(keyBytes);

            final Date iat = new Date();

            JwtBuilder compactJwsBuilder = Jwts.builder()
                    .setHeader(config.getConfigAuthMap())
                    .setSubject(claims.getUserID())
                    .setIssuer(claims.getIssuerName())
                    .setAudience(claims.getAudienceName())
                    .signWith(key, algValue);

            if (isTimeless) {
                compactJwsBuilder.setIssuedAt(iat);
                compactJwsBuilder.setExpiration(new Date(iat.getTime() + EXPIRATIONTIME));
            }

            token = compactJwsBuilder.compact();
        } catch (SignatureException ex) {
            log.error("ERROR: The specified value does not match any"
                    + " SignatureAlgorithm name, in JwtUtil Class.", ex);
        } catch (WeakKeyException ex) {
            log.error("ERROR: The Secret Key byte array length is less than"
                    + " 256 bits (32 bytes), in JwtUtil Class.", ex);
        } catch (InvalidKeyException ex) {
            log.error("ERROR: The HAMAC Key is insufficient or explicitly"
                    + " disallowed by the JWT specification, in JwtUtil Class", ex);
        } catch (Exception ex) {
            log.error("ERROR: An error in Token JWT generate Method in"
                    + " JwtUtil class.", ex);
        }
        return token;
    }

//    public static String decodeToken(String token) {
//        if (token != null) {
//            Claims claims = Jwts
//                    .parser()
//                    .setSigningKey(DatatypeConverter.parseBase64Binary(config.getSecretKey()))
//                    //.parseClaimsJws(token)
//                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
//                    .getBody();
//
//            //return claims.getSubject().equals(AKUMOS_NG) && (claims.getExpiration().after(new Date()));
//            if (claims.getExpiration().after(new Date())) {
//                return claims.getSubject();
//            }
//        }
//        return null;
//    }

    /**
     * Converte a String do Algoritimo de assinatura do Token em Objeto
     * SignatureAlgorithm {@link SignatureAlgorithm}.
     *
     * @param algorithm String que representa o Algoritimo.
     * @return Objeto SignatureAlgorithm.
     */
    private static SignatureAlgorithm geSignaturetAlgorithm(String algorithm)
            throws SignatureException {
        return SignatureAlgorithm.forName(algorithm);
    }
}
