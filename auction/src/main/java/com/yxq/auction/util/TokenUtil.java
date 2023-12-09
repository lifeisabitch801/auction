package com.yxq.auction.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
@Component
public class TokenUtil  {
    private static final Logger logger = LoggerFactory.getLogger(TokenUtil.class);
    /**
     * 过期时间
     */
    private static String accessTokenExpireTime;
    /**
     * JWT认证加密私钥(Base64加密)
     */
    private static String encryptJWTKey;

    /**
     * 解决@Value不能修饰static的问题
     */
    @Value("${accessTokenExpireTime}")
    public void setAccessTokenExpireTime(String accessTokenExpireTime) {
        TokenUtil.accessTokenExpireTime = accessTokenExpireTime;
    }

    @Value("${encryptJWTKey}")
    public void setEncryptJWTKey(String encryptJWTKey) {
        TokenUtil.encryptJWTKey = encryptJWTKey;
    }

    public static String encode(String str) throws Exception {
        byte[] encodeBytes = Base64.getEncoder().encode(str.getBytes("utf-8"));
        return new String(encodeBytes);
    }
    public static String decode(String str) throws Exception {
        byte[] decodeBytes = Base64.getDecoder().decode(str.getBytes("utf-8"));
        return new String(decodeBytes);
    }

    /**
     * 生成口令
     * @param loginname 登录名
     * @return String 口令
     */
    public static String sign(String loginname) throws Exception {
        try {
            // 使用私钥进行加密
            String secret = loginname + encode(encryptJWTKey);
            // 设置过期时间：根据当前时间计算出过期时间。此处过期时间是以毫秒为单位，所以乘以1000。
            Date date = new Date(System.currentTimeMillis() + Long.parseLong(accessTokenExpireTime) * 1000);
            // 对私钥进行再次加密
            Algorithm algorithm = Algorithm.HMAC256(secret);
            // 生成token 附带loginname信息
            String token = JWT.create().withClaim("loginname", loginname).withClaim("currentTimeMillis", System.currentTimeMillis())
                    .withExpiresAt(date).sign(algorithm);
            return token;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("JWTToken加密出现异常:" + e.getMessage());
            throw new Exception("JWTToken加密出现异常:" + e.getMessage());
        }

    }

    /**
     * 检验token是否有效
     * @param token 口令
     * @return boolean有效为true
     */
    public static boolean verify(String token) {
        try {
            // 通过token获取登录名
            String secret = getClaim(token, "loginname") + encode(encryptJWTKey);
            // 进行二次加密
            Algorithm algorithm = Algorithm.HMAC256(secret);
            // 使用JWTVerifier进行验证解密
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("JWTToken认证解密出现UnsupportedEncodingException异常:" + e.getMessage());
            return false;
        }
    }

    /**
     * 获取token中的信息就是withClaim中设置的值
     * @param token 口令
     * @param claim sign()方法中withClaim设置的key
     * @return String 值
     */
    public static String getClaim(String token, String claim) throws Exception {
        try {
            // 对token进行解码获得解码后的jwt
            DecodedJWT jwt = JWT.decode(token);
            // 获取到指定的claim,如果是其他类型返回null
            return jwt.getClaim(claim).asString();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("解密Token中的公共信息出现异常:" + e.getMessage());
            throw new Exception("解密Token中的公共信息出现异常:" + e.getMessage());
        }
    }


}

