import Lista.Lista;

import java.util.function.Consumer;

public class TesteListas {

    public static void testarOrdenacao(String nomeMetodo, Consumer<Lista> metodoOrdenacao) {
        Lista lista = new Lista();
        int[] valores = {55, 17, 23, 12, 42};
        for (int valor : valores) {
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

        // testarOrdenacao("Quick", Lista::quickSort);
        //testarOrdenacao("Merge", Lista::mergeSort);

        testarOrdenacao("Counting", Lista::countingSort);
        testarOrdenacao("Bucket", lista -> lista.bucketSort(5));
        testarOrdenacao("Radix", Lista::radixSort);
        testarOrdenacao("CombSort", Lista::combSort);
        testarOrdenacao("GnomeSort", Lista::gnomeSort);
    }
}
