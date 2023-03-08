package com.malakhov.journalofpractice.security.jwt;

import com.malakhov.journalofpractice.exception.JwtAuthenticationException;
import com.malakhov.journalofpractice.security.services.UserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

import static org.springframework.util.StringUtils.hasText;

@Component
//@Log todo
public class JwtUtils {

    //private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationInMs;

    @Value("${app.jwtHeader}")
    private String jwtHeader;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    public String generateJwtToken(Authentication authentication) {

        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader(jwtHeader);
        if(hasText(header) && header.startsWith("Bearer")) {
            return header.substring(7);
        }
        return null;
    }

    public boolean validateJwtToken(String jwtToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwtToken);
            return true;
        } catch (SignatureException e) {
            //logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            //logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            //logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            //logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            //logger.error("JWT claims string is empty: {}", e.getMessage());
        } catch (JwtException e) {
            throw new JwtAuthenticationException("JWT token is expired or invalid");
        }
        return false;
    }

    public String getEmailFromToken(String jwtToken) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwtToken).getBody().getSubject();
    }

    public Authentication getAuthentication(String jwtToken) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getEmailFromToken(jwtToken));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
