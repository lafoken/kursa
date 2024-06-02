package com.inkronsane.ReadArticlesServer.service;

import jakarta.mail.internet.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.mail.javamail.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class EmailSenderServiceTest {

   @InjectMocks
   private EmailSenderService emailSenderService;

   @Mock
   private JavaMailSender mailSender;

   @BeforeEach
   void setUp() {
      MockitoAnnotations.initMocks(this);
   }

   @Test
   void sendRegistrationEmail_ValidInput_SuccessfullySent() {

      String toEmail = "mail@example.com";
      int code = 123456;

      emailSenderService.sendRegistrationEmail(toEmail, code);

      ArgumentCaptor<MimeMessage> argumentCaptor = ArgumentCaptor.forClass(MimeMessage.class);

      try {
         verify(mailSender, times(1)).send(argumentCaptor.capture());
         MimeMessage sentMessage = argumentCaptor.getValue();
         Assertions.assertEquals(toEmail, sentMessage.getAllRecipients()[0].toString());
         Assertions.assertEquals("Registration confirmation", sentMessage.getSubject());
      } catch (Exception e) {
         Assertions.fail("Exception occurred: " + e.getMessage());
      }
   }
}