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
            String message = "<body data-new-gr-c-s-loaded=\"14.1130.0\">\n" +
                    "    <div class=\"es-wrapper-color\">\n" +
                    "        <!--[if gte mso 9]>\n" +
                    "\t\t\t<v:background xmlns:v=\"urn:schemas-microsoft-com:vml\" fill=\"t\">\n" +
                    "\t\t\t\t<v:fill type=\"tile\" color=\"#4F6926\"></v:fill>\n" +
                    "\t\t\t</v:background>\n" +
                    "\t\t<![endif]-->\n" +
                    "        <table class=\"es-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                    "            <tbody>\n" +
                    "                <tr>\n" +
                    "                    <td class=\"esd-email-paddings\" valign=\"top\">\n" +
                    "                        <table class=\"esd-header-popover es-header\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">\n" +
                    "                            <tbody>\n" +
                    "                                <tr>\n" +
                    "                                    <td class=\"esd-stripe\" align=\"center\">\n" +
                    "                                        <table class=\"es-header-body\" style=\"background-color: #ffffff;\" width=\"600\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#ffffff\" align=\"center\">\n" +
                    "                                            <tbody>\n" +
                    "                                                <tr>\n" +
                    "                                                    <td class=\"esd-structure\" align=\"left\">\n" +
                    "                                                        <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                    "                                                            <tbody>\n" +
                    "                                                                <tr>\n" +
                    "                                                                    <td class=\"es-m-p0r esd-container-frame\" width=\"600\" valign=\"top\" align=\"center\">\n" +
                    "                                                                        <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                    "                                                                            <tbody>\n" +
                    "                                                                                <tr>\n" +
                    "                                                                                    <td align=\"center\" class=\"esd-block-banner\" style=\"position: relative;\" esdev-config=\"h5\"><a target=\"_blank\" href=\"https://viewstripo.email\"><img class=\"adapt-img esdev-stretch-width esdev-banner-rendered\" src=\"https://tlr.stripocdn.email/content/guids/bannerImgGuid/images/image1663309729725341.png\" alt=\"Happy Halloween\" title=\"Happy Halloween\" width=\"100%\"></a></td>\n" +
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
                    "                        <table cellpadding=\"0\" cellspacing=\"0\" class=\"es-header\" align=\"center\">\n" +
                    "                            <tbody>\n" +
                    "                                <tr>\n" +
                    "                                    <td class=\"esd-stripe\" align=\"center\">\n" +
                    "                                        <table bgcolor=\"rgba(0, 0, 0, 0)\" class=\"es-header-body\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\">\n" +
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
                    "                                                                                    <td align=\"center\" class=\"esd-block-image\" style=\"font-size: 0px;\"><a target=\"_blank\"><img class=\"adapt-img\" src=\"https://tlr.stripocdn.email/content/guids/CABINET_1c1ce83354239d17871322fbf9106775/images/mask_group_9iA.png\" alt style=\"display: block;\" width=\"600\"></a></td>\n" +
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
                    "                        <table cellpadding=\"0\" cellspacing=\"0\" class=\"es-content esd-footer-popover\" align=\"center\">\n" +
                    "                            <tbody>\n" +
                    "                                <tr>\n" +
                    "                                    <td class=\"esd-stripe\" align=\"center\">\n" +
                    "                                        <table bgcolor=\"#FFF7A2\" class=\"es-content-body\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" style=\"background-image: url(https://tlr.stripocdn.email/content/guids/CABINET_1c1ce83354239d17871322fbf9106775/images/mask_group_yjx.png); background-repeat: no-repeat; background-position: left center;\" background=\"https://tlr.stripocdn.email/content/guids/CABINET_1c1ce83354239d17871322fbf9106775/images/mask_group_yjx.png\">\n" +
                    "                                            <tbody>\n" +
                    "                                                <tr>\n" +
                    "                                                    <td class=\"esd-structure es-p20t es-p40b es-p40r es-p40l\" align=\"left\">\n" +
                    "                                                        <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                    "                                                            <tbody>\n" +
                    "                                                                <tr>\n" +
                    "                                                                    <td width=\"520\" class=\"esd-container-frame\" align=\"center\" valign=\"top\">\n" +
                    "                                                                        <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                    "                                                                            <tbody>\n" +
                    "                                                                                <tr>\n" +
                    "                                                                                    <td align=\"center\" class=\"esd-block-text es-p40r es-p40l es-m-txt-c\">\n" +
                    "                                                                                        <h1>YOUR TOKEN</h1>\n" +
                    "                                                                                    </td>\n" +
                    "                                                                                </tr>\n" +
                    "                                                                                <tr>\n" +
                    "                                                                                    <td align=\"center\" class=\"esd-block-text es-p20t es-p40r es-p40l es-m-p30r es-m-p30l\">\n" +
                    "                                                                                        <p>We invite you to our Halloween party! Netus et malesuada fames ac turpis egestas sed tempus urna. Nibh mauris cursus mattis molestie. Eget aliquet nibh praesent tristique magna sit amet purus gravida. Morbi tincidunt ornare massa eget egestas purus viverra accumsan in. Pretium lectus quam id leo in vitae.</p>\n" +
                    "                                                                                    </td>\n" +
                    "                                                                                </tr>\n" +
                    "                                                                                <tr>\n" +
                    "                                                                                    <td align=\"center\" class=\"esd-block-image es-p10t es-p10b es-p40r es-p40l es-m-p20r es-m-p20l\" style=\"font-size: 0px;\"><a target=\"_blank\" href=\"https://viewstripo.email\"><img class=\"adapt-img\" src=\"https://tlr.stripocdn.email/content/guids/CABINET_1c1ce83354239d17871322fbf9106775/images/group_160.png\" alt style=\"display: block;\" width=\"440\"></a></td>\n" +
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
