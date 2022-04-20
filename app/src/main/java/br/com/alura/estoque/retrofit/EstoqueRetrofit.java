package br.com.alura.estoque.retrofit;

import java.io.IOException;

import br.com.alura.estoque.retrofit.service.ProdutoService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EstoqueRetrofit {

    private final ProdutoService produtoService;

    //6min
    public EstoqueRetrofit() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        //Configurando instancia do nosso retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.166:8080/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        produtoService = retrofit.create(ProdutoService.class);
    }

    //Tendo acesso ao nosso service em qualquer lugar do nosso APP atraves da instancia Retrofit
    public ProdutoService getProdutoService() {
        return produtoService;
    }
}
