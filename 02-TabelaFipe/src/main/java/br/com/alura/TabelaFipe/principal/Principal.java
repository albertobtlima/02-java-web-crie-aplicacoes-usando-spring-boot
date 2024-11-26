package br.com.alura.TabelaFipe.principal;

import br.com.alura.TabelaFipe.model.Dados;
import br.com.alura.TabelaFipe.model.Modelos;
import br.com.alura.TabelaFipe.model.Veiculo;
import br.com.alura.TabelaFipe.service.ConsumoApi;
import br.com.alura.TabelaFipe.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Principal {
  private final Scanner leitura = new Scanner(System.in);
  private final ConsumoApi consumoApi = new ConsumoApi();
  private final ConverteDados converteDados = new ConverteDados();

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

    System.out.println("\nDigite o nome do veículo a ser buscado: ");
    var nomeVeiculo = leitura.nextLine();

    List<Dados> modelosFiltrados = modeloLista.modelos().stream()
      .filter(m -> m.nome().toLowerCase().contains(nomeVeiculo.toLowerCase()))
      .toList();

    System.out.println("\nModelos filtrados: ");
    modelosFiltrados.forEach(System.out::println);

    System.out.println("Digite o código do veículo: ");
    var codigoModelo = leitura.nextLine();

    endereco = endereco + "/" + codigoModelo + "/anos";
    json = consumoApi.obterDados(endereco);
    List<Dados> anos = converteDados.obterLista(json, Dados.class);
    List<Veiculo> veiculos = new ArrayList<>();

    for (Dados ano : anos) {
      var enderecoAnos = endereco + "/" + ano.codigo();
      json = consumoApi.obterDados(enderecoAnos);
      Veiculo veiculo = converteDados.obterDados(json, Veiculo.class);
      veiculos.add(veiculo);
    }

    System.out.println("\nTodos os veículos filtrados com avaliações por ano: ");
    veiculos.forEach(System.out::println);
  }
}
