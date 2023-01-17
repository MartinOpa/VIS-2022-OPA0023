# VIS-2022-OPA0023

## Projekt do předmětu vývoj informačních systémů na VŠB-TUO

Jedná se o jednoduchý rezervační systém autoservisu psaný v Javě za využití Javafx pro uživatelské rozhranní. Pro databázi slouží nativní ApacheDerby.

## Rozdělení do vrstev

### Prezentační vrstva

Jednoduché uživatelské rozhraní za využití javafx s formátováním rozhraní pomocí fxml souborů, obsahuje všechny potřebné ovladače pro jednotlivé scény.
Vrstva se nachází v src/main/java/presentation_layer, fxml soubory v src/main/java/resources/fxml.

### Doménová vrstva

Obsahuje business logiku aplikace a všechny třídy potřebné pro její správné fungování.
Nachází se v src/main/java/domain_layer.

Jednotlivé třídy:
* Address - slouží jako datový typ adresy.
* Client - slouží jako datový typ klienta, dědí z abstraktní třídy User. Obsahuje metody potřebné pro komunikaci s datovou vrstvou.
* ClientHolder - třída dle vzoru singleton. Obsahuje instanci přihlášeného uživatele, přes kterou získává prezentační vrstva potřebná data.
* LoginValidation - třída s metodami pro přihlášení, registraci a reset hesla. Obsahuje také soukromou metodu pro šifrování hesla pomocí SHA-512.
* Reservation - slouží jako datový typ rezervace.
* User - abstraktní třída sloužící jako základ všech typů uživatelů, v systému implementován pouze Client.
* Vehicle - slouží jako datový typ vozidla.

### Datová vrstva

Obsahuje připojení na databázi ApacheDerby a metody potřebné pro její využití a filtrování výsledků.
Nachází se v src/main/java/data_layer.
