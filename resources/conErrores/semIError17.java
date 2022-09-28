///[Error:debugPrint|7]

//test de una clase que redefine de manera incorrecta el metodo debugPrint

class A implements B, C {

    void debugPrint(int x) {

    }

    static void main () {

    }

}

interface B {

}

interface C {

}