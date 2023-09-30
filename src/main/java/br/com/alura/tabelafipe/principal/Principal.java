package br.com.alura.tabelafipe.principal;

import br.com.alura.tabelafipe.model.*;
import br.com.alura.tabelafipe.service.ConsumoAPI;
import br.com.alura.tabelafipe.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    Scanner sc = new Scanner(System.in);

    ConsumoAPI consumo = new ConsumoAPI();

    ConverteDados conversor = new ConverteDados();

    public void exibeMenu() {

        System.out.println("Informe o tipo de veículo (carro, moto, ou caminhão): ");
        var veiculoEscolhido = sc.nextLine();
        var json = consumo.obterDados("https://parallelum.com.br/fipe/api/v1/" + veiculoEscolhido + "/marcas");
        var dadosMarcas = conversor.obterLista(json, Dados.class);
        dadosMarcas.stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("Informe o código da marca escolhida: ");
        var marcaEscolhida = sc.nextLine();
        json = consumo.obterDados("https://parallelum.com.br/fipe/api/v1/" + veiculoEscolhido + "/marcas/" + marcaEscolhida + "/modelos");
        var dadosModelos = conversor.obterDados(json, DadosModelo.class);
        dadosModelos.modelos().stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("Informe um trecho do nome do carro a ser buscado: ");
        var modeloEscolhido = sc.nextLine();

        List<Dados> modelosFiltrados = dadosModelos.modelos().stream()
                .filter(m -> m.nome().toLowerCase().contains(modeloEscolhido.toLowerCase()))
                .collect(Collectors.toList());

        System.out.println("Modelos filtrados");
        modelosFiltrados.forEach(System.out::println);

        System.out.println("Informe o código do modelo escolhido: ");
        var codigoModelo = sc.nextLine();
        json = consumo.obterDados("https://parallelum.com.br/fipe/api/v1/" + veiculoEscolhido + "/marcas/" + marcaEscolhida + "/modelos/" + codigoModelo + "/anos");
        var dadosAnos = conversor.obterLista(json, Dados.class);
        List<DadosVeiculo> veiculos = new ArrayList<>();

        for (int i = 0; i < dadosAnos.size(); i++) {
            json = consumo.obterDados("https://parallelum.com.br/fipe/api/v1/" + veiculoEscolhido + "/marcas/" + marcaEscolhida + "/modelos/" + codigoModelo + "/anos/" + dadosAnos.get(i).codigo());
            DadosVeiculo veiculo = conversor.obterDados(json, DadosVeiculo.class);
            veiculos.add(veiculo);
        }

        System.out.println("Todos os veículos filtrados com avaliações por ano: ");
        veiculos.forEach(System.out::println);

    }
}
