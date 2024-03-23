import Arquivo.Arquivo;
import Lista.Lista;

import java.util.function.Consumer;

public class MainListas {

    public static void testarOrdenacao(String nomeMetodo, Consumer<Lista> metodoOrdenacao) {
        Lista lista = new Lista();

        int[] values = {4, 2, 1, 11, 6, 12, 9, 7};

        for (int valor : values) {
            lista.inserirNoInicio(valor);
        }

        System.out.println("\n\nLista (original): ");
        lista.exibir();

        System.out.println("\n\nLista ordenada por " + nomeMetodo + ": ");
        metodoOrdenacao.accept(lista);
        lista.exibir();
    }
    public static void main(String[] args) {
        testarOrdenacao("Insercao Direta", Lista::insercaoDireta);
        testarOrdenacao("Insercao Binária", Lista::insercaoBinaria);
        testarOrdenacao("Selecao Direta", Lista::selecaoDireta);
        testarOrdenacao("Bolha", Lista::bolha);
        testarOrdenacao("Shake", Lista::shakeSort);
        testarOrdenacao("Shell", Lista::shellSort);
        testarOrdenacao("Heap", Lista::heapSort);
        testarOrdenacao("Quick Sem Pivô", Lista::quickSortSemPivo);
        testarOrdenacao("Quick Com Pivô", Lista::quickSortComPivo);
        testarOrdenacao("Merge- 1º Implementação", Lista::mergePrimeiraImplementacao);
        testarOrdenacao("Merge- 2º Implementação", Lista::mergeSegundaImplementacao);
        testarOrdenacao("Counting", Lista::countingSort);
        testarOrdenacao("Bucket", lista -> lista.bucketSort(5));
        testarOrdenacao("Radix", Lista::radixSort);
        testarOrdenacao("CombSort", Lista::combSort);
        testarOrdenacao("GnomeSort", Lista::gnomeSort);
        testarOrdenacao("TimSort", Lista::timSort);
    }
}
