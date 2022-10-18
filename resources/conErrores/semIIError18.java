///[Error:a1|12]
//acceso a una variable de instancia heredada pero su tipo es privado
class A {

    private int a1;
    static void main() {}
}

class B extends A {

    int metodo(){
        this.a1 = 3;
    }
}