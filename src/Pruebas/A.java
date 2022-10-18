package Pruebas;

public class A implements X, Y{

    private int x =3;

    public static A mae() {
        mae().x = 3;
        return new A();
    }

    void met() {
        this.mae().x = 3;
    }

    B x(){
        return this.x();
    }

    public Y a(){
//        this
//        this.x == 4;
        var a = new A();
        this.metodox((3+4*(23+1)));
        var b = new A();
        //var x = 3;
        if (true) {
            var x = 3;
            b = new A(); {x =3;} {{

            }x= 3;}
        }
//        var x = (a != b) ;
        var x = 3;
        return a;
    }
    public void metodox(int xa) {
        Y y = new A();
        X x = new A();
        A a = new A();
        return;
    }


    void m1(int p1)
    {


        {

            { var y = 2;
                var x = 3;
                //var x = 20;
            }
            var y = 3;

        }
        var x = 1;

    }

    @Override
    public void metodox() {

    }

    @Override
    public void metodoY() {

    }
}
