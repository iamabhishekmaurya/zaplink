package io.zaplink.core.dto.request.qr;

import lombok.Data;

@Data
public class QRConfig
{
    private String       data;
    private int          size                  = 1024;
    private int          margin                = 0;
    private QRBodyConfig body                  = new QRBodyConfig();
    private QREyeConfig  eye                   = new QREyeConfig();
    private QRLogoConfig logo;
    private String       backgroundColor       = "#FFFFFF";
    private boolean      transparentBackground = false;
}
