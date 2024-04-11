package Arquivo;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Registro {
    public final int tf = 1;
    private int codigo; // 4 bytes
    private char lixo[] = new char[tf]; //2044 bytes

    public Registro() {
        this.codigo = codigo;

        for(int i = 0; i<tf; i++)
            lixo[i] = 'X';
    }

    public Registro(int codigo){

        this.codigo = codigo;

        for(int i = 0; i<tf; i++)
            lixo[i] = 'X';
    }

    public int getCodigo() {
        return codigo;
    }

    public void gravaNoArq(RandomAccessFile arquivo)
    {
        try
        {
            arquivo.writeInt(codigo);
            for(int i=0 ; i<tf ; i++)
                arquivo.writeChar(lixo[i]);
        }catch(IOException e){}
    }

    public void leDoArq(RandomAccessFile arquivo)
    {
        try
        {
            codigo = arquivo.readInt();
            for(int i=0 ; i<tf ; i++)
                lixo[i]=arquivo.readChar();
        }catch(IOException e){}
    }
    static int length()
    {
        return(6);
    }

    public void exibirRegistro(){
        System.out.println(codigo);
    }
}
