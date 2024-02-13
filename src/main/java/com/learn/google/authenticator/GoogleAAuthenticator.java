package com.learn.google.authenticator;

import com.google.zxing.WriterException;

import java.io.IOException;
import java.util.Scanner;

import static com.learn.google.authenticator.Utils.getTOTPCode;

public class GoogleAAuthenticator {
    public static void main(String[] args) throws IOException, WriterException {
        String secretKey = "QDWSM3OYBPGTEVSPB5FKVDM3CSNCWHVK";
        String email = "test-2fa@yopmail.com";
        String companyName = "Test 2FA Company";
        String barCodeUrl = Utils.getGoogleAuthenticatorBarCode(secretKey, email, companyName);
        System.out.println(barCodeUrl);
        Utils.createQRCode(barCodeUrl, "qrcode/QRCode.png", 400, 400);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter 2FA Code for this account " + email + ":");
        String code = scanner.nextLine();
        if (code.equals(getTOTPCode(secretKey))) {
            System.out.println("Logged in successfully");
        } else {
            System.out.println("Invalid 2FA Code");
        }

    }

}
