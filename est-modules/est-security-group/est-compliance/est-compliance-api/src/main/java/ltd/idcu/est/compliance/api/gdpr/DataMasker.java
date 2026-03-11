package ltd.idcu.est.compliance.api.gdpr;

public interface DataMasker {

    String maskEmail(String email);

    String maskPhone(String phone);

    String maskIdCard(String idCard);

    String maskBankCard(String bankCard);

    String maskName(String name);

    String mask(String data, MaskType type);

    enum MaskType {
        EMAIL,
        PHONE,
        ID_CARD,
        BANK_CARD,
        NAME,
        CUSTOM
    }
}
