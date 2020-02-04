import java.util.Scanner;

public class Provador {
    
    // mostra a nomenclatura dos conectivos
    public static void mostrarNomenclatura() {
        System.out.print(
            "Provador - Tableaux\n" +
            "Nomenclatura: \n" +
            "^ - conjunção \n" +
            "v - disjunção \n" +
            "> - implicação\n" +
            "~ - negacao   \n" +
            "Ex.: a>b, b>c | a>c\n\n"
        );
    }
    
    public static void main(String[] args) {
        
        Scanner entrada = new Scanner(System.in);
        
        String expressao = new String("");
        
        mostrarNomenclatura();
        
        while (true) {
            System.out.print("Digite a expressao desejada: ");
            expressao = entrada.nextLine();
            
            // verifica a entrada do usuario
            if ( expressao.equals("") == true )
                break;
            
            expressao = expressao.replace(" ", "");
            
            long inicio, fim; // trataram de calcular o tempo inicial e final da execucao do Tableaux
            
            inicio = System.currentTimeMillis();
            Arvore tableaux = new Arvore( expressao );
            tableaux.expandir(); // expande a arvore
            tableaux.provar();   // calcula a prova da arvore
            fim = System.currentTimeMillis();
            
            System.out.println("\nO tempo que levou para a fórmula ser demonstrada: " + (fim - inicio) + " ms");
            System.out.println("Números de nós gerados no Tableaux: " + tableaux.contarFolhas());
            System.out.println("Números de ramos no Tableaux: " + tableaux.contarRamos() + "\n");
            
            if ( tableaux.resultado() )
                System.out.println( "Algum ramo ficou aberto. [Não foi Provado]\n" );
            else
                System.out.println( "Todos os ramos foram fechados. [Provado]\n" );
        }
    }
}
