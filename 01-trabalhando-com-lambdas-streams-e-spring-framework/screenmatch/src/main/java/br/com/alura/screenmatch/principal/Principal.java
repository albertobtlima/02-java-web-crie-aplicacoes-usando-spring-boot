package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

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
//  temporadas.forEach(t -> System.out.println(t));
    temporadas.forEach(System.out::println);



//    for (int i = 0; i < dadosSerie.totalTemporadas(); i++) {
//      List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
//      for (int j = 0; j < episodiosTemporada.size(); j++) {
//        System.out.println(episodiosTemporada.get(j).titulo());
//      }
//    }

    temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

    List<DadosEpisodio> dadosEpisodios = temporadas.stream()
      .flatMap(t -> t.episodios().stream())
      .toList();

    System.out.println("\nTop 5 episódios");
    dadosEpisodios.stream()
      .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
      .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
      .limit(5)
      .forEach(System.out::println);

    List<Episodio> episodios = temporadas.stream()
      .flatMap(t -> t.episodios().stream()
        .map(d -> new Episodio(t.temporada(), d))
      ).collect(Collectors.toList());

    episodios.forEach(System.out::println);
  }
}
