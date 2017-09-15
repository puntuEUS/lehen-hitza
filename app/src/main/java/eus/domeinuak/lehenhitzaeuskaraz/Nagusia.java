package eus.domeinuak.lehenhitzaeuskaraz;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.android.device.DeviceName;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Nagusia extends AppCompatActivity implements View.OnClickListener {

    // Android gailuaren defektuzko hizkuntza gordetzeko
    private String gailuarenHizkuntza;

    // Android gailu ezberdinetan euskara adierazteko erabiltzen diren locale-ak: Resources.getSystem().getAssets().getLocales(); bidez ateratakoa
    private final static List euskalLocaleak = Arrays.asList("eu", "eu-ES", "eu_ES", "eu_");

    // Erabiltzailea ezarpenetatik datorren adierazten duen aldagaia
    private boolean hizkuntzEzarpenetatik = false;

    // Orientazio aldaketa bat gertatzen bada, uneko dialogoa bistaratzen jarraitu ahal izateko kontrol aldagaia
    private int unekoDialogoa = 0;

    // Aplikazioaren egoera kontrolatzeko erabiltzen diren key-value bikote gakoak
    private final static String EZARPENETATIK_STATE = "ezarpenetatik";

    private final static String UNEKO_DIALOGOA = "dialogoa";

    // Firebase-eko datu-basearen erreferentzia gordeko duen aldagai
    private DatabaseReference firebaseDatubasea;

    // Ezarpen honek uneko gailuaren estatistikak gorde diren jakiteko balio du
    private final static String prefEstatistikak = "estatistikaGordeak";

    // Ezarpen honek uneko gailuaren estatistikak gordetzeko erabilitako id-a gordetzeko balio du
    private final static String prefGailuId = "gailuId";

    // Uneko gailuaren informazioa gordetzen duen aldagaia
    private Gailua gailua;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nagusia);

        Button euskaratuBotoia = (Button) findViewById(R.id.euskaratuButton);
        euskaratuBotoia.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Datu-base objektuaren instantzia bat lortu
        firebaseDatubasea = FirebaseDatabase.getInstance().getReference();

        // Gailuan ezarrita dagoen hizkuntza lortu, euskaraz dagoen jakiteko. onResume bakoitzean exekutatu behar da, ezarpenetan hizkuntz aldaketa egin eta gero eguneratzen ez badugu aplikazioa ez delako konturatuko
        //  euskaraz dagoela
        gailuarenHizkuntzaLortu();


        // Hizkuntza aldatzetik badator eta orain euskaraz badauka, zoriondu. Kontrolatzeko erabilitako boolearra falsera ezarri, hemendik aurrera aplikazioa itxi arte behin eta berriz exekutatu ez dadin
        if (hizkuntzEzarpenetatik && euskalLocaleak.contains(gailuarenHizkuntza)) {
            hizkuntzEzarpenetatik = false;

            // Aplikazioa erabilita gailua euskaratu badu estatistikak eguneratu behar dira. Horretaz arduratzen den funtzioari deitu
            gailuaEuskaratuDu();

            dialogoaErakutsi(R.string.euskaraEzarriDu);
        }
    }

    /**
     * Funtzio hau bi egoeratan exekutatuko da:
     *  - Aplikazioak fokua galdu aurretik: erabiltzailea ezarpenen orrialdera joan al den ikusteko erabiliko dugu
     *  - Orientazio aldaketa dagoenean: unean bistaratzen ari den dialogoa (egotekotan) gordetzeko
     * @param savedInstanceState
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putBoolean(EZARPENETATIK_STATE, hizkuntzEzarpenetatik);

        savedInstanceState.putInt(UNEKO_DIALOGOA, unekoDialogoa);

        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Funtzio hau aplikazioak fokua galdu edo orientazioa aldatu eta gero berriz exekutatzen denean exekutatuko da, eta bi gauza kontrolatzen ditu:
     * - Erabiltzailea hizkuntz ezarpenetatik datorren
     * - Dialogoren bat bistaratua izaten ari al zen
     * @param savedInstanceState
     */
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        hizkuntzEzarpenetatik = savedInstanceState.getBoolean(EZARPENETATIK_STATE);
        unekoDialogoa = savedInstanceState.getInt(UNEKO_DIALOGOA);

        if (0 != unekoDialogoa) { // Dialogoren bat bistaratua izaten ari bazen, berriz ere bistaratu. Hau orientazio aldaketen kasuan exekutatuko da
            dialogoaErakutsi(unekoDialogoa);
        }

        super.onRestoreInstanceState(savedInstanceState);
    }


    /**
     * Funtzio hau exekutatuko da erabiltzaileak gailua euskaratzearen aukera hautatzen duenean. Euskaraz jar daitekeen begiratuko du, eta horren arabera dialogo bat edo bestea erakutsi. Horrekin
     * batera, gailuaren estatistikak ere gordeko ditu
     * @param v
     */
    @Override
    public void onClick(View v) {
        boolean mugikorraEuskarazJarDaiteke = mugikorraEuskarazJarAlDaiteke();

        if ( mugikorraEuskarazJarDaiteke ) {
            if ( !euskalLocaleak.contains(gailuarenHizkuntza) ) { // Gailuaren hizkuntza bektorean ez badago, mugikorra ez du euskaraz jarri, horretarako aukera eduki arren.
                // Jarrita ez daukala jakinarazi eta Ezarpenen orrialdera bideratu

                // Uneko gailuaren estatistikak gordetzen dituen funtzioari deitu, gailua euskaraz jar daitekeen arren euskaraz ez dagoela adieraziz. Aldaketa egiten badu, gero eguneratuko da
                informazioaGorde("EZ");

                dialogoaErakutsi(R.string.euskaraDaukaDeskribapena);
            } else { // Mugikorra euskaraz dauka, zoriontze mezua. Hau bi egoeratan exekutatuko da: hizkuntza aldatu berritan edota mugikorra hasieratik euskaraz dagoenean

                // Uneko gailuaren estatistikak gordetzen dituen funtzioari deitu, gailua dagoeneko euskaraz jarrita dagoela adieraziz
                informazioaGorde("");

                dialogoaErakutsi(R.string.euskaratutaDagoDeskribapena);
            }
        } else { // Ezin da euskaraz jarri, abisatu

            // Uneko gailuaren estatistikak gordetzen dituen funtzioari deitu, gailua euskaraz jar ezin daitekeela adieraziz
            informazioaGorde("EZIN");

            dialogoaErakutsi(R.string.euskararikEzDeskribapena);
        }
    }


    /**
     * Funtzio honek AlertDialog-a erakutsiko du. Jasotzen duen deskribapenaren arabera, ekintza bat (Ezarpenenen orrialdea irekitzea) gertatuko da "Ados" botoia sakatzean, edo besterik gabe dismiss egingo du
     * @param deskribapena
     */
    private void dialogoaErakutsi(int deskribapena) {

        // Dialog-aren sortzailea sortu eta deskribapena esleitu
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(deskribapena);

        // Unean bistaratzen ari den dialogoa gorde
        unekoDialogoa = deskribapena;

        // Deskribapenaren araberako portaera

        if (deskribapena == R.string.euskaraDaukaDeskribapena) { // Gailua euskaraz jar badaiteke baina euskaraz ez badago, "Ados" sakatzean ezarpenetara joan
            builder.setPositiveButton(R.string.dialogoaOnartu, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Erabiltzailea ezarpenetara eraman dugula kontrolatzen duen aldagaiaren balioa ezarri
                    hizkuntzEzarpenetatik = true;
                    // Dialogoa itxi du, unean dialogorik ez daukagula ezarri
                    unekoDialogoa = 0;

                    // Erabiltzailea ezarpenetara bideratzen duen intent-a martxan jarri
                    Intent languageIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                    startActivity(languageIntent);
                }
            });
        } else { // Gainontzeko kasuetan, bere lagunen artean zabaltzeko aukera eman eta dialogoa ixteko aukera emango diogu
            builder.setPositiveButton(R.string.dialogoaZabaldu,new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Dialogoa itxi du, unean dialogorik ez daukagula ezarri
                    unekoDialogoa = 0;

                    // Intent bat hasieratu, mudikorrean instalatuta dauzkan aplikazioen bitartez Lehen Hitza aplikazioa zabaltzeko aukera izan dezan
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    // Zabalduko den mezua
                    sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.zabaltzeMezua));
                    sendIntent.setType("text/plain");
                    // Aplikazio hautatzailea, aldi oro erabiltzaileak zabaltzeko erabili nahi duen aplikazioa aukera ahal dezan
                    startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.dialogoaZabaldu)));
                }
            });
            builder.setNeutralButton(R.string.dialogoaUtzi, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Dialogoa itxi du, unean dialogorik ez daukagula ezarri
                    unekoDialogoa = 0;
                }
            });
        }

        // Dialog-a sortu eta erakutsi
        builder.create();
        builder.show();
    }


    //
    //  LOCALE detekziorako funtzio lagungarriak
    //


    /**
     * Funtzio lagungarri honek gailuan ezarrita dagoen hizkuntza hautatuko du. Informazio horretan oinarrituta erabiltzailea ezarpenen orrialdera eramango dugu
     *
     * Konprobazioa bi modu ezberdinetan egiten da, bat SDK-ren 24. bertsioa edo berriagoa duten gailuentzat eta bestea hori baino lehenagokoa dutenentzat (Manifestuko bertsio minimoa 15.-a da)
     */
    private void gailuarenHizkuntzaLortu() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // API-aren 24. bertsiotik aurrera soilik erabili daiteke
            gailuarenHizkuntza =  getResources().getConfiguration().getLocales().get(0).getLanguage();
        } else { // API-aren 24.bertsiotik aurrera deprekatuta dago, baina bertsio zaharretan (API 15 <= x < API 24) erabiltzeko behar dugu
            gailuarenHizkuntza = getResources().getConfiguration().locale.getLanguage();
        }
    }

    /**
     * Funtzio hau arduratuko da mugikorraren hizkuntzak begiratzeaz eta mugikorra euskaraz jarri ote daitekeen ikusteaz. Euskaraz jarri badaiteke true itzuliko du, eta bestela false
     * */
    private boolean mugikorraEuskarazJarAlDaiteke() {
        // Honekin ez dira denak agertzen, emuladorean adibidez esaten du ez daukala euskararik, baina gailu fisiko gehienetan (Huawei eta HTC gailu batzuetan ezetz dirudi) ondo doa. Emuladoreko Custom Locale aplikazioan lor daitekeen hizkuntz zerrenda
        //  itzultzen du
        String[] locales = Resources.getSystem().getAssets().getLocales();

        for(String l:locales) {
            if (euskalLocaleak.contains(l)) { // Uneko locale-a bektorean badago, mugikorra euskaraz jartzeko aukera dago
                return true;
            }
        }
        return false;
    }


    //
    //  Estatistikak gordetzeko funtzioak
    //

    /*
    * Funtzio honek uneko gailuaren modelo, Android bertsio etab. hartuko ditu eta datu-base zentralizatuan gordeko ditu
    * */
    private void informazioaGorde(String euskaraz) {
        // Estatistikak gorde diren kontrolatzen duen ezarpenaren balioa lortu
        SharedPreferences ezarpenak =  getPreferences(MODE_PRIVATE);
        boolean estatistikaGordeak = ezarpenak.getBoolean(prefEstatistikak, false);


        // Gailuaren SIM txartelaren operadorea behar dugu, horretarako TelephonyManager objektua lortu
        TelephonyManager telephonyManager = ((TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE));

        SimpleDateFormat dataFormatua = new SimpleDateFormat("yyyy-MM-dd");

        // gailua objektuari balioa eman, uneko gailuaren informazioarekin
        gailua = new Gailua(Build.BRAND, Build.MANUFACTURER, Build.DEVICE, DeviceName.getDeviceName(), Build.PRODUCT, Long.valueOf(Build.VERSION.SDK_INT), Build.VERSION.RELEASE, telephonyManager.getSimOperatorName(), gailuarenHizkuntza, euskaraz, dataFormatua.format(new Date()));

        if (!estatistikaGordeak) { // Orain arte gailu honentzako estatistikak gorde ez badira, gorde

            // Datu-basera konexioa ireki
            firebaseDatubasea.goOnline();

            // Gailua gordetzeko erabiliko den ID-a gorde. Horrela, erabiltzaileak aplikazioa erabilita gailua euskaratzen badu, estatistikak gorde ahalko ditugu
            String firebaseErrenkadaId = firebaseDatubasea.push().getKey();

            // gailua objektua datu-basean gorde
            firebaseDatubasea.child(firebaseErrenkadaId).setValue(gailua);

            // Datu-baserako konexioa itxi. Firebase-ek aldi berean egin daitezkeen konexioak 100era mugatzen ditu doako bertsioan, beraz, komeni da idazketa bukatu eta berehala konexioa ixtea
            firebaseDatubasea.goOffline();

            // Ezarpenak aldatuko ditugu, uneko gailuarentzat estatistikak gorde ditugula ezartzeko eta gailuaren ID-a gordetzeko
            SharedPreferences.Editor ezarpenAldatzailea = ezarpenak.edit();
            ezarpenAldatzailea.putBoolean(prefEstatistikak, true);
            ezarpenAldatzailea.putString(prefGailuId, firebaseErrenkadaId);
            ezarpenAldatzailea.apply();
        }

    }

    /*
    * Funtzio hau exekutatuko da erabiltzaileak aplikazioa erabilita gailua euskaratzen duenean. Izan ere, estatistikak lehen aldiz gordetzean ez dakigu oraindik erabiltzaileak hizkuntza aldatuko duen (nahiz eta berak horretarako aukera eduki). Beraz, funtzio
    * hau erabiliko da "a posteriori" informazio hori gordetzeko, behar izatekotan
    * */
    private void gailuaEuskaratuDu() {
        // Gailuaren id-a lortu, eguneratu ahal izateko
        SharedPreferences ezarpenak =  getPreferences(MODE_PRIVATE);
        String gailuId = ezarpenak.getString(prefGailuId, "");

        if ("" != gailuId) { // Id-a hutsik ez badago

            // Datu-basera konexioa ireki
            firebaseDatubasea.goOnline();

            // Gailuaren informazioa eguneratu datu-basean, euskaratua izan dela adierazteko
            firebaseDatubasea.child(gailuId).child("euskaratuAlDu").setValue("BAI");

            // Datu-baserako konexioa itxi. Firebase-ek aldi berean egin daitezkeen konexioak 100era mugatzen ditu doako bertsioan, beraz, komeni da idazketa bukatu eta berehala konexioa ixtea
            firebaseDatubasea.goOffline();
        } else { // Id-a hutsik badago estatistikak zerotik gordeko ditugu

            // Estatistikak gorde ez ditugula ezarri
            SharedPreferences.Editor ezarpenAldatzailea = ezarpenak.edit();
            ezarpenAldatzailea.putBoolean(prefEstatistikak, false);
            ezarpenAldatzailea.commit();

            // Gailuaren informazioa gordetzen duen funtzioa deitu
            informazioaGorde("BAI");
        }
    }

}