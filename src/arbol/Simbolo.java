/*
 * Ejemplo desarrollado por Erick Navarro
 * Blog: e-navarro.blogspot.com
 * Julio - 2018
 */
package arbol;

import java.util.LinkedList;

/**
 * Clase símbolo, que es un nodo de la tabla de símbolos, estos símbolos son 
 * variables con su valor, identificador y tipo, actualmente todas las variables 
 * son de tipo número, pero se colocó la variable tipo por si se quisiera ampliar
 * el lenguaje y agregar más tipos.
 * @author Erick
 */
public class Simbolo {
    /**
     * Bandera que indica si el símbolo creado es un parámetro o no.
     */
    private boolean parametro;
    /**
     * Si el símbolo es un parámetro y además ya se le asigno valor esta bandera es verdadera, de lo contrario es falsa.
     */
    private boolean parametroInicializado;
    /**
     * Tipo del símbolo que se almacena
     */
    private final Tipo tipo;
    /**
     * Identificador del símbolo que se almacena
     */
    private final String id;
    /**
     * Lista de los tamaños de las dimensiones del arreglo, en caso de que lo que
     * se almacene sea un arreglo
     */
    private final LinkedList<Integer> tamaniosDimensiones;
    /**
     * Variable que almacena el valor del símbolo para la operación de ejecución
     */
    private Object valor;    
    /**
     * Constructor de la clase Símbolo.
     * @param id identificador de la variable que se desea almacenar
     * @param tipo tipo de la variable que se desea almacenar
     */
    public Simbolo(String id, Tipo tipo) {
        this.tipo = tipo;
        this.id = id;
        this.tamaniosDimensiones = null;
        this.parametro=false;
        this.parametroInicializado=false;
    }
    /**
     * Constructor para los símbolos que almacenan un arreglo
     * @param id Identificador del arreglo
     * @param tipo Tipo del arreglo
     * @param td 
     */
    public Simbolo(String id, Tipo tipo, LinkedList<Integer> td) {
        this.tipo = tipo;
        this.id = id;
        this.tamaniosDimensiones = td;
        NodoArreglo arr=new NodoArreglo();
        arr.setTipo(tipo);
        arr.inicializarNodo(tamaniosDimensiones.size(), 1, tamaniosDimensiones);
        this.valor=arr;
    }
    /**
     * Método que devuelve el identificador de la variable almacenada en el símbolo.
     * @return Identificador de la variable
     */
    public String getId() {
        return id;
    }
    /**
     * Método que devuelve el valor que almacena la variable.
     * @return Valor de la variable
     */
    public Object getValor() {
        return valor;
    }
    /**
     * Método que asigna un nuevo valor a la variable.
     * @param v Nuevo valor para la variable.
     * @return retorno indica si se asigno de forma correcta la variable
     */
    boolean setValor(Object v) {
        //Variable que alberga el tipo del valor a asignar
        Tipo vTipo=null;
        boolean retorno = false;
        if (v instanceof Double) {
            vTipo = Simbolo.Tipo.NUMERO;
        } else if(v instanceof String) {
            vTipo = Simbolo.Tipo.CADENA;
        } else if(v instanceof Boolean){
            vTipo = Simbolo.Tipo.BOOLEANO;
        }
        if(v instanceof NodoArreglo && valor instanceof NodoArreglo){// se va asignar un arreglo a una variable arreglo 
            // If nuevo que valida que se asignara un arreglo a otro -> arreglo1 = arreglo2
                NodoArreglo arregloNuevo = (NodoArreglo) v;
                NodoArreglo arregloActual = (NodoArreglo) this.valor;
                vTipo = arregloNuevo.getTipo();
                // verificar tamaño de las dimensiones de cada arreglo
                if(verificarDimensionesArreglo(arregloActual, arregloNuevo)){
                    if(vTipo == tipo){
                        valor=v;
                        retorno = true;
                    }else{
                        System.err.println("Esta intentando asignar un arreglo de "
                            + "tipo " + arregloNuevo.getTipo()
                            + " a un arreglo ["+id+"] de tipo " + arregloActual.getTipo());
                    }
                }else{
                    System.err.println("Las dimensiones del arreglo a asignar no"
                            + " coinciden con las dimensiones del arreglo ["+id+"]");
                }
        }else if(!(v instanceof NodoArreglo) && !(valor instanceof NodoArreglo)){
            //Esto ya existia, solamente se movio a este if que valida la asignacion de variableSimple1 = variableSimple2
            //¿Es el valor a asignar igual al tipo de la variable a la que se le va a asignar el valor?
            if(vTipo == tipo){
                valor=v;
                retorno = true;
            }else{
                System.err.println("Esta intentando asignar un valor de tipo ["+(vTipo==null?"null":vTipo.name())+"] a la variable ["+id+"] de tipo ["+tipo.name()+ "], esto no está permitido.");
            }
        }else{
            // Si se llega a este caso ambos son errores ya que asigna 
            // VariableSimple = Arreglo y Arreglo = VariableSimple lo cual no se puede
            if(!(v instanceof NodoArreglo) && valor instanceof NodoArreglo){
                System.err.println("Esta intentando asignar un valor de tipo ["+(vTipo==null?"null":vTipo.name())+"] a la variable [Arreglo "+id+"] de tipo ["+tipo.name()+ "], esto no está permitido.");
            }else{
                System.err.println("Esta intentando asignar un valor de tipo [Arreglo] a la variable ["+id+"] de tipo ["+tipo.name()+ "], esto no está permitido.");
            }
        }
        return retorno;
    }
    /**
     * Método que setea un valor en cierta celda de un arreglo.
     * @param val Valor a asignar
     * @param indices Valores de los indices con los que se quiere acceder al arreglo para asignar el valor
     */
    void setValor(Object val, LinkedList<Integer> indices) {
        if(this.valor instanceof NodoArreglo){
            if(this.tamaniosDimensiones.size()==indices.size()){
                NodoArreglo arr=(NodoArreglo)this.valor;
                arr.setValor(indices.size(), 1, indices, val, id);
            }else{
                System.err.println("La cantidad de indices indicados no "
                        + "coincide con la cantidad de dimensiones del arreglo "+id+", no puede asignársele un valor.");            
            }
        }else{
            System.out.println("La variable "+id+" no es un arreglo, por lo "
                    + "que no puede asignársele un valor de esta manera.");
        }
    }
    /**
     * Método con el que se obtiene el valor de cierta celda de un arreglo.
     * @param id Identificador del arreglo
     * @param indices Valores de los indices con los que se quiere acceder al arreglo para obtener el valor
     * @return Cuando se encuentra la celda y su valor se devuelve dicho valor, de lo contrario se devuelve null
     */
    Object getValor(String id, LinkedList<Integer> indices) {
        if(this.valor instanceof NodoArreglo){
            if(this.tamaniosDimensiones.size()==indices.size()){
                NodoArreglo arr=(NodoArreglo)this.valor;
                return arr.getValor(indices.size(), 1, indices, id);
            }else{
                System.out.println("La cantidad de indices indicados no "
                        + "coincide con la cantidad de dimensiones del arreglo "+id+", no puede accederse a este arreglo.");            
            }
        }else{
            System.out.println("La variable "+id+" no es un arreglo, por lo "
                    + "que no se puede accesar de esta manera.");
        }
        return null;
    }
    
    boolean verificarDimensionesArreglo(NodoArreglo Actual, NodoArreglo Nuevo){
        LinkedList<NodoArreglo> arregloActual = Actual.getCeldasVecinas();
        LinkedList<NodoArreglo> arregloNuevo = Nuevo.getCeldasVecinas();
        if(arregloActual.size() == arregloNuevo.size()){
            if(arregloActual.size() == 0){
                return true;
            }else{
                return verificarDimensionesArreglo(arregloActual.get(0), arregloNuevo.get(0));
            }
        }
        return false;
    }
    
    /**
     * Enumeración que lista todos los tipos de variable reconocidos en el lenguaje.
     */
    public static enum Tipo{
        NUMERO,
        CADENA,
        BOOLEANO,
        VOID
    }
    /**
     * Método que devuelve el valor de la bandera parámetro.
     * @return 
     */
    public boolean isParametro() {
        return parametro;
    }
    /**
     * Método con el que se configura el valor de la bandera parámetro.
     * @param parametro 
     */
    public void setParametro(boolean parametro) {
        this.parametro = parametro;
    }
    /**
     * Método que devuelve el valor de la bandera parámetro inicializado.
     * @return 
     */
    public boolean isParametroInicializado() {
        return parametroInicializado;
    }
    /**
     * Método con el que se configura el valor de la bandera parámetro inicializado.
     * @param parametroInicializado
     */
    public void setParametroInicializado(boolean parametroInicializado) {
        this.parametroInicializado = parametroInicializado;
    }
}
