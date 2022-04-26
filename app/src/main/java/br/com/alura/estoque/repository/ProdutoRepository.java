package br.com.alura.estoque.repository;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;

import br.com.alura.estoque.asynctask.BaseAsyncTask;
import br.com.alura.estoque.database.dao.ProdutoDAO;
import br.com.alura.estoque.model.Produto;
import br.com.alura.estoque.retrofit.EstoqueRetrofit;
import br.com.alura.estoque.retrofit.callback.BaseCallback;
import br.com.alura.estoque.retrofit.service.ProdutoService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class ProdutoRepository {
    private final ProdutoDAO dao;
    private final ProdutoService service;

    public ProdutoRepository(ProdutoDAO dao) {
        this.dao = dao;
        service = new EstoqueRetrofit().getProdutoService();
    }

    public void buscaProdutos(DadosCarregadosCallback<List<Produto>> callback) {
        //Buscando Dados locais com ROOM, enquanto a outra asynctask da requisição ainda não terminou
        buscaProdutosInternos(callback);
    }

    private void buscaProdutosInternos(DadosCarregadosCallback<List<Produto>> callback) {
        new BaseAsyncTask<>(dao::buscaTodos,
                resultado -> {
                    // Notifica que o dado esta pronto
                    callback.quandoSucesso(resultado);
                    buscaProdutosNaApi(callback);
                }).execute();
    }

    private void buscaProdutosNaApi(DadosCarregadosCallback<List<Produto>> callback) {
        ProdutoService service = new EstoqueRetrofit().getProdutoService();
        Call<List<Produto>> call = service.buscaTodos();

        call.enqueue(new BaseCallback<>(new BaseCallback.RespostaCallback<List<Produto>>() {
            @Override
            public void quandoSucesso(List<Produto> produtosNovos) {
                atualizaInterno(produtosNovos, callback);
            }

            @Override
            public void quandoFalha(String erro) {
                callback.quandoFalha(erro);
            }
        }));
    }

    private void atualizaInterno(List<Produto> produtos,
                                 DadosCarregadosCallback<List<Produto>> callback) {
        new BaseAsyncTask<>(() -> {
            dao.salva(produtos);
            return dao.buscaTodos();
        }, callback::quandoSucesso).execute();
    }

    public void salva(Produto produto, DadosCarregadosCallback<Produto> callback) {
        salvaNaApi(produto, callback);
    }

    private void salvaNaApi(Produto produto, DadosCarregadosCallback<Produto> callback) {
        Call<Produto> call = service.salva(produto);

        //enqueue = método para executar asincrona sem ter a necessidade de fazer uma asyncTask, não precisamos usar            o execute tambem usando ele
        call.enqueue(new BaseCallback<>(new BaseCallback.RespostaCallback<Produto>() {
            @Override
            public void quandoSucesso(Produto produtoSalvo) {
                salvaInterno(produtoSalvo, callback);
            }

            @Override
            public void quandoFalha(String erro) {
                callback.quandoFalha(erro);
            }
        }));
    }

    private void salvaInterno(Produto produto, DadosCarregadosCallback<Produto> callback) {
        new BaseAsyncTask<>(() -> {
            long id = dao.salva(produto);
            return dao.buscaProduto(id);
            //Notifica a nossa activity
        }, callback::quandoSucesso)
                .execute();
    }

    public interface DadosCarregadosCallback<T> {
        void quandoSucesso(T resultado);

        void quandoFalha(String erro);
    }
}
