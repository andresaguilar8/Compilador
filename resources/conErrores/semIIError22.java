///[Error:=|13]
//nodo asignacion con lado izquierdo no asignable
class A {

    private int a1;
    static void main() {}
}

class B extends A {

    int metodo(){

      this.metodo() = 3;
    }
}