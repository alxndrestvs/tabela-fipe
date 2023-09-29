package br.com.alura.tabelafipe.principal;

import br.com.alura.tabelafipe.model.*;
import br.com.alura.tabelafipe.service.ConsumoAPI;
import br.com.alura.tabelafipe.service.ConverteDados;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

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

        System.out.println("Informe o código do modelo escolhido: ");
        var modeloEscolhido = sc.nextLine();
        json = consumo.obterDados("https://parallelum.com.br/fipe/api/v1/" + veiculoEscolhido + "/marcas/" + marcaEscolhida + "/modelos/" + modeloEscolhido + "/anos");
        var dadosAnos = conversor.obterLista(json, Dados.class);
        dadosAnos.forEach(System.out::println);

        System.out.println("Informe o ano escolhido: ");
        var anoEscolhido = sc.nextLine();
        json = consumo.obterDados("https://parallelum.com.br/fipe/api/v1/" + veiculoEscolhido + "/marcas/" + marcaEscolhida + "/modelos/" + modeloEscolhido + "/anos/" + anoEscolhido);
        DadosVeiculo dadosVeiculo = conversor.obterDados(json, DadosVeiculo.class);
        System.out.println(dadosVeiculo);

    }
}
