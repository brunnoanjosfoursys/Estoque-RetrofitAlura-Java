package br.com.alura.estoque.retrofit;

import androidx.annotation.NonNull;

import br.com.alura.estoque.retrofit.service.ProdutoService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EstoqueRetrofit {

    private static final String BASE_URL = "http://192.168.0.181:8080/";
    private final ProdutoService produtoService;

    //6min
    public EstoqueRetrofit() {
        OkHttpClient client = configuraClient();

        //Configurando instancia do nosso retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        produtoService = retrofit.create(ProdutoService.class);
    }

    @NonNull
    private OkHttpClient configuraClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
        return client;
    }

    //Tendo acesso ao nosso service em qualquer lugar do nosso APP atraves da instancia Retrofit
    public ProdutoService getProdutoService() {
        return produtoService;
    }
}
