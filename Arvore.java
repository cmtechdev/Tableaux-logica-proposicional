public class Arvore {
    // armazenara o topo da arvore
    public No topo;
    public String expA, expB;
    public int totalFolhas;
    public int totalRamos;
    
    // metodo construtor
    public Arvore( String expressao ) {
        
        this.topo = new No(' ', expressao, null, null);
        this.expA = "";
        this.expB = "";
        this.totalFolhas = 0;
        this.totalRamos  = 0;
        
    }
    
    // metodo que sera chamado pelo desenvolvedor no momento em que o mesmo desejar desenvolver a arvore
    public void expandir() {
        expandir( topo );
    }
    
    // metodo que cuidara de expandir a arvore
    public void expandir( No no ) {
        
        // guardara a expressao desejada
        String novaExpressao = "";
        char valoracao = 'T';
        
        // percorre todas a expressao
        for ( int i = 0; i < no.expressao.length(); i++ ) {
            if ( no.expressao.charAt(i) == ',' ) {
                adicionar( topo, valoracao, tratarParenteses(novaExpressao) );
                novaExpressao = ""; // limpa o conteudo
            }
            else if ( no.expressao.charAt(i) == '|' ) {
                // verifica se a nova expressao existe
                if ( novaExpressao.length() > 0 ) {
                    adicionar( topo, valoracao, tratarParenteses(novaExpressao) );
                }
                
                valoracao = 'F';
                novaExpressao = ""; // limpa o conteudo
            }
            else 
                novaExpressao += no.expressao.charAt(i);
        }
        adicionar( topo, valoracao, tratarParenteses(novaExpressao) );
        
        // realiza as expansoes necessárias
        r_exp( topo.esquerda );
        
    }
    
    // realiza as expansões de acordo com a eurística - Alfas antes das Betas
    public void r_exp( No no ) {
        if ( no != null ) {
            total_alfa( no );
            total_beta( no );
            
            r_exp( no.esquerda );
            r_exp( no.direita );
        }
    }
    
    // realiza expansão alfa
    public void total_alfa( No no ) {
        if ( no != null ) {
            expansao_alfa( no );
            
            total_alfa( no.esquerda );
            total_alfa( no.direita );
        }
    }
    
    // realiza expansão beta
    public void total_beta( No no ) {
        if ( no != null ) {
            if ( !expansao_beta( no ) ) {
                total_beta( no.esquerda );
                total_beta( no.direita );
            }
        }
    }
    
    // expande as formulas alfas
    public boolean expansao_alfa( No no ) {
        // se a 'expressao' passada nao for um atomo, então...
        if ( no != null && no.estendido == false && verificar_negacao( no, no.expressao, no.valoracao) == false && no.expressao.length() > 1 ) {
            
            char conectivo = conectivo( no.expressao );
            
            expA = tratarParenteses( expA );
            expB = tratarParenteses( expB );
            
            // verifica se a expansao eh alfa
            if ( conectivo == '^' && no.valoracao == 'T' ) {
                no.estendido = true;
                adicionar( no, 'T', expA );
                adicionar( no, 'T', expB );
                return true;
            }
            else if ( conectivo == 'v' && no.valoracao == 'F' ) {
                no.estendido = true;
                adicionar( no, 'F', expA );
                adicionar( no, 'F', expB );
                return true;
            }
            else if ( conectivo == '>' && no.valoracao == 'F' ) {
                no.estendido = true;
                adicionar( no, 'T', expA );
                adicionar( no, 'F', expB );
                return true;
            }
            
            return false;
            
        }
        
        return true;
    }
    
    // expande as formulas betas
    public boolean expansao_beta( No no ) {
        // se a 'expressao' passada nao for um atomo, então...
        if ( no != null && no.estendido == false && verificar_negacao( no, no.expressao, no.valoracao) == false && no.expressao.length() > 1 ) {
            
            char conectivo = conectivo( no.expressao ); // obtem o conectivo para a expressao atual
            
            expA = tratarParenteses( expA ); // trata a expA 
            expB = tratarParenteses( expB ); // trata a expB
            
            // verifica se a expansao eh alfa
            if ( conectivo == '^' && no.valoracao == 'F' ) {
                no.estendido = true;
                adicionar( no, 'F', expA, 'F', expB );
                return true;
            }
            else if ( conectivo == 'v' && no.valoracao == 'T' ) {
                no.estendido = true;
                adicionar( no, 'T', expA, 'T', expB );
                return true;
            }
            else if ( conectivo == '>' && no.valoracao == 'T' ) {
                no.estendido = true;
                adicionar( no, 'F', expA, 'T', expB );
                return true;
            }
            
            return false;
        }
        
        return true;
        
    }
    
    // verifica se ha negacao em uma dada expressao
    public boolean verificar_negacao( No no, String expressao, char v) {
        
        if ( expressao.length() > 1 && expressao.charAt(0) == '~' && removerNegacao( expressao ) ) {
            
            String novaExp = new String("");
            
            // remove a negacao da expressao, formulando assim, uma nova
            for ( int i = 1; i < expressao.length(); i++ )
                novaExp += expressao.charAt(i);
            
            // verifica a valoracao
            if ( v == 'T' )
                adicionar( no, 'F', tratarParenteses(novaExp) );
            else if ( v == 'F' )
                adicionar( no, 'T', tratarParenteses(novaExp) );
            
            no.estendido = true;
            
            return true;
        }
        
        return false;
    }
    
    // verifica se o valor passado eh uma expressao
    public boolean removerNegacao( String exp ) {
        
        int conectivos, totalParenteses;
        conectivos = totalParenteses = 0;
        boolean parenteses = false;
        
        boolean c, p;
        c = p = false;
        
        for (int i = 0; i < exp.length(); i++) {
            if ( (exp.charAt(i) == '>' || exp.charAt(i) == 'v' || exp.charAt(i) == '^') && p == false )
                c = true;
            else if ( exp.charAt(i) == '(' && c == false )
                p = true;
            
            if ( exp.charAt(i) == '(' )
                totalParenteses++;
            else if ( exp.charAt(i) == ')' )
                totalParenteses--;
                
            if ( totalParenteses == 0 && (exp.charAt(i) == '>' || exp.charAt(i) == 'v' || exp.charAt(i) == '^') )
                return false;
        }
        
        if ( c )
            return false;
        else if ( p )
            return true;
        else 
            return true;
        
    }
    
    // metodo responsavel por adicionar as novas folhas a arvore - Expansao Alfa
    public void adicionar(No folha, char v, String exp) {
        
        // verifica se eh uma folha da extremidade da arvore
        if ( folha.esquerda == null && folha.direita == null ) {
            // adiciona a nova folha
            folha.esquerda = new No(v, exp, null, null);
            
            if ( exp.length() == 1 )
                folha.esquerda.estendido = true;
            
            totalFolhas += 1; // incrementa no total de folhas
        }
        // caso não seja, continua sua busca
        else {
            if ( folha.esquerda != null )
                adicionar( folha.esquerda, v, exp );
            if ( folha.direita != null )
                adicionar( folha.direita, v, exp );
        }
        
    }
    
    // metodo responsavel por adicionar as novas folhas a arvore - Expansao Beta
    public void adicionar(No no, char v1, String exp1, char v2, String exp2) {
        
        if ( no != null ) {
            
            // se a folha atual for de alguma extremidade, entao...
            if ( no.esquerda == null && no.direita == null ) {
                // adicina-se as novas folhas
                no.esquerda = new No( v1, exp1, null, null );
                no.direita  = new No( v2, exp2, null, null );
                
                if ( exp1.length() == 1 )
                    no.esquerda.estendido = true;
                
                if ( exp2.length() == 1 )
                    no.direita.estendido = true;
                
                totalFolhas += 2; // incrementa o total de folhas
            }
            
            // se nao for o caso, a busca continua...
            else {
                
                if ( no.esquerda != null )
                    adicionar( no.esquerda, v1, exp1, v2, exp2 );
                if ( no.direita != null )
                    adicionar( no.direita, v1, exp1, v2, exp2 );
                
            }
            
        }
        
    }
    
    // metodo responsavel por provar ou refutar a arvore montada
    public void provar() {
        totalRamos = contarRamos(); // conta os ramos da arvore
        provar( topo );
    }
    
    // prova a arvore ja estruturada
    public void provar(No folha) {
        
        // verifica se a folha atual eh valida
        if ( folha != null ) {
            
            // busca a contradicao da expressao atual nas folhas seguintes
            if ( folha.valoracao == 'T' )
                buscarContradicao( folha, folha.expressao, 'F' );
            else if ( folha.valoracao == 'F' )
                buscarContradicao( folha, folha.expressao, 'T' );
            
            // se houver mais ramos, os mesmos sao verificados
            if ( folha.esquerda != null )
                provar( folha.esquerda );
            if ( folha.direita != null )
                provar( folha.direita );
            
        }
        
    }
    
    // metodo que busca pela contradicao de uma determinada expressao
    public void buscarContradicao(No folha, String exp, char v) {
        
        // se a folha for valida...
        if ( folha != null ) {
            
            // se encontrar uma folha que eh uma contradicao, entao...
            if ( folha.expressao.equals( exp ) && folha.valoracao == v ) {
                // verifica quantos ramos ha, abaixo de tal folha e os subtrai do valor total de ramos
                totalRamos -= contarRamos( folha );
                
                // anula os ramos posteiores
                folha.expressao += "*";
                
                totalFolhas -= (contarFolhas( folha ) - 1) ; // decrementa 'totalFolhas'
                //ramosFechados = contarFolhas( folha );
                
                folha.esquerda = null;
                folha.direita  = null;
            }
            
            if ( folha.esquerda != null )
                buscarContradicao( folha.esquerda, exp, v );
            if ( folha.direita != null ) 
                buscarContradicao( folha.direita, exp, v );
            
        }
        
    }
    
    // mostra o resultado da prova
    public boolean resultado() {
        if ( totalRamos >= 1 )
            return true;
        else
            return false;
    }
    
    // metodo responsavel por chamar o proximo metodo que serve para mostrar as folhas da arvore
    public void mostrar() {
        mostrar(topo.esquerda);
    }
    
    // metodo responsavel por mostrar as folhas da arvore
    public void mostrar(No no) {
        
        if ( no != null ) {
            System.out.println( no.valoracao + "[ " + no.expressao + " ]" );
            mostrar( no.esquerda );
            mostrar( no.direita );
        }
        else
            System.out.println("\n");
        
    }
    
    // conta o total de nos a partir do topo
    public int contarFolhas() {
        return contarFolhas( topo.esquerda );
    }
    
    // conta o total de nos a partir de um dado no
    public int contarFolhas( No no ) {
        if ( no != null )
            return 1 + contarFolhas( no.esquerda ) + contarFolhas( no.direita );
        else
            return 0;
    }
    
    // metodo que cuidara de encontrar o conectivo da expressao informada
    public char conectivo(String exp) {
        
        int  tamanho = exp.length();
        char c = ' ';
        int  parenteses = 0;
        boolean vez = true; // 'true' - expA | 'false' - expB
        
        // limpa os valores das expressoes gerais
        expA = expB = "";
        
        // percorre toda a expressao
        for (int i = 0; i < tamanho; i++) {
            
            // conta a quantidade de parenteses
            if ( exp.charAt(i) == '(' )
                parenteses += 1;
            else if ( exp.charAt(i) == ')' )
                parenteses -= 1;
            
            // verifica se o caractere informado eh um conectivo valido
            if ( (exp.charAt(i) == '>' || exp.charAt(i) == '^' || exp.charAt(i) == 'v') && parenteses == 0 && vez ) {
                c = exp.charAt(i);
                vez = false;
            }
            // se nao for, e a vez for da expA, a mesma eh desenvolvida
            else if ( vez )
                expA += exp.charAt(i);
            // caso a vez seja da expB, a mesma eh desenvolvida
            else
                expB += exp.charAt(i);
            
        }
        
        // retorna o conectivo
        return c;
    }
    
    // metodo que serve para um metodo que retornar o total de ramos da arvore
    public int contarRamos() {
        return contarRamos(topo);
    }
    
    // metodo que retorna o total de ramos da arvore
    public int contarRamos(No folha) {
        
        // se a folha atual for valida, entao...
        if ( folha != null ) {
            
            // se for o final de um ramo, eh retornada '1' para a soma
            if ( folha.esquerda == null && folha.direita == null )
                return 1;
            
            // caso nao seja, eh retornado '0' e a busca continua
            else 
                return 0 + contarRamos( folha.esquerda ) + contarRamos( folha.direita );
            
        }
        // caso nao seja uma folha valida...
        else
            return 0;
        
    }
    
    // metodo que ira tratar os parenteses das extremidades da expressao
    public String tratarParenteses(String exp) {
        
        String novaExpressao = "";
        int tamanho, parenteses;
        
        tamanho = exp.length();
        parenteses = 0;
        
        if ( exp.length() > 1 && exp.charAt(0) == '(' && exp.charAt(tamanho - 1) == ')' ) {
            parenteses++;
            
            for ( int i = 1; i < tamanho; i++ ) {
                if ( exp.charAt(i) == '(' )
                    parenteses++;
                if ( exp.charAt(i) == ')' )
                    parenteses--;
                    
                if ( parenteses == 0 && i != (tamanho - 1) )
                    return exp;
                
                if ( i < (tamanho - 1) )
                    novaExpressao += exp.charAt(i);
            }
            
            if ( parenteses == 0 )
                return novaExpressao;
                
        }
        
        return exp;
        
    }
}
