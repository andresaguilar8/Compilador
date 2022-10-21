package Pruebas;

public class B extends A implements Y{

    public X x;
    public Y y;
    public B b;





    void met() {
//        y = x; --> error
        x = y;
        x = b;
        if (b == x) {

        }
        if (x == y)
            if (y == x)
                return;
        A a = new B();
    }

    @Override
    public void metodoY() {

    }

    @Override
    public void metodox() {

    }
}
