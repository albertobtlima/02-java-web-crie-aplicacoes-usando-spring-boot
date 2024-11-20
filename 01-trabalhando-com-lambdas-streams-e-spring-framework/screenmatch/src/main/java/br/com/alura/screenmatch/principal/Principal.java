package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {
  private final Scanner leitura = new Scanner(System.in);
  private final ConsumoApi consumoApi = new ConsumoApi();
  private final ConverteDados conversor = new ConverteDados();

  private final String ENDERECO = "https://www.omdbapi.com/?t=";
  private final String API_KEY = "&apikey=3cc1d164";

  public void exibeMenu() {
    System.out.println("Digite o nome da série: ");
    var nomeSerie = leitura.nextLine();
    var json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(
      " ", "+") + API_KEY);

    DadosSerie dadosSerie = conversor.obterDados(json, DadosSerie.class);
    System.out.println(dadosSerie);

    List<DadosTemporada> temporadas = new ArrayList<>();

    for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
      json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(
        " ", "+")+ "&season=" + i + API_KEY);
      DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
      temporadas.add(dadosTemporada);
    }
    temporadas.forEach(System.out::println);
  }
}
