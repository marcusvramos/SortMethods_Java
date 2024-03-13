package Vetor;

import Lista.*;

import java.util.Arrays;
import java.util.Stack;

public class Vetor {
    private int[] vet;
    private int TL;

    public Vetor(int posicoes) {
        vet = new int[posicoes+1];
        TL = posicoes;
    }

    public void preencherVetorRandomico() {
        for (int i = 0, j = 2; i < TL; i++, j++){
            vet[i] = (int) Math.floor(Math.random() * j);
        }
    }

    public void imprimiVetor(){
        System.out.print("Vetor: ");
        for(int i = 0; i < TL; i++){
            System.out.print(" " + vet[i]);
        }
    }

    public void insercao_direta() {
        int aux, pos, i = 1;
        while (i < TL) {
            aux = vet[i];
            pos = i;
            while(pos > 0 && aux < vet[pos-1]) {
                vet[pos] = vet[pos-1];
                pos--;
            }
            vet[pos] = aux;
            i++;
        }
    }

    public void insercao_binaria() {
        int pos, aux;
        for(int i =1; i < TL; i++) {
            aux = vet[i];
            pos = this.busca_binaria(aux, i);
            for(int j = i; j > pos; j--) {
                vet[j] = vet[j-1];
            }
            vet[pos] = aux;
        }
    }

    public int busca_binaria(int chave, int TL) {
        int ini = 0, fim = TL-1, meio = fim/2;
        while (ini < fim && chave != vet[meio]) {
            if (chave < vet[meio]) {
                fim = meio - 1;
            } else {
                ini = meio + 1;
            }
            meio = (ini + fim) / 2;
        }
        if (chave > vet[meio]) {
            return meio + 1;
        }

        return meio;
    }

    public void shake_sort() {
        int ini = 0, fim = TL - 1, i, j, aux;
        while (ini < fim) {
            for (i = ini; i < fim; i++) {
                if (vet[i] > vet[i+1]) {
                    aux = vet[i];
                    vet[i] = vet[i+1];
                    vet[i+1] = aux;
                }
            }
            fim--;
            for (j = fim; j > ini; j--) {
                if (vet[j] < vet[j-1]) {
                    aux = vet[j];
                    vet[j] = vet[j-1];
                    vet[j-1] = aux;
                }
            }
            ini++;
        }
    }

    public void selecaoDireta() {
        int i, j, pos_menor;
        for (i = 0; i < TL-1; i++) {
            pos_menor = i;
            for (j = i+1; j < TL; j++) {
                if (vet[j] < vet[pos_menor]) {
                    pos_menor = j;
                }
            }

            if (pos_menor != i) {
                int aux = vet[i];
                vet[i] = vet[pos_menor];
                vet[pos_menor] = aux;
            }
        }
    }

    public void heapSort() {
        int n = TL, FE, FD, aux;
        while (n > 1) {
            for(int pai = n / 2 - 1; pai >= 0; pai--) {
                FE = 2 * pai + 1;
                FD = 2 * pai + 2;
                int posMaior = FE;
                if(FD < n && vet[FD] > vet[FE]) {
                    posMaior = FD;
                }

                if (vet[posMaior] > vet[pai]) {
                    aux = vet[pai];
                    vet[pai] = vet[posMaior];
                    vet[posMaior] = aux;
                }
            }

            aux = vet[0];
            vet[0] = vet[n-1];
            vet[n-1] = aux;
            n--;
        }
    }


    public void bolha() {
        boolean precisaOrdenar = true;
        for (int i = 0; i < TL && precisaOrdenar; i++) {
            precisaOrdenar = false;
            for (int j = 0; j < TL - 1 - i; j++) {
                if (vet[j] > vet[j + 1]) {
                    int aux = vet[j];
                    vet[j] = vet[j + 1];
                    vet[j + 1] = aux;
                    precisaOrdenar = true;
                }
            }
        }
    }

    public void combSort() {
        int gap = (int) (TL / 1.3);
        int index;

        while(gap > 0) {
            index = 0;
            while((index + gap) < TL) {
                if(vet[index] > vet[index + gap]) {
                    int temp = vet[index];
                    vet[index] = vet[index + gap];
                    vet[index + gap] = temp;
                }
                index++;
            }
            gap = (int) (gap / 1.3);
        }
    }

    public void shellSort() {
        int i, j, aux, dist = 1;
        while (dist < TL) {
            dist = 3 * dist + 1;
        }
        dist = dist / 3;
        while (dist > 0) {
            for (i = dist; i < TL; i++) {
                aux = vet[i];
                j = i;
                while (j >= dist && vet[j - dist] > aux) {
                    vet[j] = vet[j - dist];
                    j = j - dist;
                }
                vet[j] = aux;
            }
            dist = dist / 3;
        }
    }

    public void coutingSort() {
        int n = TL;

        // Encontrando o máximo para saber o tamanho da matriz de contagem
        int max = vet[0];
        for (int i = 1; i < n; i++) {
            if (vet[i] > max) {
                max = vet[i];
            }
        }

        // Inicializa a matriz de contagem
        int[] count = new int[max + 1];

        // Conta cada elemento
        for (int i = 0; i < n; i++) {
            count[vet[i]]++;
        }

        // Calcula a soma cumulativa
        for (int i = 1; i <= max; i++) {
            count[i] += count[i-1];
        }

        // Coloca os elementos nas posições corretas
        int[] output = new int[n];
        for (int i = n - 1; i >= 0; i--) {
            output[count[vet[i]] - 1] = vet[i];
            count[vet[i]]--;
        }

        // Copia os elementos ordenados de volta para a matriz original
        System.arraycopy(output, 0, vet, 0, n);
    }

    public void bucketSort(int quantBuckets) {
        int tamanhoVet = TL;
        int maior = vet[0];
        int menor = vet[0];

        // Encontra o maior e o menor valor
        for (int i = 1; i < tamanhoVet; i++) {
            if (vet[i] > maior) maior = vet[i];
            if (vet[i] < menor) menor = vet[i];
        }

        int intervalo = (maior - menor) / quantBuckets + 1;

        // Cria os buckets como um array de Listas
        Lista[] buckets = new Lista[quantBuckets];
        for (int i = 0; i < quantBuckets; i++) {
            buckets[i] = new Lista();
        }

        // Distribui os elementos pelos buckets
        for (int i = 0; i < tamanhoVet; i++) {
            int index = (vet[i] - menor) / intervalo;
            buckets[index].inserirNoInicio(vet[i]);
        }

        // Ordena cada bucket e concatena de volta ao array original
        int index = 0;
        for (int i = 0; i < quantBuckets; i++) {
            buckets[i].insercaoDireta();
            No aux = buckets[i].getInicio();
            while (aux != null) {
                vet[index++] = aux.getInfo();
                aux = aux.getProx();
            }
        }
    }

    public void radixSort() {
        int n = TL;
        int max = getMaior();

        for (int exp = 1; max / exp > 0; exp *= 10) {
            countingSortWithRadix(n, exp);
        }
    }

    public void gnomeSort() {
        int n = TL;
        int index = 0;

        while (index < n) {
            if (index == 0) {
                index++;
            }
            if (vet[index] >= vet[index - 1]) {
                index++;
            } else {
                int temp = vet[index];
                vet[index] = vet[index - 1];
                vet[index - 1] = temp;
                index--;
            }
        }
    }

    public void timSort() {
        Arrays.sort(vet);
    }

    private void countingSortWithRadix(int n, int exp) {
        int[] output = new int[n];
        int[] count = new int[10]; // 0...9
        int i;

        // Armazene a contagem de ocorrências em count[]
        for (i = 0; i < n; i++)
            count[(vet[i] / exp) % 10]++;

        // acumalar a contagem
        for (i = 1; i < 10; i++)
            count[i] += count[i - 1];

        //  vetor de saída
        for (i = n - 1; i >= 0; i--) {
            output[count[(vet[i] / exp) % 10] - 1] = vet[i];
            count[(vet[i] / exp) % 10]--;
        }

        // copiando saída para vet -- números ordenados de acordo com o dígito atual
        for (i = 0; i < n; i++)
            vet[i] = output[i];
    }

    private int getMaior() {
        int maior = vet[0];
        for (int i = 1; i < TL; i++) {
            if (vet[i] > maior) {
                maior = vet[i];
            }
        }
        return maior;
    }

}