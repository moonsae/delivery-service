package org.delivery.api.domain.token.helper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.Valid;
import org.antlr.v4.runtime.Token;
import org.delivery.api.common.error.ErrorCode;
import org.delivery.api.common.error.TokenErrorCode;
import org.delivery.api.common.exception.ApiException;
import org.delivery.api.domain.token.ifs.TokenHelperIfs;
import org.delivery.api.domain.token.model.TokenDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenHelper implements TokenHelperIfs {

    @Value("${token.secret.key}")
    private String secretKey;

    @Value("${token.access-token.plus-hour}")
    private Long accessTokenPlusHour;

    @Value("${token.refresh-token.plus-hour}")
    private Long refreshTokenPlusHour;
    @Override
    public TokenDto issueAccessToken(Map<String, Object> data) {
        var expiredLocalDataTime = LocalDateTime.now().plusHours(accessTokenPlusHour);

        var expriedAt = Date.from(
                expiredLocalDataTime.atZone(
                        ZoneId.systemDefault()
                ).toInstant()
        );
        var key = Keys.hmacShaKeyFor(secretKey.getBytes());

        var jwtToken = Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setClaims(data)
                .setExpiration(expriedAt)
                .compact();
        return TokenDto.builder()
                .token(jwtToken)
                .expiredAt(expiredLocalDataTime)
                .build()
                ;
    }

    @Override
    public TokenDto issueRefreshToken(Map<String, Object> data) {
        var expiredLocalDataTime = LocalDateTime.now().plusHours(refreshTokenPlusHour);

        var expriedAt = Date.from(
                expiredLocalDataTime.atZone(
                        ZoneId.systemDefault()
                ).toInstant()
        );
        var key = Keys.hmacShaKeyFor(secretKey.getBytes());

        var jwtToken = Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setClaims(data)
                .setExpiration(expriedAt)
                .compact();
        return TokenDto.builder()
                .token(jwtToken)
                .expiredAt(expiredLocalDataTime)
                .build()
                ;
    }

    @Override
    public Map<String, Object> validationTokenWithThrow(String token) {

        var key = Keys.hmacShaKeyFor(secretKey.getBytes());
        var parser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                ;
        try {
            var result = parser.parseClaimsJws(token);
            return new HashMap<String, Object>(result.getBody());

        }catch (Exception e){
            if(e instanceof ExpiredJwtException){
                //토큰이 유효하지 않을 때
                throw new ApiException(TokenErrorCode.INVALID_TOKEN,e);
            }
            else if(e instanceof ExpiredJwtException){
                //만료된 토큰
                throw new ApiException(TokenErrorCode.EXPIRED_TOKEN,e);
            }
            else{
                //그외 에러
                throw new ApiException(TokenErrorCode.TOKEN_EXCEPTION,e);
            }
        }

    }
}
