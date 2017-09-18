# Euskaraz (English below)
[Lehen Hitza](https://play.google.com/store/apps/details?id=eus.domeinuak.lehenhitzaeuskaraz) aplikazioaren kodea biltzen duen biltegia.

Interneteko zerbitzu eta aplikazio garatzaileek zure Interneteko arakatzaile zein Android gailuan ezarrita daukazun hizkuntza hartzen dute kontutan garapenean erabiliko dituzten hizkuntzak erabakitzerako orduan. Horregatik, garrantzitsua da zure Android gailua euskaraz edukitzea, Android Sistema Eragilea eta bere aplikazioak euskaraz erabili nahi dituen erabiltzaile komunitate zabal bat dagoela jakin dezaten. Horrela, gero eta zerbitzu gehiago erabili ahalko ditugu gure hizkuntzan.

Hori da aplikazio honen helburua, Android Sistema Eragilearen euskal erabiltzaile komunitateari bere Android gailua euskaraz jartzen laguntzea. Gailua euskaraz jar badaiteke aplikazioak aldaketa egiteko urratsak laburtuko ditu. Zoritxarrez, gailu guztiak ezin dira euskaraz jarri, gailuaren fabrikatzaileek horrela erabakita. Kasu horietan aplikazioak ezingo du hizkuntz aldaketa burutu.

Azkenik, euskaraz erabili daitezkeen gailuen zerrenda bat osatzeko asmoz, aplikazioak gailuaren oinarrizko informazioa (fabrikatzailea, modeloa, Android bertsioa, euskaraz dagoen ala ez) gordetzen du. Informazioa bildu heinean gailu zerrenda argitaratuko da, gailu berri bat erostean euskara dutenak zeintzuk diren jakin ahal izateko.

## Kodea erabiltzeko
Kode hau Android gailuak euskaratzen laguntzeko prestatuta dago baina beste hizkuntz komunitateentzako ere erabilgarria izan daiteke, moldatuz gero. Egin beharreko moldaketa  *lehen-hitza/app/src/main/java/eus/domeinuak/lehenhitzaeuskaraz/Nagusia.java* fitxategiko *euskalLocaleak* aldagaiari dagokio. Aldagai horrek Android Sistema Eragilean euskara adierazteko erabiltzen diren hizkuntz-kode ezberdinak biltzen ditu, eta, beraz, aplikazioa beste hizkuntza baterako erabili nahi izatekotan bektore hori moldatu beharko da hizkuntz berri horretarako. Android bertsio guztietan ez da hizkuntz-kode berdina erabiltzen hizkuntza bat adierazteko, ondorengo helbideetan ikus daitezke kode horietako batzuk:
* (http://www.loc.gov/standards/iso639-2/php/code_list.php)
* (https://stackoverflow.com/questions/7973023/what-is-the-list-of-supported-languages-locales-on-android)
* Horretaz gain, Android Studioren emuladoreko *Locale* aplikazioan hizkuntz bakoitzaren kodeak ikus daitezke. Emuladorea Android bertsio ezberdinekin martxan jarriz gero, hizkuntz-kodeen aldaerak topatzeko aukera gehiago (bertsio "nagusiekin" (4.0, 5.0, 6.0, etab.) probatzearekin nahikoa da).

Horretaz gain, aplikazioak [Firebase](https://firebase.google.com/) datu-base bat erabiltzen du gailuen oinarrizko informazioa biltzeko. Datu-base bat erabili nahi izatekotan beharrezkoa da Android Studio-n aplikazioa konfiguratzea, zuk sortutako Firebase datu-basera konexioa egin dezan. Horretarako, Android Studio-n goiko barran *Tools->Firebase* aukera sakatu eta bertako urratsak jarraitu. Horiek jarraitzean *app* karpetan fitxategi berri bat sortuko da konfigurazio informazioarekin, *google-services.json* deiturikoa.

## Lizentzia

Lehen Hitza aplikazioa software librea da eta [GNU General Public License v3](http://www.gnu.org/licenses/gpl.html) lizentzia erabiliz banatzen da.

<a rel="license" href="http://www.gnu.org/licenses/gpl.html"><img alt="GNU General Public License version 3" style="border-width:0" src="http://www.gnu.org/graphics/gplv3-127x51.png" /></a>

Aplikazioak Jared Rummler-ek garatutako [Android Device Names](https://github.com/jaredrummler/AndroidDeviceNames) liburutegia erabiltzen du, Apache 2 lizentziapean argitaratua dagoena. Liburutegi honek Android gailu batzuen izenak eskuratuko ditu gailuaren fabrikazioa izena hartuta.

***

# English

Repository that contains the code of the [Lehen Hitza](https://play.google.com/store/apps/details?id=eus.domeinuak.lehenhitzaeuskaraz) app.

The language set in your Android device is taken into account by Internet service providers and app developers when deciding which languages will be included in their products. That is why is important to have your device set in your own language, so that you can use Android and your favourite apps in your language. The more users a language has, the more likelihood for such language to be included in the list of supported languages.

The aim of this app is to help Basque users setting their devices in Basque. If the given device can be set in Basque the app will shorten the process to make the change. Unfortunately, not all devices can be set in Basque, as it is vastly dependant on the will of the manufacturer. In those cases the app will not be able to make the change.

Finally, the app will collect basic data about the device (manufacturer, model, Android version, whether it can be set in Basque or not), in order to collect a list of devices that can be used in Basque. Such list will be published soon, so that Basque users looking for a new Android device know the language preferences of each device beforehand.

## Code reutilisation
This code has been developped to help setting Basque in Android devices, but it is also practical for other language communities. In order to do that, variable *euskalLocaleak* located in *lehen-hitza/app/src/main/java/eus/domeinuak/lehenhitzaeuskaraz/Nagusia.java* needs to be changed. *euskalLocaleak* is an array that contains the different languages code used to identify Basque in the Android Operating System. Therefore, its content should be changed to reflect language codes of the targetted language. Language codes may not be the same across all different Android versions, those can be checked in:
* (http://www.loc.gov/standards/iso639-2/php/code_list.php)
* (https://stackoverflow.com/questions/7973023/what-is-the-list-of-supported-languages-locales-on-android)
* Besides, language codes can also be checked in the application *Locales* of the Android Studio emulator. When setting the emulator to different major versions (setting versions 4.0, 5.0, 6.0, etc. should do) you will see the language code used in that version.

Apart from that, the app relies on a [Firebase](https://firebase.google.com/) database to collect basic data about the devices. If you want to keep that functionality you will need to configure Android Studio to connect the app with your own database. It can be achieved by folllowing the steps of the option *Tools->Firebase* in Android Studio's toolbar. Upon completion a new file called *google-services.json* will be included with the configuration settings in the *app* directory.

## License

Lehen Hitza is free software distributed with the [GNU General Public License v3](http://www.gnu.org/licenses/gpl.html) license.

<a rel="license" href="http://www.gnu.org/licenses/gpl.html"><img alt="GNU General Public License version 3" style="border-width:0" src="http://www.gnu.org/graphics/gplv3-127x51.png" /></a>

The app also uses library [Android Device Names](https://github.com/jaredrummler/AndroidDeviceNames), developed by Jared Rummler, and distributed with Apache 2 license. This library will convert some device names from manufacturers' internal names to the actual name used when selling the device.
