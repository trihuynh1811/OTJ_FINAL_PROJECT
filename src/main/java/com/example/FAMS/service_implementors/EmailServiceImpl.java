package com.example.FAMS.service_implementors;

import com.example.FAMS.models.EmailDetails;
import com.example.FAMS.services.EmailService;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.chrono.MinguoChronology;

@Service
public class EmailServiceImpl implements EmailService {
    private final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;


    MimeMessage mimeMessage;

    @Override
    public String sendMail(EmailDetails details) {
        try {
            mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper;

            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(details.getRecipient());
            mimeMessageHelper.setSubject(details.getSubject());
            String message =
                    "<body data-new-gr-c-s-loaded=\"14.1130.0\">\n" +
                    "    <div class=\"es-wrapper-color\">\n" +
                    "        <!--[if gte mso 9]>\n" +
                    "\t\t\t<v:background xmlns:v=\"urn:schemas-microsoft-com:vml\" fill=\"t\">\n" +
                    "\t\t\t\t<v:fill type=\"tile\" color=\"#eff7f6\"></v:fill>\n" +
                    "\t\t\t</v:background>\n" +
                    "\t\t<![endif]-->\n" +
                    "        <table class=\"es-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                    "            <tbody>\n" +
                    "                <tr>\n" +
                    "                    <td class=\"esd-email-paddings\" valign=\"top\">\n" +
                    "                        <table class=\"es-content esd-header-popover\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">\n" +
                    "                            <tbody>\n" +
                    "                                <tr>\n" +
                    "                                    <td class=\"esd-stripe\" align=\"center\">\n" +
                    "                                        <table class=\"es-content-body\" style=\"background-color: #ffffff;\" width=\"600\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#ffffff\" align=\"center\">\n" +
                    "                                            <tbody>\n" +
                    "                                                <tr>\n" +
                    "                                                    <td class=\"esd-structure\" align=\"left\" esd-custom-block-id=\"794034\">\n" +
                    "                                                        <table cellspacing=\"0\" cellpadding=\"0\" width=\"100%\">\n" +
                    "                                                            <tbody>\n" +
                    "                                                                <tr>\n" +
                    "                                                                    <td class=\"es-m-p0r esd-container-frame\" width=\"600\" valign=\"top\" align=\"center\">\n" +
                    "                                                                        <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                    "                                                                            <tbody>\n" +
                    "                                                                                <tr>\n" +
                    "                                                                                    <td align=\"center\" class=\"esd-block-banner\" style=\"position: relative;\" esdev-config=\"h12\"><a target=\"_blank\"><img class=\"adapt-img esdev-stretch-width esdev-banner-rendered\" src=\"https://demo.stripocdn.email/content/guids/bannerImgGuid/images/image16964898618416019.png\" alt title width=\"600\" style=\"display: block;\"></a></td>\n" +
                    "                                                                                </tr>\n" +
                    "                                                                            </tbody>\n" +
                    "                                                                        </table>\n" +
                    "                                                                    </td>\n" +
                    "                                                                </tr>\n" +
                    "                                                            </tbody>\n" +
                    "                                                        </table>\n" +
                    "                                                    </td>\n" +
                    "                                                </tr>\n" +
                    "                                                <tr>\n" +
                    "                                                    <td class=\"esd-structure es-p30t es-p30b es-p20r es-p20l\" align=\"left\" esd-custom-block-id=\"794035\" bgcolor=\"#6a994e\" style=\"background-color: #6a994e;\">\n" +
                    "                                                        <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                    "                                                            <tbody>\n" +
                    "                                                                <tr>\n" +
                    "                                                                    <td width=\"560\" class=\"esd-container-frame\" align=\"center\" valign=\"top\">\n" +
                    "                                                                        <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                    "                                                                            <tbody>\n" +
                    "                                                                                <tr>\n" +
                    "                                                                                    <td align=\"center\" class=\"esd-block-text es-p10 es-m-txt-c\">\n" +
                    "                                                                                        <h3 style=\"color: #ffffff;\">Password:&nbsp;" + details.getMsgBody() + "</h3>\n" +
                    "                                                                                    </td>\n" +
                    "                                                                                </tr>\n" +
                    "                                                                                <tr>\n" +
                    "                                                                                    <td align=\"center\" class=\"esd-block-text es-m-txt-c es-p20t\">\n" +
                    "                                                                                        <p style=\"color: #ffffff;\">Using your password to login to our FAMS Application</p>\n" +
                    "                                                                                    </td>\n" +
                    "                                                                                </tr>\n" +
                    "                                                                            </tbody>\n" +
                    "                                                                        </table>\n" +
                    "                                                                    </td>\n" +
                    "                                                                </tr>\n" +
                    "                                                            </tbody>\n" +
                    "                                                        </table>\n" +
                    "                                                    </td>\n" +
                    "                                                </tr>\n" +
                    "                                            </tbody>\n" +
                    "                                        </table>\n" +
                    "                                    </td>\n" +
                    "                                </tr>\n" +
                    "                            </tbody>\n" +
                    "                        </table>\n" +
                    "                        <table cellpadding=\"0\" cellspacing=\"0\" class=\"es-content\" align=\"center\">\n" +
                    "                            <tbody>\n" +
                    "                                <tr>\n" +
                    "                                    <td class=\"esd-stripe\" align=\"center\">\n" +
                    "                                        <table bgcolor=\"#ffffff\" class=\"es-content-body\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\">\n" +
                    "                                            <tbody>\n" +
                    "                                                <tr>\n" +
                    "                                                    <td class=\"esd-structure es-p20r es-p20l\" align=\"left\">\n" +
                    "                                                        <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                    "                                                            <tbody>\n" +
                    "                                                                <tr>\n" +
                    "                                                                    <td width=\"560\" class=\"esd-container-frame\" align=\"center\" valign=\"top\">\n" +
                    "                                                                        <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                    "                                                                            <tbody>\n" +
                    "                                                                                <tr>\n" +
                    "                                                                                    <td align=\"center\" class=\"esd-block-spacer es-p5t es-p5b\" style=\"font-size:0\">\n" +
                    "                                                                                        <table border=\"0\" width=\"100%\" height=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                    "                                                                                            <tbody>\n" +
                    "                                                                                                <tr>\n" +
                    "                                                                                                    <td style=\"border-bottom: 5px dotted #a7c957; background: unset; height: 1px; width: 100%; margin: 0px;\"></td>\n" +
                    "                                                                                                </tr>\n" +
                    "                                                                                            </tbody>\n" +
                    "                                                                                        </table>\n" +
                    "                                                                                    </td>\n" +
                    "                                                                                </tr>\n" +
                    "                                                                            </tbody>\n" +
                    "                                                                        </table>\n" +
                    "                                                                    </td>\n" +
                    "                                                                </tr>\n" +
                    "                                                            </tbody>\n" +
                    "                                                        </table>\n" +
                    "                                                    </td>\n" +
                    "                                                </tr>\n" +
                    "                                                <tr>\n" +
                    "                                                    <td class=\"esd-structure es-p40b es-p20r es-p20l\" align=\"left\">\n" +
                    "                                                        <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                    "                                                            <tbody>\n" +
                    "                                                                <tr>\n" +
                    "                                                                    <td width=\"560\" class=\"esd-container-frame\" align=\"center\" valign=\"top\">\n" +
                    "                                                                        <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                    "                                                                            <tbody>\n" +
                    "                                                                                <tr>\n" +
                    "                                                                                    <td align=\"center\" class=\"esd-block-spacer es-p5t es-p5b\" style=\"font-size:0\">\n" +
                    "                                                                                        <table border=\"0\" width=\"100%\" height=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                    "                                                                                            <tbody>\n" +
                    "                                                                                                <tr>\n" +
                    "                                                                                                    <td style=\"border-bottom: 5px dotted #a7c957; background: unset; height: 1px; width: 100%; margin: 0px;\"></td>\n" +
                    "                                                                                                </tr>\n" +
                    "                                                                                            </tbody>\n" +
                    "                                                                                        </table>\n" +
                    "                                                                                    </td>\n" +
                    "                                                                                </tr>\n" +
                    "                                                                            </tbody>\n" +
                    "                                                                        </table>\n" +
                    "                                                                    </td>\n" +
                    "                                                                </tr>\n" +
                    "                                                            </tbody>\n" +
                    "                                                        </table>\n" +
                    "                                                    </td>\n" +
                    "                                                </tr>\n" +
                    "                                            </tbody>\n" +
                    "                                        </table>\n" +
                    "                                    </td>\n" +
                    "                                </tr>\n" +
                    "                            </tbody>\n" +
                    "                        </table>\n" +
                    "                        <table cellpadding=\"0\" cellspacing=\"0\" class=\"es-footer esd-footer-popover\" align=\"center\">\n" +
                    "                            <tbody>\n" +
                    "                                <tr>\n" +
                    "                                    <td class=\"esd-stripe\" align=\"center\" esd-custom-block-id=\"794054\">\n" +
                    "                                        <table bgcolor=\"#ffffff\" class=\"es-footer-body\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\">\n" +
                    "                                            <tbody>\n" +
                    "                                                <tr>\n" +
                    "                                                    <td class=\"esd-structure\" align=\"left\">\n" +
                    "                                                        <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                    "                                                            <tbody>\n" +
                    "                                                                <tr>\n" +
                    "                                                                    <td width=\"600\" class=\"esd-container-frame\" align=\"center\" valign=\"top\">\n" +
                    "                                                                        <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                    "                                                                            <tbody>\n" +
                    "                                                                                <tr>\n" +
                    "                                                                                    <td align=\"center\" class=\"esd-block-spacer es-p5t es-p5b\" style=\"font-size:0\">\n" +
                    "                                                                                        <table border=\"0\" width=\"100%\" height=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                    "                                                                                            <tbody>\n" +
                    "                                                                                                <tr>\n" +
                    "                                                                                                    <td style=\"border-bottom: 2px solid #eff7f6; background: unset; height: 1px; width: 100%; margin: 0px;\"></td>\n" +
                    "                                                                                                </tr>\n" +
                    "                                                                                            </tbody>\n" +
                    "                                                                                        </table>\n" +
                    "                                                                                    </td>\n" +
                    "                                                                                </tr>\n" +
                    "                                                                            </tbody>\n" +
                    "                                                                        </table>\n" +
                    "                                                                    </td>\n" +
                    "                                                                </tr>\n" +
                    "                                                            </tbody>\n" +
                    "                                                        </table>\n" +
                    "                                                    </td>\n" +
                    "                                                </tr>\n" +
                    "                                            </tbody>\n" +
                    "                                        </table>\n" +
                    "                                    </td>\n" +
                    "                                </tr>\n" +
                    "                            </tbody>\n" +
                    "                        </table>\n" +
                    "                    </td>\n" +
                    "                </tr>\n" +
                    "            </tbody>\n" +
                    "        </table>\n" +
                    "    </div>\n" +
                    "</body>";

            mimeMessage.setContent(message, "text/html; charset=utf-8");

            javaMailSender.send(mimeMessage);

            return "Mail sent successfully";
        } catch (Exception e) {
            return "Mail sent fail";
        }
    }


}
