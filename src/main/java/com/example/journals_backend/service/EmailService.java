package com.example.journals_backend.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;



    public void sendEmailVerification(String toEmail, String token) {
        try {
            String subject = "Verify your email address";
            String verificationUrl = "https://gracious-courtesy-production-48b6.up.railway.app/api/verify?token=" + token;

            String htmlContent = """
        <!doctype html>
        <html>
          <body style="margin:0;padding:0;background:#f6f9fc;font-family:Arial,Helvetica,sans-serif;">
            <div style="max-width:600px;margin:0 auto;padding:24px;">
              <div style="background:#ffffff;border-radius:12px;padding:28px;
                          box-shadow:0 6px 18px rgba(0,0,0,0.06);">
                
                <h2 style="margin:0 0 12px;color:#111827;font-size:22px;">
                  Verify your email
                </h2>

                <p style="margin:0 0 16px;color:#374151;font-size:14px;line-height:1.6;">
                  Thank you for registering. Please confirm your email address by clicking the button below.
                </p>

                <div style="margin:22px 0;">
                  <a href="%s"
                     style="display:inline-block;background:#2563eb;color:#ffffff;
                            text-decoration:none;padding:12px 18px;border-radius:10px;
                            font-size:14px;font-weight:600;">
                    Verify Email
                  </a>
                </div>

                <p style="margin:0 0 10px;color:#6b7280;font-size:12px;">
                  If the button doesn’t work, copy and paste this link into your browser:
                </p>

                <p style="margin:0;color:#2563eb;font-size:12px;word-break:break-all;">
                  %s
                </p>

                <hr style="border:none;border-top:1px solid #e5e7eb;margin:22px 0;" />

                <p style="margin:0;color:#9ca3af;font-size:12px;">
                  If you did not create this account, you can safely ignore this email.
                </p>
              </div>

              <p style="margin:14px 0 0;color:#9ca3af;font-size:11px;text-align:center;">
                © %d Your Company. All rights reserved.
              </p>
            </div>
          </body>
        </html>
        """.formatted(
                    verificationUrl,
                    verificationUrl,
                    java.time.Year.now().getValue()
            );

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true = HTML

            javaMailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send verification email", e);
        }
    }

    public void sendArticleUnderReviewEmailHtml(
            String toEmail, String authorName, String articleTitle, String status,String body) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

            helper.setTo(toEmail);
            helper.setFrom("awafa3067@gmail.com"); // must match spring.mail.username
            helper.setSubject("Update on Your Article Submission");

            // Include status dynamically and add submission date placeholder


            helper.setText(body, true); // true = HTML
            javaMailSender.send(message);

            System.out.println("Stylish HTML email with dynamic status sent successfully!");

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }


}
