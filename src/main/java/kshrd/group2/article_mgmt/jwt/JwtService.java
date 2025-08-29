package kshrd.group2.article_mgmt.jwt;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import kshrd.group2.article_mgmt.exception.InvalidJWTException;
import kshrd.group2.article_mgmt.model.entity.AppUser;

@Component
public class JwtService {
  private static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60; // 5 hour
  private static final long REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60; // 7 days
  public static final String ACCESS_SECRET = "FVPr6Q/fVlHGZkElZubC0Zaxv657dPUfDQ4o9DADjSin7+uST1d2A5klMWrMK8fmSl3doyf2wn5zj56VC+qqCg==";
  public static final String REFRESH_SECRET = "GCJO+cUQzm/HaPegk2XG8XqTiPUgo+jcvLFC56sgdzYYTSVAVtgznAKWeDI1LzvoUhG9uIfmUoBU2lqXMOKqXw==";

  private String createToken(Map<String, Object> claim, String subject, long VALIDITY, SecretKey secretKey) {
    return Jwts.builder()
        .claims(claim)
        .subject(subject)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + VALIDITY * 1000))
        .signWith(secretKey).compact();
  }

  private String createAccessToken(Map<String, Object> claim, String subject) {
    return createToken(claim, subject, JWT_TOKEN_VALIDITY, getAccessSignKey());
  }

  private String createRefreshToken(Map<String, Object> claim, String subject) {
    return createToken(claim, subject, REFRESH_TOKEN_VALIDITY, getRefreshSignKey());
  }

  // 1. generate signature key
  private SecretKey getSignKey(String SECRET) {
    byte[] keyBytes = Base64.getDecoder().decode(SECRET);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  private SecretKey getAccessSignKey() {
    return getSignKey(ACCESS_SECRET);
  }

  private SecretKey getRefreshSignKey() {
    return getSignKey(REFRESH_SECRET);
  }

  // 2. generate token for user
  public String generateAccessToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    AppUser appUser = (AppUser) userDetails;
    return createAccessToken(claims, appUser.getEmail());
  }

  public String generateRefreshToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    AppUser appUser = (AppUser) userDetails;
    return createRefreshToken(claims, appUser.getEmail());
  }

  // 3. retrieving any information from token we will need the secret key
  private Claims extractAllClaim(String token, SecretKey signKey) {
    try {
    return Jwts.parser()
        .verifyWith(signKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();
    } catch (JwtException | IllegalArgumentException e) {
      throw new InvalidJWTException("Invalid JWT Token");
    }
  }

  // 4. extract a specific claim from the JWT token’s claims.
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver, SecretKey signKey) {
    final Claims claims = extractAllClaim(token, signKey);
    return claimsResolver.apply(claims);
  }

  // 5. retrieve username from jwt token
  public String extractAccessEmail(String token) {
    return extractClaim(token, Claims::getSubject, getAccessSignKey());
  }

  public String extractRefreshEmail(String token) {
    return extractClaim(token, Claims::getSubject, getRefreshSignKey());
  }

  // 6. retrieve expiration date from jwt token
  public Date extractAccessExpirationDate(String token) {
    return extractClaim(token, Claims::getExpiration, getAccessSignKey());
  }

  public Date extractRefreshExpirationDate(String token) {
    return extractClaim(token, Claims::getExpiration, getRefreshSignKey());
  }

  // 7. check expired token
  private Boolean isAccessTokenExpired(String token) {
    return extractAccessExpirationDate(token).before(new Date());
  }

  private Boolean isRefreshTokenExpired(String token) {
    return extractRefreshExpirationDate(token).before(new Date());
  }

  // 8. validate token
  public Boolean validateAccessToken(String token, UserDetails userDetails) {
    final String email = extractAccessEmail(token);
    return (email.equals(userDetails.getUsername()) && !isAccessTokenExpired(token));
  }

  public Boolean validateRefreshToken(String token, UserDetails userDetails) {
    final String email = extractRefreshEmail(token);
    return (email.equals(userDetails.getUsername()) && !isRefreshTokenExpired(token));
  }
}