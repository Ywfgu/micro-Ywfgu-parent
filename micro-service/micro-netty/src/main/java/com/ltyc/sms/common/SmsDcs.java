package com.ltyc.sms.common;

import java.io.Serializable;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/6
 */
public class SmsDcs implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -5797340786616956L;
    /** The encoded dcs. */
    protected final byte dcs_;

    /**
     * Creates a specific DCS.
     *
     * @param dcs
     *            The dcs.
     */
    public SmsDcs(byte dcs) {
        dcs_ = dcs;
    }

    /**
     * Returns the encoded dcs.
     *
     * @return The dcs.
     */
    public byte getValue() {
        return dcs_;
    }

    /**
     * Builds a general-data-coding dcs.
     *
     * @param alphabet
     *            The alphabet.
     * @param messageClass
     *            The message class.
     *
     * @return A valid general data coding DCS.
     */
    public static SmsDcs getGeneralDataCodingDcs(SmsAlphabet alphabet, SmsMsgClass messageClass) {
        byte dcs = 0x00;

        // Bits 3 and 2 indicate the alphabet being used, as follows :
        // Bit3 Bit2 Alphabet:
        // 0 0 Default alphabet
        // 0 1 8 bit data
        // 1 0 UCS2 (16bit) [10]
        // 1 1 Reserved
        switch (alphabet) {
            case ASCII:
            case GSM:
                dcs |= 0x00;
                break;
            case LATIN1:
                dcs |= 0x04;
                break;
            case UCS2:
                dcs |= 0x08;
                break;
            case RESERVED:
                dcs |= 0x0C;
                break;
        }

        switch (messageClass) {
            case CLASS_0:
                dcs |= 0x10;
                break;
            case CLASS_1:
                dcs |= 0x11;
                break;
            case CLASS_2:
                dcs |= 0x12;
                break;
            case CLASS_3:
                dcs |= 0x13;
                break;
            case CLASS_UNKNOWN:
                dcs |= 0x00;
                break;
        }

        return new SmsDcs(dcs);
    }

}
