package com.example.demo.controller;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
// import javax.naming.directory.DirContext;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.User;
import com.example.demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;

@RestController
public class LoginController {

    @Value("${jwt.key}")
    private String jwtKey;

    @Autowired
    private LdapTemplate ldapTemplate;

    @Autowired
    private UserService userService;

    /**
     * Login Controller
     * @param username
     * @param password
     * @return HTTP Entity with jwt or 401 if failed
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> login(
        // @RequestParam(value = "username", required = false) String username,
        // @RequestParam(value = "password", required = false) String password,
        @RequestBody JSONObject inputJson
    ){
        // if(username == null && password == null){
            String username = (String) inputJson.get("username");
            String password = (String) inputJson.get("password");
        // }
        JSONObject output = new JSONObject();
        String userDn = username + "@AD.ESHF-IT.COM";
        LocalDateTime tokenExpired = LocalDateTime.now().plusDays(7);
        // DirContext ctx = null;
        try {
            // ctx = 
            ldapTemplate.getContextSource().getContext(userDn, password);
            User user = ldapTemplate.search(query().where("objectclass").is("person").and("sAMAccountName").is(username), new UserAttributesMapper()).get(0);

            user = userService.checkUserAfterLogin(user);
            if(user.getId() == null){
                throw new Exception("User ID not found");
            }
            String jwtToken = Jwts.builder()
                .setSubject(user.getName())
                .setId(Long.toString(user.getId()))
                .setExpiration(Date.from(tokenExpired.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(new SecretKeySpec(jwtKey.getBytes(), "HmacSHA256")).compact();
            output.put("status", "success");
            output.put("token", jwtToken);
            output.put("displayName", user.getName());
            output.put("user", user);
            output.put("tokenExpired", tokenExpired.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            return new ResponseEntity<JSONObject>(output, HttpStatus.OK);
        } catch (Exception e){
            output.put("status", "error");
            output.put("status", "UNAUTHORIZED");
            output.put("debug", e.toString());
            return new ResponseEntity<JSONObject>(output, HttpStatus.UNAUTHORIZED);
        }
    }

    private class UserAttributesMapper implements AttributesMapper<User> {
        @Override
        public User mapFromAttributes(Attributes arg0) throws NamingException {
            User user = new User();
            user.setUsername((String) arg0.get("sAMAccountName").get());
            user.setName((String) arg0.get("displayName").get());
            user.setEmail((String) arg0.get("userPrincipalName").get());
            user.setObjectguid(this.ByteArr2UUIDString((byte[]) arg0.get("ObjectGUID").get()));
            return user;
        }
        
        private String ByteArr2UUIDString(byte[] guidBytes){
            if (guidBytes.length == 16) {
                // Convert encoded AD objectGUID to UUID
                // objectGUID is not storing bits sequentially, so do the dance
                return UUID.fromString(
                    String.format("%02x%02x%02x%02x-%02x%02x-%02x%02x-%02x%02x-%02x%02x%02x%02x%02x%02x", 
                    guidBytes[3] & 255, 
                    guidBytes[2] & 255, 
                    guidBytes[1] & 255, 
                    guidBytes[0] & 255, 
                    guidBytes[5] & 255, 
                    guidBytes[4] & 255, 
                    guidBytes[7] & 255, 
                    guidBytes[6] & 255, 
                    guidBytes[8] & 255, 
                    guidBytes[9] & 255, 
                    guidBytes[10] & 255, 
                    guidBytes[11] & 255, 
                    guidBytes[12] & 255, 
                    guidBytes[13] & 255, 
                    guidBytes[14] & 255, 
                    guidBytes[15] & 255)).toString();
            }
            return null;
        }
    }
    
}
