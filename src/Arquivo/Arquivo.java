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

    public void arquivoOrdenado() {
        truncate(0);

        for (int i = 0; i < 512; i++) {
            new Registro(i).gravaNoArq(arquivo);
        }
    }

    public void arquivoReverso() {
        truncate(0);

        for (int i = 512; i > 0; i--)
            new Registro(i).gravaNoArq(arquivo);
    }

    public void arquivoRandomico() {
        truncate(0);
        for (int i = 0; i < 512; i++)
            new Registro(new Random().nextInt(10100)).gravaNoArq(arquivo);

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
        Registro regI = new Registro();
        Registro regAnterior = new Registro();
        Registro regAuxiliar;
        int i = 1, pos;
        while(i < filesize()) {
            seekArq(i);
            regI.leDoArq(arquivo);
            regAuxiliar = regI;
            pos = i;
            seekArq(pos-1);
            regAnterior.leDoArq(arquivo);
            comp++;

            while (pos > 0 && regAuxiliar.getCodigo() < regAnterior.getCodigo()) {
                mov++;
                seekArq(pos - 1);
                regAnterior.leDoArq(arquivo);
                seekArq(pos);
                regAnterior.gravaNoArq(arquivo);
                pos--;
            }

            seekArq(pos);
            regAuxiliar.gravaNoArq(arquivo);
            comp++;
            i++;
        }
    }

    public void insercaoBinaria() {
        int pos;
        Registro regI = new Registro();
        Registro regAux = new Registro();
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
                if (reg1.getCodigo() > reg2.getCodigo()) {
                    seekArq(i);
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
    
    // Shell Sort
    
    public void heapSort() {
        int TL2 = filesize(), pai, FE, FD, maiorF;
        Registro regP = new Registro();
        Registro regE = new Registro();
        Registro regD = new Registro();
        Registro regMaiorF = new Registro();
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
                regMaiorF = regE;
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

}
