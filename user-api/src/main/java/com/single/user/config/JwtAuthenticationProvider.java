package com.single.user.config;

import com.single.user.util.Aes256Util;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtAuthenticationProvider {
    private String secretKey = "akldgjekrlgjewrgjidfvjlzkcvadsfqewgqergwgvewrkjgkldsfjgkljewrigjwerogjiewgjiewrogjsdklvjwei";

    private long tokenValidTime = 1000L * 60 * 60 * 24;

    public String createToken(String userId) {
        Claims claims = Jwts.claims().setSubject(Aes256Util.encrypt(userId));

        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public boolean validateToken(String jwtToken) {
        Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(jwtToken);

        return !claimsJws.getBody().getExpiration().before(new Date());
    }

    public String getUserPk(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();

        return Aes256Util.decrypt(claims.getSubject());
    }
}
