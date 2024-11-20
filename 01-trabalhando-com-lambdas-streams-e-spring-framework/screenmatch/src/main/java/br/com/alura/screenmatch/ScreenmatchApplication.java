package br.com.alura.screenmatch;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

  public static void main(String[] args) {
    SpringApplication.run(ScreenmatchApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    var consumoApi = new ConsumoApi();
    var json = consumoApi.obterDados("https://www.omdbapi.com/?t=gilmore+girls&apikey=3cc1d164");

    ConverteDados conversor = new ConverteDados();

    DadosSerie dadosSerie = conversor.obterDados(json, DadosSerie.class);
    System.out.println(dadosSerie);

    json = consumoApi.obterDados("https://www.omdbapi.com/?t=gilmore+girls&season=1&episode=2&apikey=3cc1d164");
    DadosEpisodio dadosEpisodio = conversor.obterDados(json, DadosEpisodio.class);
    System.out.println(dadosEpisodio);

    List<DadosTemporada> temporadas = new ArrayList<>();

    for (int i = 1; i < dadosSerie.totalTemporadas(); i++) {
      json = consumoApi.obterDados("https://www.omdbapi.com/?t=gilmore+girls&season=" + i + "&apikey=3cc1d164");
      DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
      temporadas.add(dadosTemporada);
    }
    temporadas.forEach(System.out::println);
  }
}
