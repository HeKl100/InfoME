package model;

public class KVRequest {
    private String kundenNr;
    private String kunde;
    private int sachbearbeiterNr;
    private String sachbearbeiter;
    private int vermittlerNr;
    private String vermittler;
    private String betreff;
    private int kvNr;

    public KVRequest(String kundenNr, String kunde, int sachbearbeiterNr, String sachbearbeiter, int vermittlerNr, String vermittler, String betreff, int kvNr) {
        this.kundenNr = kundenNr;
        this.kunde = kunde;
        this.sachbearbeiterNr = sachbearbeiterNr;
        this.sachbearbeiter = sachbearbeiter;
        this.vermittlerNr = vermittlerNr;
        this.vermittler = vermittler;
        this.betreff = betreff;
        this.kvNr = kvNr;
    }

    public String getKundenNr() {
        return kundenNr;
    }

    public void setKundenNr(String kundenNr) {
        this.kundenNr = kundenNr;
    }

    public String getKunde() {
        return kunde;
    }

    public void setKunde(String kunde) {
        this.kunde = kunde;
    }

    public int getSachbearbeiterNr() {
        return sachbearbeiterNr;
    }

    public void setSachbearbeiterNr(int sachbearbeiterNr) {
        this.sachbearbeiterNr = sachbearbeiterNr;
    }

    public String getSachbearbeiter() {
        return sachbearbeiter;
    }

    public void setSachbearbeiter(String sachbearbeiter) {
        this.sachbearbeiter = sachbearbeiter;
    }

    public int getVermittlerNr() {
        return vermittlerNr;
    }

    public void setVermittlerNr(int vermittlerNr) {
        this.vermittlerNr = vermittlerNr;
    }

    public String getVermittler() {
        return vermittler;
    }

    public void setVermittler(String vermittler) {
        this.vermittler = vermittler;
    }

    public String getBetreff() {
        return betreff;
    }

    public void setBetreff(String betreff) {
        this.betreff = betreff;
    }

    public int getKvNr() {
        return kvNr;
    }

    public void setKvNr(int kvNr) {
        this.kvNr = kvNr;
    }
}
