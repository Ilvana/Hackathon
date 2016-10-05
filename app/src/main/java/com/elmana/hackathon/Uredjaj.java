package com.elmana.hackathon;

/**
 * Created by ilvana on 10/12/2015.
 */
public class Uredjaj {

        int _id;
        String ime_uredjaja;
        String adresa_uredjaja;

        // Empty constructor
        public Uredjaj(){

        }
        // constructor
        public Uredjaj(int id, String ime, String adresa){
            this._id = id;
            this.ime_uredjaja = ime;
            this.adresa_uredjaja = adresa;
        }

        public Uredjaj(String ime, String adresa){
            this.ime_uredjaja = ime;
            this.adresa_uredjaja = adresa;
        }

        public int getID(){
            return this._id;
        }

        public void setID(int id){
            this._id = id;
        }

        public String getIme_uredjaja(){
            return this.ime_uredjaja;
        }

        public void setIme_uredjaja(String ime){
            this.ime_uredjaja = ime;
        }

        public String getAdresa_uredjaja(){
            return this.adresa_uredjaja;
        }

        public void setAdresa_uredjaja(String adresa){
            this.adresa_uredjaja = adresa;
        }
    }

