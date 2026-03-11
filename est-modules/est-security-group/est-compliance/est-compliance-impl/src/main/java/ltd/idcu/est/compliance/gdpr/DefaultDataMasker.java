package ltd.idcu.est.compliance.gdpr;

import ltd.idcu.est.compliance.api.gdpr.DataMasker;

public class DefaultDataMasker implements DataMasker {

    @Override
    public String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return email;
        }
        int atIndex = email.indexOf("@");
        if (atIndex <= 1) {
            return email;
        }
        String username = email.substring(0, atIndex);
        String domain = email.substring(atIndex);
        if (username.length() <= 2) {
            return username.charAt(0) + "***" + domain;
        }
        return username.charAt(0) + "***" + username.charAt(username.length() - 1) + domain;
    }

    @Override
    public String maskPhone(String phone) {
        if (phone == null || phone.length() < 7) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4);
    }

    @Override
    public String maskIdCard(String idCard) {
        if (idCard == null || idCard.length() < 10) {
            return idCard;
        }
        return idCard.substring(0, 6) + "********" + idCard.substring(idCard.length() - 4);
    }

    @Override
    public String maskBankCard(String bankCard) {
        if (bankCard == null || bankCard.length() < 10) {
            return bankCard;
        }
        return bankCard.substring(0, 4) + " **** **** " + bankCard.substring(bankCard.length() - 4);
    }

    @Override
    public String maskName(String name) {
        if (name == null || name.length() <= 1) {
            return name;
        }
        if (name.length() == 2) {
            return name.charAt(0) + "*";
        }
        return name.charAt(0) + "*" + name.substring(name.length() - 1);
    }

    @Override
    public String mask(String data, MaskType type) {
        switch (type) {
            case EMAIL:
                return maskEmail(data);
            case PHONE:
                return maskPhone(data);
            case ID_CARD:
                return maskIdCard(data);
            case BANK_CARD:
                return maskBankCard(data);
            case NAME:
                return maskName(data);
            case CUSTOM:
            default:
                if (data == null || data.length() <= 2) {
                    return data;
                }
                return data.charAt(0) + "***";
        }
    }
}
