package com.pipe.avi.model;

public class Admin {

    private  Integer id ;
    private  String nombre ;
    private  String email ;
    private  String password ;

    public Admin(Integer id, String nom, String em, String pass){

        //constructor
        this.id = id;
        this.nombre = nom;
        this.email = em;
        this.password = pass;

    }


    //metodos set para darle valor a las variables
    public void setId(Integer id){
        this.id = id;
    }
    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setPassword(String password){
        this.password = password;
    }


    //metodos set para obtener el  valor a las variables
    public Integer getId(){
        return id;
    }

    public String getNombre(){
        return nombre;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

}
