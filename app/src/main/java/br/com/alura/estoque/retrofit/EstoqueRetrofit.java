package br.com.alura.estoque.retrofit;

import retrofit2.Retrofit;

public class EstoqueRetrofit {

    //6min
    public EstoqueRetrofit() {
        //Configurando instancia do nosso retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.163:8080")
                .build();
    }
}
