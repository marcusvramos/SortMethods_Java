package Lista;

import java.sql.SQLOutput;

public class Lista {
    private No inicio;
    private No fim;

    public Lista() {
        this.inicializa();
    }

    public void inicializa() {
        inicio = null;
        fim = null;
    }

    public No getInicio() {
        return inicio;
    }

    public void inserirNoInicio(int info) {
        No novo = new No(null, inicio, info);
        if (inicio == null) {
            inicio = fim = novo;
        } else {
            inicio.setAnt(novo);
            inicio = novo;
        }
    }

    public void inserirNoFim(int info) {
        No novo = new No(fim, null, info);
        if(inicio == null) {
            inicio = fim = novo;
        } else {
            fim.setProx(novo);
            fim = novo;
        }
    }

    public void exibir() {
        No aux = inicio;
        while(aux != null){
            System.out.print(aux.getInfo() + " ");
            aux = aux.getProx();
        }
    }

    public No buscaExaustiva(int info) {
        No aux = inicio;
        while(aux != null && aux.getInfo() != info) {
            aux = aux.getProx();
        }
        return aux;
    }

    public void remover(int info) {
        No atual = buscaExaustiva(info);

        if (atual != null) {
            if (inicio == fim) {
                inicio = fim = null;
            } else if (atual == inicio) {
                inicio = inicio.getProx();
                inicio.setAnt(null);
            } else if (atual == fim) {
                fim = fim.getAnt();
                fim.setProx(null);
            } else {
                atual.getAnt().setProx(atual.getProx());
                atual.getProx().setAnt(atual.getAnt());
            }
        }
    }

    public No buscaBinaria(int chave, No PontI) {
        No ini = inicio, end = PontI.getAnt(), meio = getMeio(ini, end);
        while(ini != end && chave != meio.getInfo()) {
            if (chave < meio.getInfo()) {
                end = meio.getAnt();
            } else {
                ini = meio.getProx();
            }
            meio = getMeio(ini, end);
        }

        if (chave > meio.getInfo()) {
            return meio.getProx();
        }

        return meio;
    }

    // ordenações em lista encadeada
    public void insercaoDireta() {
        if (inicio == null) {
            return;
        }

        int aux;
        No PonteiroIni = inicio.getProx(), PontPos;
        while(PonteiroIni != null) {
            aux = PonteiroIni.getInfo();
            PontPos = PonteiroIni;
            while(PontPos != inicio && aux < PontPos.getAnt().getInfo()) {
                PontPos.setInfo(PontPos.getAnt().getInfo());
                PontPos = PontPos.getAnt();
            }
            PontPos.setInfo(aux);
            PonteiroIni = PonteiroIni.getProx();
        }
    }

    public void insercaoBinaria() {
        No PontPos, PontI = inicio.getProx(), PontJ;
        int aux;
        while(PontI != null) {
            aux = PontI.getInfo();
            PontPos = buscaBinaria(aux, PontI);

            PontJ = PontI;
            while(PontJ != PontPos) {
                PontJ.setInfo(PontJ.getAnt().getInfo());
                PontJ = PontJ.getAnt();
            }

            PontPos.setInfo(aux);
            PontI = PontI.getProx();
        }
    }

    public void selecaoDireta() {
        No PontI = inicio;

        while(PontI.getProx() != null) {
            No PosMenor = PontI;
            No PontJ = PontI.getProx();

            while(PontJ != null) {
                if (PontJ.getInfo() < PosMenor.getInfo()) {
                    PosMenor = PontJ;
                }
                PontJ = PontJ.getProx();
            }

            if(PosMenor != PontI) {
                int aux = PontI.getInfo();
                PontI.setInfo(PosMenor.getInfo());
                PosMenor.setInfo(aux);
            }
            PontI = PontI.getProx();
        }
    }

    public void bolha() {
        boolean precisaOrdenar = true;
        int aux;
        No PontI = fim, PontJ;
        while(PontI != null && precisaOrdenar) {
            precisaOrdenar = false;
            PontJ = inicio;
            while(PontJ != PontI) {
                if (PontJ.getInfo() > PontJ.getProx().getInfo()) {
                    aux = PontJ.getInfo();
                    PontJ.setInfo(PontJ.getProx().getInfo());
                    PontJ.getProx().setInfo(aux);
                    precisaOrdenar = true;
                }
                PontJ = PontJ.getProx();
            }
            PontI = PontI.getAnt();
        }
    }

    public void shakeSort() {
        No ini = inicio, fim = this.fim;
        No i, j;
        int aux;
        boolean troca = true;
        while(ini != fim && troca) {
            troca = false;
            for(i = ini; i != fim; i = i.getProx()) {
                if(i.getInfo() > i.getProx().getInfo()) {
                    aux = i.getInfo();
                    i.setInfo(i.getProx().getInfo());
                    i.getProx().setInfo(aux);
                    troca = true;
                }
            }
            fim = fim.getAnt();
            if (troca) {
                troca = false;
                for(j = fim; j != ini; j = j.getAnt()) {
                    if(j.getInfo() < j.getAnt().getInfo()) {
                        aux = j.getInfo();
                        j.setInfo(j.getAnt().getInfo());
                        j.getAnt().setInfo(aux);
                        troca = true;
                    }
                }
            }

            ini = ini.getProx();
        }
    }

    public void shellSort() {
        int i, j;
        int aux, dist = 1;
        while(dist < tamanho(inicio, null)) {
            dist = 3 * dist + 1;
        }
        dist = dist / 3;
        while(dist > 0) {
            for(i = dist; i < tamanho(inicio, null); i++) {
                aux = getNodeAt(i).getInfo();
                j = i;
                while(j - dist >= 0 && getNodeAt(j - dist).getInfo() > aux) {
                    getNodeAt(j).setInfo(getNodeAt(j - dist).getInfo());
                    j = j - dist;
                }
                getNodeAt(j).setInfo(aux);
            }
            dist = dist / 3;
        }
    }

    public void quickSortSemPivo() {
        quickSortSP(inicio, fim);
    }

    private void quickSortSP(No ini, No end) {
        if (ini.getAnt() == end || end.getProx() == ini)
            return;

        No i = ini, j = end;
        int aux;
        boolean flag = true;

        while (i != j) {
            if (flag) {
                while (i != j && i.getInfo() <= j.getInfo() && i.getProx() != null) {
                    i = i.getProx();
                }
            } else {
                while (i != j && i.getInfo() <= j.getInfo() && j.getAnt() != null) {
                    j = j.getAnt();
                }
            }

            if (i != j) {
                aux = i.getInfo();
                i.setInfo(j.getInfo());
                j.setInfo(aux);
                flag = !flag;
            }
        }

        if (ini != i && i.getAnt() != null) {
            quickSortSP(ini, i.getAnt());
        }

        if (j.getProx() != end && j.getProx() != null) {
            quickSortSP(j.getProx(), end);
        }
    }

    public void quickSortComPivo() {
        quickSortCP(0, tamanho(inicio, null) - 1);
    }

    public void quickSortCP(int ini, int fim){
        int i = ini, j = fim, aux;
        No pivo = getNodeAt((ini + fim) / 2);
        No pi, pj;
        while (i < j) {
            pi = getNodeAt(i);
            pj = getNodeAt(j);
            while (i < j && pi.getInfo() < pivo.getInfo()) {
                i++;
                pi = pi.getProx();
            }
            while (i < j && pj.getInfo() > pivo.getInfo()) {
                j--;
                pj = pj.getAnt();
            }
            if (i <= j) {
                aux = pi.getInfo();
                pi.setInfo(pj.getInfo());
                pj.setInfo(aux);
                i++;
                j--;
            }
        }
        if (ini < j) {
            quickSortCP(ini, j);
        }
        if (i < fim) {
            quickSortCP(i, fim);
        }
    }

    public void mergePrimeiraImplementacao() {
        int seq = 1;
        while (seq < tamanho(inicio, null)) {
            Lista lista01 = new Lista();
            Lista lista02 = new Lista();
            lista01.inicializa();
            lista02.inicializa();
            particaoPrimeiraImp(lista01, lista02);
            fusaoPrimeiraImp(lista01, lista02, seq);
            seq = seq * 2;
        }
    }


    public void particaoPrimeiraImp(Lista lista01, Lista lista02) {
        int tam = tamanho(inicio, null) / 2;
        for (int i = 0; i < tam; i++) {
            lista01.inserirNoFim(getNodeAt(i).getInfo());
            lista02.inserirNoFim(getNodeAt(tam + i).getInfo());
        }
    }

    public void fusaoPrimeiraImp(Lista lista01, Lista lista02, int seq) {
        int i = 0, j = 0, k = 0, seq_aux = seq;
        int TL = tamanho(inicio, null);
        while(k < TL) {
            while (i < seq && j < seq) {
                if (lista01.getNodeAt(i).getInfo() < lista02.getNodeAt(j).getInfo()) {
                    getNodeAt(k).setInfo(lista01.getNodeAt(i).getInfo());
                    i++;
                    k++;
                } else {
                    getNodeAt(k).setInfo(lista02.getNodeAt(j).getInfo());
                    j++;
                    k++;
                }
            }

            while (i < seq) {
                getNodeAt(k).setInfo(lista01.getNodeAt(i).getInfo());
                i++;
                k++;
            }

            while (j < seq) {
                getNodeAt(k).setInfo(lista02.getNodeAt(j).getInfo());
                j++;
                k++;
            }
            seq = seq + seq_aux;
        }
    }

    public void mergeSegundaImplementacao() {
        Lista aux = new Lista();
        aux.inicializa();
        particaoSegundaImp(0, tamanho(inicio, null) - 1, aux);
    }

    public void particaoSegundaImp(int esq, int dir, Lista lista)  {
        int meio;
        if (esq < dir) {
            meio = (esq + dir) / 2;
            particaoSegundaImp(esq, meio, lista);
            particaoSegundaImp(meio + 1, dir, lista);
            fusaoSegundaImp(esq, meio, meio + 1, dir, lista);
        }
    }

    public void fusaoSegundaImp(int ini1, int fim1, int ini2, int fim2, Lista lista) {
        int i = ini1, j = ini2;
        No p1 = getNodeAt(ini1), p2 = getNodeAt(ini2);

        while (p1 != null && i <= fim1 && p2 != null && j <= fim2) {
            if (p1.getInfo() < p2.getInfo()) {
                lista.inserirNoFim(p1.getInfo());
                p1 = p1.getProx();
                i++;
            } else {
                lista.inserirNoFim(p2.getInfo());
                p2 = p2.getProx();
                j++;
            }
        }

        while (p1 != null && i <= fim1) {
            lista.inserirNoFim(p1.getInfo());
            p1 = p1.getProx();
            i++;
        }

        while (p2 != null && j <= fim2) {
            lista.inserirNoFim(p2.getInfo());
            p2 = p2.getProx();
            j++;
        }

        No aux = lista.getInicio();
        No original = getNodeAt(ini1);
        while (aux != null && original != null && ini1 <= fim2) {
            original.setInfo(aux.getInfo());
            aux = aux.getProx();
            original = original.getProx();
            ini1++;
        }
    }


    public void heapSort(){
        int tl2 = tamanho(inicio, null), pai, fe, fd, info;
        No PontFe, PontFd, PontMaiorF, PontPai, ini, fim;
        while (tl2 > 1){
            for(pai = tl2/2 - 1, PontPai = getNodeAt(pai); PontPai != null; PontPai = PontPai.getAnt(), pai--){
                fe = 2*pai+1;
                PontFe = getNodeAt(fe);
                fd = fe + 1;
                PontFd = getNodeAt(fd);
                PontMaiorF = PontFe;
                if (fd < tl2 && PontFd.getInfo() > PontFe.getInfo()) {
                    PontMaiorF = PontFd;
                }
                if (PontMaiorF.getInfo() > PontPai.getInfo()) {
                    info = PontPai.getInfo();
                    PontPai.setInfo(PontMaiorF.getInfo());
                    PontMaiorF.setInfo(info);
                }
            }
            ini = getNodeAt(0);
            fim = getNodeAt(tl2 - 1);
            info = ini.getInfo();
            ini.setInfo(fim.getInfo());
            fim.setInfo(info);
            tl2--;
        }
    }

    public void countingSort() {
        int maior = 0;
        No aux = inicio;
        // encontra o maior elemento da lista
        while(aux != null) {
            if(aux.getInfo() > maior) {
                maior = aux.getInfo();
            }
            aux = aux.getProx();
        }

        // vetor auxiliar para contar a frequência de cada elemento
        int[] vetorAux = new int[maior + 1];
        aux = inicio;
        while(aux != null) {
            vetorAux[aux.getInfo()]++;
            aux = aux.getProx();
        }

        // acumular a frequência
        for(int i = 1; i < vetorAux.length; i++) {
            vetorAux[i] += vetorAux[i - 1];
        }

        // vetor de saída
        int[] vetorSaida = new int[tamanho(inicio, null)];
        aux = fim;
        while(aux != null) {
            vetorSaida[vetorAux[aux.getInfo()] - 1] = aux.getInfo();
            vetorAux[aux.getInfo()]--;
            aux = aux.getAnt();
        }

        // copiar o vetor de saída para a lista
        aux = inicio;
        for(int i = 0; i < vetorSaida.length; i++) {
            aux.setInfo(vetorSaida[i]);
            aux = aux.getProx();
        }
    }

    public void bucketSort(int quantidadeBuckets) {
        int maior = inicio.getInfo();
        int menor = inicio.getInfo();
        No temp = inicio.getProx();
        while (temp != null) {
            if (temp.getInfo() > maior) maior = temp.getInfo();
            if (temp.getInfo() < menor) menor = temp.getInfo();
            temp = temp.getProx();
        }

        int intervalo = maior - menor + 1;
        double intervaloPorBalde = (double) intervalo / quantidadeBuckets;

        // Criar e inicializar os baldes
        Lista[] baldes = new Lista[quantidadeBuckets];
        for (int i = 0; i < quantidadeBuckets; i++) {
            baldes[i] = new Lista();
        }

        // Distribuir os elementos pelos baldes
        temp = inicio;
        while (temp != null) {
            int index = (int)((temp.getInfo() - menor) / intervaloPorBalde);
            index = Math.min(index, quantidadeBuckets - 1);
            baldes[index].inserirNoInicio(temp.getInfo());
            temp = temp.getProx();
        }

        // Ordenar cada balde e concatená-los de volta na lista original
        inicializa();
        for (int i = 0; i < quantidadeBuckets; i++) {
            baldes[i].insercaoDireta();

            temp = baldes[i].getInicio();
            while (temp != null) {
                inserirNoFim(temp.getInfo());
                temp = temp.getProx();
            }
        }
    }

    public void radixSort() {
        int max = getMaior();

        for (int exp = 1; max / exp > 0; exp *= 10) {
            countingSortWithRadix(exp);
        }
    }

    private void countingSortWithRadix(int exp) {
        int[] output = new int[tamanho(inicio, null)]; // Array de saída que terá os números ordenados
        int i;
        int[] count = new int[10];

        // Armazena a contagem de ocorrências em count[]
        No temp = inicio;
        while (temp != null) {
            count[(temp.getInfo() / exp) % 10]++;
            temp = temp.getProx();
        }

        // acumula os valores de count
        for (i = 1; i < 10; i++)
            count[i] += count[i - 1];

        // Constrói o array de saída
        temp = fim; // Começa do fim para manter a estabilidade
        i = tamanho(inicio, null) - 1;
        while (temp != null) {
            output[count[(temp.getInfo() / exp) % 10] - 1] = temp.getInfo();
            count[(temp.getInfo() / exp) % 10]--;
            temp = temp.getAnt();
        }

        // Copia o array de saída para a lista, para que a lista agora contenha os números ordenados
        temp = inicio;
        for (i = 0; i < output.length; i++) {
            temp.setInfo(output[i]);
            temp = temp.getProx();
        }
    }

    public void combSort() {
        int n = tamanho(inicio, null);
        int gap = (int) (n / 1.3);
        int index;

        while (gap > 0 ) {
            index = 0;
            while (index + gap < n) {
                No i = getNodeAt(index);
                No j = getNodeAt(index + gap);
                if (i.getInfo() > j.getInfo()) {
                    int temp = i.getInfo();
                    i.setInfo(j.getInfo());
                    j.setInfo(temp);
                }
                index++;
            }
            gap = (int) (gap / 1.3);
        }
    }

    public void gnomeSort() {
        int n = tamanho(inicio, null);
        int index = 0;

        while (index < n) {
            if (index == 0) {
                index++;
            }
            if (getNodeAt(index).getInfo() >= getNodeAt(index - 1).getInfo()) {
                index++;
            } else {
                int temp = getNodeAt(index).getInfo();
                getNodeAt(index).setInfo(getNodeAt(index - 1).getInfo());
                getNodeAt(index - 1).setInfo(temp);
                index--;
            }
        }
    }

    private void insertionSortForTim(No ini, No fim) {
        // eu poderia ter usado o insertionSort que já existe, mas preferi fazer um novo
        if (ini == null || fim == null) {
            return;
        }

        No PonteiroIni = ini.getProx();
        No PontPos, PontAux;

        while (PonteiroIni != null && PonteiroIni != fim.getProx()) {
            int aux = PonteiroIni.getInfo();
            PontPos = PonteiroIni;

            while (PontPos != ini && aux < PontPos.getAnt().getInfo()) {
                PontPos.setInfo(PontPos.getAnt().getInfo());
                PontPos = PontPos.getAnt();
            }

            PontPos.setInfo(aux);
            PonteiroIni = PonteiroIni.getProx();
        }
    }

    public void timSort() {
        int n = tamanho(inicio, null);
        int RUN = 32;
        for (int i = 0; i < n; i += RUN) {
            insertionSortForTim(getNodeAt(i), getNodeAt(Math.min((i + 31), (n - 1))));
        }

        for (int size = RUN; size < n; size = 2 * size) {
            for (int left = 0; left < n; left += 2 * size) {
                int mid = left + size - 1;
                int right = Math.min((left + 2 * size - 1), (n - 1));
                fusaoSegundaImp(left, mid, mid + 1, right, new Lista());
            }
        }
    }

    private int getMaior() {
        No temp = inicio;
        int maior = temp.getInfo();
        while (temp != null) {
            if (temp.getInfo() > maior) {
                maior = temp.getInfo();
            }
            temp = temp.getProx();
        }
        return maior;
    }

    private No getNodeAt(int index) {
        No atual = inicio;
        int contador = 0;
        while (contador < index && atual != null) {
            atual = atual.getProx();
            contador++;
        }
        return atual;
    }

    public int tamanho(No ini, No end) {
        int tam = 0;
        No aux = ini;
        while(aux != null && aux != end) {
            tam++;
            aux = aux.getProx();
        }
        return tam;
    }

    public No getMeio(No start, No end) {
        int meio = tamanho(start, end) / 2;
        No aux = start;
        for (int i = 0; i < meio && aux != end; i++) {
            aux = aux.getProx();
        }
        return aux;
    }
}

