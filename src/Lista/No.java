package Lista;

public class No {
    private No ant;
    private int info;
    private No prox;

    public No(No ant, No prox, int info) {
        this.ant = ant;
        this.info = info;
        this.prox = prox;
    }

    public No getAnt() {
        return ant;
    }

    public void setAnt(No ant) {
        this.ant = ant;
    }

    public int getInfo() {
        return info;
    }

    public void setInfo(int info) {
        this.info = info;
    }

    public No getProx() {
        return prox;
    }

    public void setProx(No prox) {
        this.prox = prox;
    }
}
