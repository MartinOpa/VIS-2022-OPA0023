package domain_layer;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class LoginValidation {
    public LoginValidation(){}
    
    private static String encrypt(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            return hashtext;
        }

        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    
    //login
    public boolean checkCredentials(String loginInput, String passwordInput) {
        for (String element : getData()) {
            int space = element.indexOf(" ");
            int divider = element.indexOf("|");
            int ID = Integer.parseInt(element.substring(0, divider));
            String login = element.substring(divider+1, space);
            String password = element.substring(space+1, element.length());
            
            if (login.equals(loginInput) && password.equals(encrypt(passwordInput))) {
                ClientHolder holder = ClientHolder.getInstance();
                holder.setClient(new Client(ID, login, null, null, null, 0));
                holder.setClient(holder.getClient().getClientDB().load(holder.getClient()));
                return true;
            }
        }
        
        return false;
    }
    
    private List<String> getData() {
        Path p = Paths.get("./src/main/resources/users.txt");
        List<String> result = new ArrayList<String>();
        
        try (InputStream in = Files.newInputStream(p);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                result.add(line);
            }
            in.close();
        } catch (IOException x) {
            System.err.println(x);
        }
        
        return result;
    }
    
    public int checkDuplicateLogin(String loginInput) {
        int ID = 0;
        for (String element : getData()) {
            int space = element.indexOf(" ");
            int divider = element.indexOf("|");
            
            String existinglogin = element.substring(divider+1, space);
            
            if (existinglogin.equals(loginInput)) {
                ID = 0;
                return -1;
            }
            ID += 1;
        }
        
        return ID;
    }
    
    public void storeUser(int ID, String login, String password) {
        String p = "./src/main/resources/users.txt";
        try {
            FileWriter out = new FileWriter(p, true);
            out.write("\n" + Integer.toString(ID) + "|" + login + " " + encrypt(password));
            out.close();           
        } catch (IOException x) {
            System.err.println(x);
        }
    }
    
    //reset
    public boolean tryPasswordReset(String loginInput, String newpassword) {
        int ID = -1;
        int saveID = -1;
        String login = "";
        boolean success = false;
        Path p = Paths.get("./src/main/resources/users.txt");
        List<String> result = new ArrayList<String>();
        
        try (InputStream in = Files.newInputStream(p);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                int space = line.indexOf(" ");
                int divider = line.indexOf("|");
                ID = Integer.parseInt(line.substring(0, divider));
                login = line.substring(divider+1, space);                
                if (login.equals(loginInput)) {
                    saveID = ID;
                    success = true;
                } else result.add(line);
            }
            in.close();
        } catch (IOException x) {
            System.err.println(x);
        }
        
        if (!success) {
            return false;
        }

        String write = "./src/main/resources/users.txt";
        
        try {
            FileWriter out = new FileWriter(write);
            out.write("");
            out.close(); 

            out = new FileWriter(write, true);
            for (String element : result) {
                out.write(element + "\n");
            }
            out.write(Integer.toString(saveID) + "|" + loginInput + " " + encrypt(newpassword)); 
            out.close();           
        } catch (IOException x) {
            System.err.println(x);
        }
        
        return true;
    }
}
