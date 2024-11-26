package br.com.alura.TabelaFipe.principal;

import br.com.alura.TabelaFipe.service.ConsumoApi;

import java.util.Scanner;

public class Principal {
  private Scanner leitura = new Scanner(System.in);
  private ConsumoApi consumoApi = new ConsumoApi();

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
      System.out.println("Opção invalida.");
    }

    var json = consumoApi.obterDados(endereco);
    System.out.println(json);
  }
}
