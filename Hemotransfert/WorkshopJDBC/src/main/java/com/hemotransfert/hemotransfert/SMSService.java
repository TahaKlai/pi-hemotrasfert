package com.hemotransfert.hemotransfert;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SMSService {

    // Find your Account Sid and Token at twilio.com/console
    public static final String ACCOUNT_SID = "your_account_sid";
    public static final String AUTH_TOKEN = "your_auth_token";

    public void sendSMS(String to, String from, String message) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message.creator(new PhoneNumber(to), // to
                        new PhoneNumber(from), // from
                        message)
                .create();
    }
}