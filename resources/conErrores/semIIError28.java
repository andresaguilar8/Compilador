///[Error:m|10]

//el metodo m no tiene retorno y tiene un encadenado
class B {

    public int y;

    void m() {
        var x = new A();
        x = x.f().m().y;
    }

    static void main() {}
}

class A  {

    B f() {

    }
}