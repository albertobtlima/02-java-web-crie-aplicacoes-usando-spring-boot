package br.com.alura.TabelaFipe.principal;

import br.com.alura.TabelaFipe.model.Dados;
import br.com.alura.TabelaFipe.model.Modelos;
import br.com.alura.TabelaFipe.service.ConsumoApi;
import br.com.alura.TabelaFipe.service.ConverteDados;

import java.util.Comparator;
import java.util.Scanner;

public class Principal {
  private Scanner leitura = new Scanner(System.in);
  private ConsumoApi consumoApi = new ConsumoApi();
  private ConverteDados converteDados = new ConverteDados();

  private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";

  public void exibeMenu() {
    var menu = """
      *** OPÇÕES ***
      
      Digite uma das opções para consulta [1, 2 ou 3]:
      
      1 - Carro
      2 - Moto
      3 - Caminhão
      """;

    System.out.println(menu);
    var opcao = leitura.nextInt();

    String endereco = "";

    if (opcao == 1) {
      endereco = URL_BASE + "carros/marcas";
    } else if (opcao == 2) {
      endereco = URL_BASE + "motos/marcas";
    } else if (opcao == 3) {
      endereco = URL_BASE + "caminhoes/marcas";
    } else {
      System.out.println("Opção invalida!!!");
      exibeMenu();
    }

    var json = consumoApi.obterDados(endereco);
    var marcas = converteDados.obterLista(json, Dados.class);

    marcas.stream()
      .sorted(Comparator.comparing(Dados::nome))
      .forEach(System.out::println);

    System.out.println("Informe o código da marca para consulta: ");
    var codigoMarca = leitura.nextInt();

    endereco = endereco + "/" + codigoMarca + "/modelos";
    json = consumoApi.obterDados(endereco);
    var modeloLista = converteDados.obterDados(json, Modelos.class);

    System.out.println("\nModelos da marca escolhida: ");
    modeloLista.modelos().stream()
      .sorted(Comparator.comparing(Dados::nome))
      .forEach(System.out::println);
  }
}
