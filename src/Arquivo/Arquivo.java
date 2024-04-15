package Arquivo;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

public class Arquivo {
    private String nomearquivo;
    private RandomAccessFile arquivo;
    private int comp, mov;

    public int getComp() {
        return comp;
    }

    public int getMov() {
        return mov;
    }

    public void initComp() {
        comp = 0;
    }

    public void initMov() {
        mov = 0;
    }

    public Arquivo(String nomearquivo)
    {
        try
        {
            arquivo = new RandomAccessFile(nomearquivo, "rw");
        } catch (IOException e)
        { }
    }

    public void deletarArquivo() {
        try {
            arquivo.close();
            arquivo = null;
            System.gc();
            System.runFinalization();
        } catch (IOException e) {
            System.out.println("Erro ao deletar o arquivo!");
        }
    }

    public void arquivoOrdenado() {
        truncate(0);

        for (int i = 0; i < 1024; i++) {
            new Registro(i).gravaNoArq(arquivo);
        }
    }

    public void arquivoReverso() {
        truncate(0);

        for (int i = 1024; i > 0; i--)
            new Registro(i).gravaNoArq(arquivo);
    }

    public void arquivoRandomico() {
        truncate(0);
        for (int i = 0; i < 1024; i++)
            new Registro(new Random().nextInt(1024)).gravaNoArq(arquivo);

    }

    public void copiaArquivo(Arquivo aux1){
        Registro reg = new Registro();

        seekArq(0);
        aux1.truncate(0);
        aux1.seekArq(0);
        while(!eof()){
            reg.leDoArq(arquivo);
            reg.gravaNoArq(aux1.arquivo);
        }
    }

    public void truncate(long pos) //desloca eof
    {
        try
        {
            arquivo.setLength(pos * Registro.length());
        } catch (IOException exc)
        { }
    }

    public boolean eof()
    {
        boolean retorno = false;
        try
        {
            if (arquivo.getFilePointer() == arquivo.length())
                retorno = true;
        } catch (IOException e)
        { }
        return (retorno);
    }

    public void inserirRegNoFinal(int i)
    {
        Registro reg = new Registro(i);
        seekArq(filesize());//ultimo byte
        reg.gravaNoArq(arquivo);
    }

    public int filesize()
    {
        try
        {
            return (int) (arquivo.length() / Registro.length());
        } catch (IOException e)
        { }

        return 0;
    }

    public void fechar()
    {
        try
        {
            arquivo.close();
        } catch (IOException e)
        { }
    }

    public void exibirArq()
    {
        int i;
        Registro aux = new Registro();
        seekArq(0);
        i = 0;
        while (!this.eof())
        {
            System.out.println("Posicao " + i);
            aux.leDoArq(arquivo);
            aux.exibirRegistro();
            i++;
        }
    }

    public void exibirUmRegistro(int pos)
    {
        Registro aux = new Registro();
        seekArq(pos);
        System.out.println("Posicao " + pos);
        aux.leDoArq(arquivo);
        aux.exibirRegistro();
    }

    public void seekArq(int pos)
    {
        try
        {
            arquivo.seek(pos * Registro.length());
        } catch (IOException e)
        { }
    }

    public void insercaoDireta() {
        int i = 1;
        while(i < filesize()) {
            seekArq(i);
            Registro regI = new Registro();
            regI.leDoArq(arquivo);
            Registro regAuxiliar = new Registro(regI.getCodigo());
            int pos = i;

            seekArq(pos - 1);
            Registro regAnterior = new Registro();
            regAnterior.leDoArq(arquivo);

            comp++;
            while (pos > 0 && regAuxiliar.getCodigo() < regAnterior.getCodigo()) {
                mov++;
                seekArq(pos);
                regAnterior.gravaNoArq(arquivo);
                pos--;

                if (pos > 0) {
                    seekArq(pos - 1);
                    mov++;
                    regAnterior.leDoArq(arquivo);
                }
            }

            mov++;
            seekArq(pos);
            regAuxiliar.gravaNoArq(arquivo);
            i++;
        }
    }

    public void insercaoBinaria() {
        int pos;
        Registro regI = new Registro();
        Registro regAux;
        Registro regAnt = new Registro();
        for (int i = 1; i < filesize(); i++) {
            seekArq(i);
            regI.leDoArq(arquivo);
            regAux = regI;
            pos = buscaBinaria(regAux.getCodigo(), i);
            for (int j = i; j > pos; j--) {
                seekArq(j - 1);
                regAnt.leDoArq(arquivo);
                seekArq(j);
                regAnt.gravaNoArq(arquivo);
                mov++;
            }
            seekArq(pos);
            regAux.gravaNoArq(arquivo);
            mov++;
        }
    }

    private int buscaBinaria(int chave, int TL) {
        int ini = 0, fim = TL - 1, meio = fim / 2;
        Registro regMeio = new Registro();
        seekArq(meio);
        regMeio.leDoArq(arquivo);
        while (ini < fim && regMeio.getCodigo() != chave) {
            comp++;
            if (chave < regMeio.getCodigo()) {
                fim = meio - 1;
            } else {
                ini = meio + 1;
            }
            meio = (ini + fim) / 2;
            seekArq(meio);
            regMeio.leDoArq(arquivo);
        }
        comp++;
        if (chave > regMeio.getCodigo()) {
            return meio + 1;
        }
        return meio;
    }

    public void selecaoDireta() {
        int tamanho = filesize();
        Registro regAtual = new Registro();
        Registro regMenor = new Registro();
        Registro regTemp = new Registro();

        for(int i = 0; i < tamanho - 1; i++) {
            int posMenor = i;
            seekArq(i);
            regMenor.leDoArq(arquivo);
            for (int j = i + 1; j < tamanho; j++) {
                seekArq(j);
                regAtual.leDoArq(arquivo);
                comp++;
                if (regAtual.getCodigo() < regMenor.getCodigo()) {
                    regMenor = new Registro(regAtual.getCodigo());
                    posMenor = j;
                }
            }
            
    	    comp++;
            if(posMenor != i) {
            	mov+=2;
                seekArq(i);
                regTemp.leDoArq(arquivo);
                seekArq(posMenor);
                regTemp.gravaNoArq(arquivo);
                seekArq(i);
                regMenor.gravaNoArq(arquivo); 		
            }
        }
    }

    public void bolha() {
        Registro reg1 = new Registro();
        Registro reg2 = new Registro();
        boolean troca = true;
        int tamanho = filesize();

        while(tamanho > 1 && troca) {
            troca = false;
            for (int i = 0; i < tamanho - 1; i++) {
                seekArq(i);
                reg1.leDoArq(arquivo);
                reg2.leDoArq(arquivo);
                comp++;
                if (reg1.getCodigo() > reg2.getCodigo()) {
                    seekArq(i);
                    mov+=2;
                    reg2.gravaNoArq(arquivo);
                    reg1.gravaNoArq(arquivo);
                    troca = true;
                }
            }
            tamanho--;
        }
    }
    
    public void shakeSort() {
        int inicio = 0, fim = filesize() - 1;
        boolean troca = true;
        Registro reg1 = new Registro(), reg2 = new Registro();
        while (inicio < fim && troca) {
            troca = false;
            for (int i = inicio; i < fim; i++) {
                seekArq(i);
                reg1.leDoArq(arquivo);
                reg2.leDoArq(arquivo);
                comp++;

                if (reg1.getCodigo() > reg2.getCodigo()) {
                    mov+=2;
                    seekArq(i);
                    reg2.gravaNoArq(arquivo);
                    reg1.gravaNoArq(arquivo);
                    troca = true;
                }
            }
            fim--;
            
            comp++;
            if (troca) {
                troca = false;
                for (int i = fim; i > inicio; i--) {
                    seekArq(i);
                    reg1.leDoArq(arquivo);
                    seekArq(i-1);
                    reg2.leDoArq(arquivo);
                    comp++;

                    if (reg1.getCodigo() < reg2.getCodigo()) {
                        mov+=2;
                        seekArq(i);
                        reg2.gravaNoArq(arquivo);
                        seekArq(i-1);
                        reg1.gravaNoArq(arquivo);
                        troca = true;
                    }
                }
            }
            inicio++;
        }
    }

    public void shellSort() {
        int i, j, dist = 1;
        Registro regI = new Registro();
        Registro regAux;
        Registro regJ = new Registro();

        while(dist < filesize()){
            dist = 3 * dist + 1;
        }
        dist = dist / 3;
        while(dist > 0) {
            for (i = dist; i < filesize(); i++){
                seekArq(i);
                regI.leDoArq(arquivo);
                regAux = new Registro(regI.getCodigo());
                j = i;

                seekArq(j - dist);
                regJ.leDoArq(arquivo);
                comp++;
                while(j - dist >= 0 && regAux.getCodigo() < regJ.getCodigo()) {
                    mov++;
                    seekArq(j);
                    regJ.gravaNoArq(arquivo);
                    j = j - dist;
                    seekArq(j - dist);
                    regJ.leDoArq(arquivo);
                }
                mov++;
                seekArq(j);
                regAux.gravaNoArq(arquivo);
            }
            dist = dist / 3;
        }
    }

    public void quickSemPivo() {
        quickSP(0, filesize() - 1);
    }
    public void quickSP(int ini, int fim) {
        int i = ini, j = fim;
        Registro reg1 = new Registro(), reg2 = new Registro();

        while (i < j) {
            seekArq(i);
            reg1.leDoArq(arquivo);
            seekArq(j);
            reg2.leDoArq(arquivo);

            comp++;
            while (i < j && reg1.getCodigo() <= reg2.getCodigo()) {
                i++;
                if (i < j) {
                    seekArq(i);
                    reg1.leDoArq(arquivo);
                }
                comp++;
            }

            if (i < j) {
                mov+=2;
                Registro temp = new Registro(reg1.getCodigo());
                seekArq(i);
                reg2.gravaNoArq(arquivo);
                seekArq(j);
                temp.gravaNoArq(arquivo);
            }

            comp++;
            while (i < j && reg1.getCodigo() <= reg2.getCodigo()) {
                j--;
                if (i < j) {
                    seekArq(j);
                    reg2.leDoArq(arquivo);
                }
                comp++;
            }

            if (i < j) {
                mov+=2;
                Registro temp = new Registro(reg1.getCodigo());
                seekArq(i);
                reg2.gravaNoArq(arquivo);
                seekArq(j);
                temp.gravaNoArq(arquivo);
            }
        }


        if (ini < i - 1) quickSP(ini, i - 1);
        if (j + 1 < fim) quickSP(j + 1, fim);
    }

    public void quickComPivo(){
        quickComPivo(0, filesize() - 1);
    }

    public void quickComPivo(int ini, int fim) {
        int i = ini, j = fim;
        Registro regI = new Registro();
        Registro regJ = new Registro();
        Registro regPivo = new Registro();
        seekArq((ini + fim) / 2);
        regPivo.leDoArq(arquivo);
        while (i < j) {
            comp++;
            seekArq(i);
            regI.leDoArq(arquivo);
            while (regI.getCodigo() < regPivo.getCodigo()) {
                i++;
                seekArq(i);
                regI.leDoArq(arquivo);
                comp++;
            }

            comp++;
            seekArq(j);
            regJ.leDoArq(arquivo);
            while (regJ.getCodigo() > regPivo.getCodigo()) {
                j--;
                seekArq(j);
                regJ.leDoArq(arquivo);
                comp++;
            }

            if (i <= j) {
                mov+=2;
                seekArq(i);
                regJ.gravaNoArq(arquivo);
                seekArq(j);
                regI.gravaNoArq(arquivo);
                i++;
                j--;
            }
        }

        if (ini < i - 1) {
            quickComPivo(ini, j);
        }
        if (j + 1 < fim) {
            quickComPivo(i, fim);
        }
    }

    public void mergePrimeiraImplementacao() {
        Arquivo arq1 = new Arquivo("particao1.dat");
        Arquivo arq2 = new Arquivo("particao2.dat");
        int seq = 1;
        while (seq < filesize()) {
            arq1.truncate(0);
            arq2.truncate(0);
            particao(arq1, arq2);
            fusao1Imp(arq1, arq2, seq);
            seq = seq * 2;
        }
    }

    public void particao(Arquivo arq1, Arquivo arq2) {
        int tam = filesize() / 2;
        Registro regI = new Registro();
        Registro regITam = new Registro();
        for (int i = 0; i < tam; i++) {
            mov+=2;
            seekArq(i);
            regI.leDoArq(arquivo);
            seekArq(i + tam);
            regITam.leDoArq(arquivo);
            arq1.seekArq(i);
            regI.gravaNoArq(arq1.arquivo);
            arq2.seekArq(i);
            regITam.gravaNoArq(arq2.arquivo);
        }
    }

    public void fusao1Imp(Arquivo arq1, Arquivo arq2, int seq) {
        int i = 0, j = 0, k = 0, t_seq = seq;
        Registro regI = new Registro();
        Registro regJ = new Registro();
        while (k < filesize()) {
            while (i < seq && j < seq) {
                arq1.seekArq(i);
                regI.leDoArq(arq1.arquivo);
                arq2.seekArq(j);
                regJ.leDoArq(arq2.arquivo);
                comp++;
                if (regI.getCodigo() < regJ.getCodigo()) {
                    seekArq(k);
                    mov++;
                    regI.gravaNoArq(arquivo);
                    k++;
                    i++;
                } else {
                    seekArq(k);
                    mov++;
                    regJ.gravaNoArq(arquivo);
                    k++;
                    j++;
                }
            }
            while (i < seq) {
                mov++;
                arq1.seekArq(i);
                regI.leDoArq(arq1.arquivo);
                seekArq(k);
                regI.gravaNoArq(arquivo);
                k++;
                i++;
            }
            while (j < seq) {
                mov++;
                arq2.seekArq(j);
                regJ.leDoArq(arq2.arquivo);
                seekArq(k);
                regJ.gravaNoArq(arquivo);
                k++;
                j++;
            }
            seq += t_seq;
        }
    }

    public void mergeSegundaImplementacao() {
        Arquivo arqAux = new Arquivo("auxiliar.dat");
        particaoImp2(0, filesize() - 1, arqAux);
    }

    public void particaoImp2(int esq, int dir, Arquivo arqAux) {
        int meio;
        if (esq < dir) {
            meio = (esq + dir) / 2;
            particaoImp2(esq, meio, arqAux);
            particaoImp2(meio + 1, dir, arqAux);
            fusao2Imp(esq, meio, meio + 1, dir, arqAux);
        }
    }

    public void fusao2Imp(int ini1, int fim1, int ini2, int fim2, Arquivo arqAux) {
        int i = ini1, j = ini2, k = 0;
        Registro regI = new Registro();
        Registro regJ = new Registro();
        while (i <= fim1 && j <= fim2) {
            seekArq(i);
            regI.leDoArq(arquivo);
            seekArq(j);
            regJ.leDoArq(arquivo);
            comp++;

            if (regI.getCodigo() < regJ.getCodigo()) {
                arqAux.seekArq(k);
                regI.gravaNoArq(arqAux.arquivo);
                i++;
                k++;
            } else {
                arqAux.seekArq(k);
                regJ.gravaNoArq(arqAux.arquivo);
                j++;
                k++;
            }
            mov++;
        }
        while (i <= fim1) {
            mov++;
            seekArq(i);
            regI.leDoArq(arquivo);
            arqAux.seekArq(k);
            regI.gravaNoArq(arqAux.arquivo);
            k++;
            i++;
        }
        while (j <= fim2) {
            mov++;
            seekArq(j);
            regJ.leDoArq(arquivo);
            arqAux.seekArq(k);
            regJ.gravaNoArq(arqAux.arquivo);
            k++;
            j++;
        }
        for (i = 0; i < k; i++) {
            mov++;
            arqAux.seekArq(i);
            regI.leDoArq(arqAux.arquivo);
            seekArq(i + ini1);
            regI.gravaNoArq(arquivo);
        }
    }

    public void heapSort() {
        int TL2 = filesize(), pai, FE, FD, maiorF;
        Registro regP = new Registro();
        Registro regE = new Registro();
        Registro regD = new Registro();
        Registro regMaiorF;
        Registro regIni = new Registro();
        Registro regFim = new Registro();

        while (TL2 > 1) {
            for (pai = TL2 / 2 - 1; pai >= 0; pai--) {
                seekArq(pai);
                regP.leDoArq(arquivo);
                FE = 2 * pai + 1;
                FD = FE + 1;
                seekArq(FE);
                regE.leDoArq(arquivo);
                regD.leDoArq(arquivo);
                maiorF = FE;
                regMaiorF = new Registro(regE.getCodigo());
                if (FD < TL2 && regD.getCodigo() > regE.getCodigo()) {
                    regMaiorF = regD;
                    maiorF = FD;
                }

                comp+=2;

                if (regMaiorF.getCodigo() > regP.getCodigo()) {
                    mov+=2;
                    seekArq(pai);
                    regMaiorF.gravaNoArq(arquivo);
                    seekArq(maiorF);
                    regP.gravaNoArq(arquivo);
                }
            }
            seekArq(0);
            regIni.leDoArq(arquivo);
            seekArq(TL2 - 1);
            regFim.leDoArq(arquivo);
            seekArq(0);
            regFim.gravaNoArq(arquivo);
            seekArq(TL2 - 1);
            regIni.gravaNoArq(arquivo);
            TL2--;
            mov+=2;
        }
    }
    
    public void countingSort() {
        int max = 0;
        int tamanho = filesize();
        // Encontrar o valor máximo
        for(int i = 0; i < tamanho; i++) {
            seekArq(i);
            Registro reg = new Registro();
            reg.leDoArq(arquivo);
            comp++;
            if(reg.getCodigo() > max) max = reg.getCodigo();
        }

        // Inicializar o array de contagem
        int[] count = new int[max + 1];

        // Contar as ocorrências
        for(int i = 0; i < tamanho; i++) {
            seekArq(i);
            Registro reg = new Registro();
            reg.leDoArq(arquivo);
            count[reg.getCodigo()]++;
        }

        // Posições cumulativas
        for(int i = 1; i <= max; i++) {
            count[i] += count[i - 1];
        }

        // Criar arquivo auxiliar e ordenar
        Arquivo arquivoAux = new Arquivo("arqAux");
        Registro auxReg = new Registro();
        for(int i = tamanho - 1; i >= 0; i--) {
            mov++;
            seekArq(i);
            auxReg.leDoArq(arquivo);
            arquivoAux.seekArq((count[auxReg.getCodigo()] - 1));
            auxReg.gravaNoArq(arquivoAux.arquivo);
            count[auxReg.getCodigo()]--;
        }

        // Copiar arquivo auxiliar de volta para o original
        for(int i = 0; i < tamanho; i++) {
            mov++;
            arquivoAux.seekArq(i);
            auxReg.leDoArq(arquivoAux.arquivo);
            seekArq(i);
            auxReg.gravaNoArq(arquivo);
        }

        arquivoAux.fechar();
    }

    public void bucketSort() {
        int quantBuckets = 10; // Nesse caso defini 10 fixo, mas poderia passar por params

        int tamanho = filesize();
        int maior = 0, menor = Integer.MAX_VALUE;

        // Encontrar maior e menor valor para determinar os intervalos
        for (int i = 0; i < tamanho; i++) {
            seekArq(i);
            Registro reg = new Registro();
            reg.leDoArq(arquivo);
            comp+=2;
            if (reg.getCodigo() > maior) maior = reg.getCodigo();
            if (reg.getCodigo() < menor) menor = reg.getCodigo();
        }

        int intervalo = (maior - menor) + 1; //intervaloPorBucket = ((M - m) + 1) / qtdeBuckets
        double intervaloPorBucket = (double) intervalo / quantBuckets;

        // Criar os buckets como arquivos temporários
        Arquivo[] buckets = new Arquivo[quantBuckets];
        for (int i = 0; i < quantBuckets; i++) {
            buckets[i] = new Arquivo("bucket" + i + ".dat");
            buckets[i].truncate(0); // Limpar o bucket se já existir
        }

        // Distribuir os elementos pelos buckets
        for (int i = 0; i < tamanho; i++) {
            seekArq(i);
            Registro reg = new Registro();
            reg.leDoArq(arquivo);
            int index = (int) ((reg.getCodigo() - menor) / intervaloPorBucket);
            if (index >= quantBuckets) {
                index = quantBuckets - 1;
            }
            mov++;
            buckets[index].inserirRegNoFinal(reg.getCodigo());
        }

        // Ordenar cada bucket e concatenar de volta ao arquivo original
        truncate(0); // Limpar o arquivo original
        for (int i = 0; i < quantBuckets; i++) {
            buckets[i].insercaoDireta();
            mov += buckets[i].getMov();
            comp += buckets[i].getComp();
            for (int j = 0; j < buckets[i].filesize(); j++) {
                buckets[i].seekArq(j);
                Registro reg = new Registro();
                reg.leDoArq(buckets[i].arquivo);
                mov++;
                inserirRegNoFinal(reg.getCodigo());
            }
            buckets[i].deletarArquivo();
        }
    }

    public void radixSort() {
        int maior = getMaiorValor();
        for(int i = 1; maior/i > 0; i*=10){
            countingWithRadix(i);
        }
    }

    public void countingWithRadix(int exp) {
        int n = filesize();
        int[] count = new int[10]; // 0...9

        // Contar as ocorrências com cada casa decimal
        for(int i = 0; i < n; i++) {
            seekArq(i);
            Registro reg = new Registro();
            reg.leDoArq(arquivo);
            count[(reg.getCodigo() / exp) % 10]++;
        }

        // Posições cumulativas
        for(int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }

        // Criar arquivo auxiliar e ordenar
        Arquivo arquivoAux = new Arquivo("arqAux");
        Registro auxReg = new Registro();
        for(int i = n - 1; i >= 0; i--) {
            mov++;
            seekArq(i);
            auxReg.leDoArq(arquivo);
            arquivoAux.seekArq((count[(auxReg.getCodigo() / exp) % 10] - 1));
            auxReg.gravaNoArq(arquivoAux.arquivo);
            count[(auxReg.getCodigo() / exp) % 10]--;
        }

        // Copiar arquivo auxiliar de volta para o original
        for(int i = 0; i < n; i++) {
            mov++;
            arquivoAux.seekArq(i);
            auxReg.leDoArq(arquivoAux.arquivo);
            seekArq(i);
            auxReg.gravaNoArq(arquivo);
        }
    }

    public void combSort() {
        int gap = (int) (filesize() / 1.3);
        int posAtual;

        while(gap > 0) {
           posAtual = 0;
           while((posAtual + gap) < filesize()) {
              seekArq(posAtual);
              Registro regAtual = new Registro();
              regAtual.leDoArq(arquivo);

              seekArq(posAtual + gap);
              Registro regGap = new Registro();
              regGap.leDoArq(arquivo);

              comp++;
              if(regAtual.getCodigo() > regGap.getCodigo()) {
                  mov+=2;
                  seekArq(posAtual);
                  regGap.gravaNoArq(arquivo);
                  seekArq(posAtual + gap);
                  regAtual.gravaNoArq(arquivo);
              }
              posAtual++;
           }
           gap = (int) (gap / 1.3);
        }
    }

    public void gnomeSort() {
        int currentPos = 0;
        int n = filesize();
        Registro regAtual = new Registro();
        Registro regAnterior = new Registro();

        while(currentPos < n) {
            if (currentPos == 0) {
                currentPos++;
            }
            seekArq(currentPos);
            regAtual.leDoArq(arquivo);

            seekArq(currentPos-1);
            regAnterior.leDoArq(arquivo);

            comp++;
            if(regAtual.getCodigo() >= regAnterior.getCodigo()) {
                currentPos++;
            } else {
                mov+=2;
                seekArq(currentPos);
                regAnterior.gravaNoArq(arquivo);
                seekArq(currentPos-1);
                regAtual.gravaNoArq(arquivo);
                currentPos--;
            }
        }
    }

    private void insercaoDiretaForTim(int ini, int fim) {
        // eu poderia ter usado o insertionSort que já existe, mas preferi fazer um novo
        for (int i = ini + 1; i <= fim; i++) {
            seekArq(i);
            Registro regI = new Registro();
            regI.leDoArq(arquivo);
            Registro regAuxiliar = new Registro(regI.getCodigo());
            int pos = i;

            seekArq(pos - 1);
            Registro regAnterior = new Registro();
            mov++;
            regAnterior.leDoArq(arquivo);

            comp++;
            while (pos > ini && regAuxiliar.getCodigo() < regAnterior.getCodigo()) {
                mov++;
                seekArq(pos);
                regAnterior.gravaNoArq(arquivo);
                pos--;

                if (pos > ini) {
                    seekArq(pos - 1);
                    mov++;
                    regAnterior.leDoArq(arquivo);
                }
            }

            mov++;
            seekArq(pos);
            regAuxiliar.gravaNoArq(arquivo);
        }
    }

    public void timSort() {
        int n = filesize();
        int RUN = 32;
        for (int i = 0; i < n; i += RUN) {
            insercaoDiretaForTim(i, Math.min((i + 31), (n - 1)));
        }

        for (int size = RUN; size < n; size = 2 * size) {
            for (int left = 0; left < n; left += 2 * size) {
                int mid = left + size - 1;
                int right = Math.min((left + 2 * size - 1), (n - 1));
                fusao2Imp(left, mid, mid + 1, right, new Arquivo("auxiliar.dat"));
            }
        }
    }

    private int getMaiorValor() {
        int maior = 0;
        for(int i = 0; i < filesize(); i++) {
            seekArq(i);
            Registro reg = new Registro();
            reg.leDoArq(arquivo);
            comp++;
            if(reg.getCodigo() > maior) maior = reg.getCodigo();
        }
        return maior;
    }

}
