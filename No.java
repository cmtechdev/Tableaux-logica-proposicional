public class No {
    public char valoracao;
    public String expressao;
    public No esquerda;
    public No direita;
    public boolean estendido;
    
    public No(char valoracao, String expressao, No esquerda, No direita) {
        this.valoracao  = valoracao;
        this.expressao  = expressao;
        this.esquerda   = esquerda;
        this.direita    = direita;
        estendido       = false;
    }
}
