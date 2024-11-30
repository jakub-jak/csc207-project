package data_access;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;
import org.apache.commons.codec.binary.Base64;
import use_case.share_article.ShareArticleEmailDataAccessInterface;


import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Properties;
import java.util.Set;

public class EmailDataAccessObject implements ShareArticleEmailDataAccessInterface {

    private static final String APP_EMAIL = "newsbuddyapp@gmail.com";
    private final Gmail service;

    public EmailDataAccessObject() throws Exception {

        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        service = new Gmail.Builder(httpTransport, jsonFactory, getCredentials(httpTransport, jsonFactory))
                .setApplicationName("CSC207Project")
                .build();

    }


    private static Credential getCredentials(final NetHttpTransport httpTransport, GsonFactory jsonFactory)
            throws IOException {
        // Load client secrets.

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(EmailDataAccessObject.class.getResourceAsStream("/client_secret_517070072065-qeqlgusgdo22dad8q7uicnaat4pd35m8.apps.googleusercontent.com.json")));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, clientSecrets, Set.of(GmailScopes.GMAIL_SEND))
                .setDataStoreFactory(new FileDataStoreFactory(Paths.get("tokens").toFile()))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        //returns an authorized Credential object.
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

    }

    public void sendMail(String subject, String message, String rec_email) throws Exception {

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(APP_EMAIL));
        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(rec_email));
        email.setSubject(subject);
        email.setContent(message, "text/html");

        //email.setText(message);

        // Encode and wrap the MIME message into a gmail message
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
        Message msg = new Message();
        msg.setRaw(encodedEmail);

        try {
            // Create send message
            msg = service.users().messages().send("me", msg).execute();
            System.out.println("Message id: " + msg.getId());
            System.out.println(msg.toPrettyString());
        } catch (GoogleJsonResponseException e) {
            GoogleJsonError error = e.getDetails();
            if (error.getCode() == 403) {
                System.err.println("Unable to send message: " + e.getDetails());
            } else {
                throw e;
            }
        }

    }

    public static void main(String[] args) throws Exception {
        String emailBody = """
        <!DOCTYPE html>
        <html>
        <head>
            <style>
                body {
                    font-family: Arial, sans-serif;
                    line-height: 1.6;
                    color: #333;
                }
                .email-container {
                    max-width: 600px;
                    margin: 0 auto;
                    padding: 20px;
                    border: 1px solid #ddd;
                    border-radius: 8px;
                    background-color: #f9f9f9;
                }
                .article-title {
                    font-size: 1.5em;
                    font-weight: bold;
                    margin-bottom: 10px;
                }
                .article-date {
                    font-size: 0.9em;
                    color: #777;
                    margin-bottom: 20px;
                }
                .article-description {
                    font-size: 1em;
                    margin-bottom: 20px;
                }
                .article-author {
                    font-size: 0.9em;
                    font-style: italic;
                    margin-bottom: 20px;
                }
                .article-link {
                    display: inline-block;
                    margin-top: 10px;
                    padding: 10px 20px;
                    font-size: 1em;
                    color: #fff;
                    background-color: #007BFF;
                    text-decoration: none;
                    border-radius: 4px;
                }
                .article-link:hover {
                    background-color: #0056b3;
                }
            </style>
        </head>
        <body>
            <div class="email-container">
                <div class="article-title">The Amazing World of HTML Emails</div>
                <div class="article-date">Published on: November 27, 2024</div>
                <div class="article-description">
                    In this article, we explore the intricacies of crafting engaging and professional HTML email templates. Learn the best practices to captivate your audience.
                </div>
                <div class="article-author">Written by: Hriday Chhaochharia</div>
                <a href="https://example.com/html-emails" class="article-link">Read the Full Article</a>
            </div>
        </body>
        </html>
        """;

        new EmailDataAccessObject().sendMail("A new message", emailBody, "hriday.chhaochharia33@gmail.com");

    }


}

