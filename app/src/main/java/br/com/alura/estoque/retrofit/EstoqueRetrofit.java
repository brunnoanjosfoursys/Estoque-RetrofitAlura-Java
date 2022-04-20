package br.com.alura.estoque.retrofit;

import br.com.alura.estoque.retrofit.service.ProdutoService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EstoqueRetrofit {

    private final ProdutoService produtoService;

    //6min
    public EstoqueRetrofit() {
        //Configurando instancia do nosso retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.163:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        produtoService = retrofit.create(ProdutoService.class);
    }

    //Tendo acesso ao nosso service em qualquer lugar do nosso APP atraves da instancia Retrofit
    public ProdutoService getProdutoService() {
        return produtoService;
    }
}
