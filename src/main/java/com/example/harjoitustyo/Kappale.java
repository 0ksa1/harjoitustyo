package com.example.harjoitustyo;


import java.util.concurrent.atomic.AtomicInteger;

/** Luokka toteuttaa yksinkertaisen kappaleen, jolla on
 * järjestysnumero, nimi, kesto ja tiedostotyyppi
 * @author Aliisa Pikkarainen
 * @version 1.0 2024/04/09
 */
public class Kappale {
    /**
     * järjestysnumero AtomicInteger:inä.
     */
    private AtomicInteger jarjestysNumero;
    /**
     * nimi merkkijonona.
     */
    private String nimi;
    /**
     * kesto merkkijonona.
     */
    private String kesto;
    /**
     * tiedostotyyppi merkkijonona.
     */
    private String tiedostoTyyppi;

    /**
     * Kappale perustiedoila
     * @param jarjestysNumero AtomicInteger kappaleen järjestysnumero
     * @param nimi String kappaleen nimi
     * @param kesto String kappaleen kesto
     * @param tiedostoTyyppi String kappaleen tiedostotyyppi
     */
    public Kappale(AtomicInteger jarjestysNumero, String nimi, String kesto, String tiedostoTyyppi) {
        this.jarjestysNumero = jarjestysNumero;
        this.nimi = nimi;
        this.kesto = kesto;
        this.tiedostoTyyppi = tiedostoTyyppi;
    }

    /**
     * Palauttaa järjestysnumeron
     * @return AtomicInteger jarjestysNumero
     */
    public AtomicInteger getJarjestysNumero() {
        return jarjestysNumero;
    }

    /**
     * Asettaa järjestysnumeron
     * @param jarjestysNumero AtomicInteger
     */
    public void setJarjestysNumero(AtomicInteger jarjestysNumero) {
        this.jarjestysNumero = jarjestysNumero;
    }

    /**
     * Palauttaa nimen
     * @return String nimi
     */
    public String getNimi() {
        return nimi;
    }

    /**
     * Asettaa nimen
     * @param nimi String
     */
    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    /**
     * Palauttaa keston
     * @return String kesto
     */
    public String getKesto() {
        return kesto;
    }

    /**
     * Asettaa keston
     * @param kesto String
     */
    public void setKesto(String kesto) {
        this.kesto = kesto;
    }

    /**
     * Palauttaa tiedostotyypin
     * @return String tiedostoTyyppi
     */
    public String getTiedostoTyyppi() {
        return tiedostoTyyppi;
    }

    /**
     * Asettaa tiedostotyypin
     * @param tiedostoTyyppi String
     */
    public void setTiedostoTyyppi(String tiedostoTyyppi) {
        this.tiedostoTyyppi = tiedostoTyyppi;
    }
}

