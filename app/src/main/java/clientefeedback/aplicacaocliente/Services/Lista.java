package clientefeedback.aplicacaocliente.Services;

import java.util.ArrayList;
import java.util.Arrays;

public class Lista<T> extends ArrayList<T>{

    public Lista() {
        super(); //chame o construtor de ArrayList
    }

    /*Esse método sobrecarrega o metodo contains, e irá funcionar
    com arrays de objetos. Você pode sobregarregar, sobrescrever
    ou adicionar mais métodos a esta classe, de forma que
    ela fique ainda mais poderosa, e usar ela, em vez de ArrayList.
    */
    public boolean contains(Object[] o){
        //percorre o bjeto 'Lista':
        for(int i=0; i< this.size(); i++){
            /* cria um array de objetos e faz um 'type-cast'
            desta própria classe atribuindo a ele o valor do
            elemento, e isso para cada elemento, de forma que
            todos os elementos sejam tratados como arrays de
            objetos:
            */
            Object[] toCompare = (Object[])this.get(i);
            /* através do metodo equals da classe Arrays, e do loop
            for-each, verifica se algum elemento do array candidato ao
            add() já está adicionado na lista, se sim é retornado true,
            se não, é retornado false.
            */
            if(Arrays.equals(o, toCompare)){
                return true;
            }
        }
        return false;
    }
}