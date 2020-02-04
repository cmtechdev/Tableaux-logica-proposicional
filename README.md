# Método Tableaux para lógica proposicional
A presente ferramenta permite a verificação de proposições por meio da implementação do método [Tableaux](https://pt.wikipedia.org/wiki/M%C3%A9todo_dos_Tableaux), também conhecido por 'Árvore Verdade' e bastante utilizado na **teoria da prova**.

## Nomenclatura utilizada 
Para organizar as formulações, a seguinte nomenclatura deve ser adotada:
  - **^** - Define a **conjunção**
  - **v** - Define a **disjunção**
  - **>** - Define a **implicação**
  - **~** - Define a **negação**
  - **|** - Define a **prova**

## Exemplo
Para o exemplo, utilize: **a>b, b>c | a>c** .
Como resultado, irá obter informações tais como o tempo (em milissegundos) que levou para a prova ser calculada e a quantidade de nós e ramos gerados, assim como a notificação de que todos os ramos foram fechados ou não.
