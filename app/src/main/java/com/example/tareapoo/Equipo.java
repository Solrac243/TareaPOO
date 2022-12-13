package com.example.tareapoo;

import java.io.Serializable;

public class Equipo implements Serializable{
    private int serie;
    private String descripcion;
    private int valor;
    private String tipo;

    public Equipo(){};

    //region Constructores

    public Equipo(int serie, String descripcion, int valor, String tipo){
        this.serie = serie;
        this.descripcion = descripcion;
        this.valor = valor;
        this.tipo = tipo;
    }
    //endregion

    //region Get y Set

    public int getSerie() {
        return serie;
    }

    public void setSerie(int serie) {
        this.serie = serie;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    //endregion
}
