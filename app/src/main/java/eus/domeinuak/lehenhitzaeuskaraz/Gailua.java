package eus.domeinuak.lehenhitzaeuskaraz;


import com.google.firebase.database.IgnoreExtraProperties;

/*
* Klase honek Firebase-eko datu-basean erabiliko den datu-basearen egitura irudikatzen du
* */
@IgnoreExtraProperties
public class Gailua {

    // Klasearen propietateak: Firebase-ren beharrak direla eta publikoak izan behar dira


    //
    // OHARRA: Firebase-eko kontsolako segurtasun arauetan zehaztu da zein den gordetako informazioak eduki behar duen egitura. Beraz, gordeko den informazio kopuruan aldaketarik egitekotan, bertan ere egin beharko da
    //

    // Gailua salmentan jartzeko erabili den marka
    public String marka;
    // Gailua egin duen enpresa (batzuetan ez dira berdinak (Google-en nexus batzuk, adibidez, LG-renak dira))
    public String fabrikatzailea;
    // Gailuaren izena
    public String izena;
    // Gailuaren izena, user-friendly den formatu batean
    public String modeloa;
    // Gailuaren produktuaren izena (normalean izen interno bat izaten da, salmentarako erabiltzen denaren ezberdina)
    public String produktua;
    // Gailuaren Android SDK bertsio zenbakia, APIaren notazioarekin
    public Long androidAPI;
    // Gailuaren Android SDK bertsioa, notazio orokorrarekin (4.3, 7.1.2, etab.)
    public String androidBertsioa;
    // Gailuan jarrita dagoen SIM txartelaren operadora
    public String zerbitzuEmailea;
    // Gailuak euskara daukan ala ez
    public Boolean euskarazBai;
    // Gailuak aplikazioa exekutatu aurretik ezarrita zeukan hizkuntza
    public String gailuarenHizkuntza;
    /*
     *  Erabiltzaileak gailua euskaratu al duen kontrolatzen du. Lau balio izan ditzake:
     *      - "" -> Dagoeneko euskaraz dauden mugikorrak
     *      - "EZ" -> euskaraz jar daitezkeen arren, euskaratu ez dituztenak
      *     - "BAI" -> euskaratu diren mugikorrak
      *     - "EZIN" -> Euskara ez duten mugikorrentzako. Redundantea da, euskarazBai aldagaiak hori gordetzen duelako. Hemen gehitzearen arrazoia da horrela erabiltzaileek izandako
      *         jarrera hobe aztertu ahalko dugula, kasuistika guztiak biltzen dituelako.
      *  OHARRA: Era berean, euskarazBai aldagaia mantentzen jarraitu behar dugu, horrek gailuaren informazio gordetzen duelako eta modu errazean ikusi ahalko delako zein modelok daukan euskara
       *    eta zeinek ez
      *
      */
    public String euskaratuAlDu = "EZIN";
    // Gailuaren informazioa gorde den data
    public String data;

    // Eraikitzaile hutsa
    public Gailua() {}

    // Eraikitzailea
    public Gailua(String marka, String fabrikatzailea, String izena, String modeloa, String produktua, Long androidAPI, String androidBertsioa, String zerbitzuEmailea, String gailuarenHizkuntza, String euskaraz, String data) {
        this.marka = marka;
        this.fabrikatzailea = fabrikatzailea;
        this.izena = izena;
        this.modeloa = modeloa;
        this.produktua = produktua;
        this.androidAPI = androidAPI;
        this.androidBertsioa = androidBertsioa;
        this.zerbitzuEmailea = zerbitzuEmailea;
        this.gailuarenHizkuntza = gailuarenHizkuntza;
        switch(euskaraz) {
            case "EZIN":
                this.euskarazBai = false;
                break;
            default: // Case "EZ", "BAI", ""
                this.euskarazBai = true;
        }
        // "EZ" duten kasuetan, balio hau gero eguneratuko dugu, erabiltzaileak ezarpenetan hizkuntza aldatzen badu
        this.euskaratuAlDu = euskaraz;
        this.data = data;
    }

    /*
    *   Funtzio honekin uneko gailua euskaratua izan dela adieraziko dugu, objektua berriro Firebase-era igo ahal izateko eta balio hori eguneratu ahal izateko
     */
    public void euskaratuDute() {
        this.euskaratuAlDu = "BAI";
    }

}
