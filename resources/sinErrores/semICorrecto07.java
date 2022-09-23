
//test de clase que implementa correctamente los metodos de una interfaz que extiende a otras dos
interface X {
    String metodox();
}
interface Y {
    String metodoy();
}

interface Z extends X, Y {
    String metodoz();
}

class A implements Z {

    String metodox(){

    }

    String metodoy() {

    }

    String metodoz(){

    }

    static void main() {}
}




