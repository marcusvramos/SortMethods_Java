package Arquivo;
import java.io.*;
import java.util.function.Consumer;

@FunctionalInterface
interface Calculo {
    double calcular(int tamanho);
}

public class Tabela {
    private static class ResultadoOrdenacao {
        int comp, mov, tempo;

        public ResultadoOrdenacao(int comp, int mov, int tempo) {
            this.comp = comp;
            this.mov = mov;
            this.tempo = tempo;
        }
    }

    private static ResultadoOrdenacao executarOrdenacao(Arquivo arquivo, Consumer<Arquivo> metodoOrdenacao) {
        arquivo.initComp();
        arquivo.initMov();
        long tini = System.currentTimeMillis();

        metodoOrdenacao.accept(arquivo);

        long tfim = System.currentTimeMillis();
        int tempo = (int)(tfim - tini) / 1000;

        return new ResultadoOrdenacao(arquivo.getComp(), arquivo.getMov(), tempo);
    }

    public void criarTabela() throws IOException {
        FileWriter arq = new FileWriter("tabela.txt");
        PrintWriter gravarArq = new PrintWriter(arq);
        cabecalhoTabela(gravarArq);

        // Inicializa os arquivos
        Arquivo arqOrd = new Arquivo("arquivoOrdenado.dat");
        Arquivo arqRand = new Arquivo("arquivoRandomico.dat");
        Arquivo arqRev = new Arquivo("arquivoReverso.dat");

        arqOrd.arquivoOrdenado();
        arqRev.arquivoReverso();
        arqRand.arquivoRandomico();

        // Inserção Direta
        realizarOrdenacaoEAdicionarNaTabela(
            gravarArq,
            "Inserção Direta",
            arqOrd,
            arqRev,
            arqRand,
            Arquivo::insercaoDireta,
            this::calculaCompInsercaoDiretaOrd,
            this::calculaCompInsercaoDiretaRev,
            this::calculaCompInsercaoDiretaRand,
            this::calculaMovInsercaoDiretaOrd,
            this::calculaMovInsercaoDiretaRev,
            this::calculaMovInsercaoDiretaRand
        );

        // Inserção Binária
        realizarOrdenacaoEAdicionarNaTabela(
            gravarArq,
            "Inserção Binária",
            arqOrd,
            arqRev,
            arqRand,
            Arquivo::insercaoBinaria,
            this::calculaCompInsercaoDiretaOrd,
            this::calculaCompInsercaoDiretaRev,
            this::calculaCompInsercaoDiretaRand,
            this::calculaMovInsercaoDiretaOrd,
            this::calculaMovInsercaoDiretaRev,
            this::calculaMovInsercaoDiretaRand
        );

        // Seleção Direta
        realizarOrdenacaoEAdicionarNaTabela(
            gravarArq,
            "Seleção Direta",
            arqOrd,
            arqRev,
            arqRand,
            Arquivo::selecaoDireta,
            this::calculaCompSelecaoDiretaDir,
            this::calculaCompSelecaoDiretaDir,
            this::calculaCompSelecaoDiretaDir,
            this::calculaMovSelecaoDiretaOrd,
            this::calculaMovSelecaoDiretaRev,
            this::calculaMovSelecaoDiretaRand
        );

        // Bolha
        realizarOrdenacaoEAdicionarNaTabela(
            gravarArq,
            "Bolha",
            arqOrd,
            arqRev,
            arqRand,
            Arquivo::bolha,
            this::calculaCompBolha,
            this::calculaCompBolha,
            this::calculaCompBolha,
            this::noMov,
            this::calculaMovBolhaRev,
            this::calculaMovBolhaRand
        );

        // Shake Sort
        realizarOrdenacaoEAdicionarNaTabela(
            gravarArq,
            "Shake Sort",
            arqOrd,
            arqRev,
            arqRand,
            Arquivo::shakeSort,
            this::calculaCompBolha,
            this::calculaCompBolha,
            this::calculaCompBolha,
            this::noMov,
            this::calculaMovBolhaRev,
            this::calculaMovBolhaRand
        );

        // Shell Sort

        // Heap Sort
        realizarOrdenacaoEAdicionarNaTabela(
            gravarArq,
            "Heap Sort",
            arqOrd,
            arqRev,
            arqRand,
            Arquivo::heapSort,
            this::noComp,
            this::noComp,
            this::noComp,
            this::noMov,
            this::noMov,
            this::noMov
        );

        // Quick com pivô

        // Quick sem pivô

        // Merge implementação 01

        // Merge implementação 02

        // Couting Sort
        realizarOrdenacaoEAdicionarNaTabela(
            gravarArq,
            "Counting Sort",
            arqOrd,
            arqRev,
            arqRand,
            Arquivo::countingSort,
            this::noComp,
            this::noComp,
            this::noComp,
            this::noMov,
            this::noMov,
            this::noMov
        );

        // Bucket

        // Radix
        realizarOrdenacaoEAdicionarNaTabela(
            gravarArq,
            "Radix Sort",
            arqOrd,
            arqRev,
            arqRand,
            Arquivo::radixSort,
            this::noComp,
            this::noComp,
            this::noComp,
            this::noMov,
            this::noMov,
            this::noMov
        );

        // Comb
        realizarOrdenacaoEAdicionarNaTabela(
            gravarArq,
            "Comb Sort",
            arqOrd,
            arqRev,
            arqRand,
            Arquivo::combSort,
            this::noComp,
            this::noComp,
            this::noComp,
            this::noMov,
            this::noMov,
            this::noMov
        );

        // Gnome
        realizarOrdenacaoEAdicionarNaTabela(
            gravarArq,
            "Gnome Sort",
            arqOrd,
            arqRev,
            arqRand,
            Arquivo::gnomeSort,
            this::noComp,
            this::noComp,
            this::noComp,
            this::noMov,
            this::noMov,
            this::noMov
        );

        // Tim

        arq.close();
        exibirTabelaFinal();
    }

    private void realizarOrdenacaoEAdicionarNaTabela(
        PrintWriter gravarArq,
        String nomeMetodo,
        Arquivo arqOrd,
        Arquivo arqRev,
        Arquivo arqRand,
        Consumer<Arquivo> metodoOrdenacao,
        Calculo compProgOrd,
        Calculo compProgRev,
        Calculo compProgRand,
        Calculo movProgOrd,
        Calculo movProgRev,
        Calculo movProgRand
    ) {
        Arquivo auxRev = new Arquivo("CopiaReverso.dat");
        Arquivo auxRand = new Arquivo("CopiaRandomico.dat");

        arqRev.copiaArquivo(auxRev);
        arqRand.copiaArquivo(auxRand);

        ResultadoOrdenacao resOrd = executarOrdenacao(arqOrd, metodoOrdenacao);
        ResultadoOrdenacao resRev = executarOrdenacao(auxRev, metodoOrdenacao);
        ResultadoOrdenacao resRand = executarOrdenacao(auxRand, metodoOrdenacao);

        addLinhaTabela(
            gravarArq,
            nomeMetodo,
            resOrd.comp, resRev.comp, resRand.comp,
            resOrd.mov, resRev.mov, resRand.mov,
            compProgOrd.calcular(arqOrd.filesize()),
            compProgRev.calcular(auxRev.filesize()),
            compProgRand.calcular(auxRand.filesize()),
            movProgOrd.calcular(arqOrd.filesize()),
            movProgRev.calcular(auxRev.filesize()),
            movProgRand.calcular(auxRand.filesize()),
            resOrd.tempo,
            resRev.tempo,
            resRand.tempo
        );
    }

    private void cabecalhoTabela(PrintWriter gravarArq) {
        gravarArq.println("+-------------------+----------------+----------------+----------------+----------------+--------+----------------+----------------+----------------+----------------+--------+----------------+----------------+----------------+----------------+--------+");

        gravarArq.printf("|%19s|%-67s|%-76s|%-85s|%n", "", "Arquivo Ordenado", "Arquivo em Ordem Reverso", "Arquivo Randômico");

        gravarArq.printf("| %-17s | %-14s | %-14s | %-14s | %-14s | %-6s | %-14s | %-14s | %-14s | %-14s | %-6s | %-14s | %-14s | %-14s | %-14s | %-6s |%n",
                "Métodos Ordenação", "Comp. Prog. *", "Comp. Equa. #", "Mov. Prog. +", "Mov. Equa. -", "Tempo",
                "Comp. Prog. *", "Comp. Equa. #", "Mov. Prog. +", "Mov. Equa. -", "Tempo",
                "Comp. Prog. *", "Comp. Equa. #", "Mov. Prog. +", "Mov. Equa. -", "Tempo"
        );

        gravarArq.println("+-------------------+----------------+----------------+----------------+----------------+--------+----------------+----------------+----------------+----------------+--------+----------------+----------------+----------------+----------------+--------+");
    }


    private void addLinhaTabela(PrintWriter gravarArq, String metodo, int compOrd, int compRev, int compRand, int movOrd, int movRev, int movRand, double compEquaOrd, double compEquaRev,
                                double compEquaRand, double movEquaOrd, double movEquaRev, double movEquaRand, int ttotalOrd, int ttotalRev, int ttotalRand) {
        gravarArq.printf("| %-17s | %-14d | %-14.1f | %-14d | %-14.1f | %-6d | %-14d | %-14.1f | %-14d | %-14.1f | %-6d | %-14d | %-14.1f | %-14d | %-14.1f | %-6d |%n",
                metodo,
                compOrd, compEquaOrd, movOrd, movEquaOrd, ttotalOrd,
                compRev, compEquaRev, movRev, movEquaRev, ttotalRev,
                compRand, compEquaRand, movRand, movEquaRand, ttotalRand
        );
        gravarArq.println("+-------------------+----------------+----------------+----------------+----------------+--------+----------------+----------------+----------------+----------------+--------+----------------+----------------+----------------+----------------+--------+");
    }


    private void exibirTabelaFinal() throws IOException{
        FileInputStream stream = new FileInputStream("tabela.txt");

        InputStreamReader reader = new InputStreamReader(stream);
        BufferedReader br = new BufferedReader(reader);
        String linha = br.readLine();

        while(linha != null) {
            System.out.println(linha);
            linha = br.readLine();
        }
    }

    private double calculaCompInsercaoDiretaOrd(int TL){
        return TL-1;
    }

    private double calculaCompInsercaoDiretaRev(int TL){
        return ((Math.pow(TL, 2)) + (TL-2)) / 4;
    }

    private double calculaCompInsercaoDiretaRand(int TL){
        return ((Math.pow(TL, 2)) + (TL-4)) / 4;
    }

    private int calculaMovInsercaoDiretaOrd(int TL){
        return 3 * (TL-1);
    }

    private double calculaMovInsercaoDiretaRev(int TL){
        return ((Math.pow(TL, 2)) + (9 * TL) - 10) / 4;
    }

    private double calculaMovInsercaoDiretaRand(int TL){
        return ((Math.pow(TL, 2)) + (3 * TL) - 4) / 2;
    }

    private double calculaCompSelecaoDiretaDir(int TL){
        return ((Math.pow(TL, 2)) - TL) / 2;
    }

    private double calculaMovSelecaoDiretaOrd(int TL){
        return 3 * (TL - 1);
    }

    private double calculaMovSelecaoDiretaRev(int TL){
        return TL * ((TL) + 0.577216);
    }

    private double calculaMovSelecaoDiretaRand(int TL){
        return ((Math.pow(TL, 2)) /4 ) + (3 * (TL-1));
    }

    private double calculaCompBolha(int TL){
        return ((Math.pow(TL, 2)) - TL) / 2;
    }

    private double calculaMovBolhaRev(int TL){
        return (3 * ((Math.pow(TL, 2)) - TL)) / 2;
    }

    private double calculaMovBolhaRand(int TL){
        return (3* (Math.pow(TL, 2)) - TL ) / 4;
    }

    private double noMov(int TL){
        return 0;
    }

    private double noComp(int TL){
        return 0;
    }
}
