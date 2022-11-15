//8=8
//3=3
//6=6
//5=5
//1=1
//false=false
//true=true
//false=false
//true=true
//true=true
//true=true
//false=false
//true=true

class A{
    static void main(){
        System.printI(8);
        System.printC('=');
        System.printIln(5 + 3);

        System.printI(3);
        System.printC('=');
        System.printIln(10 - 7);

        System.printI(6);
        System.printC('=');
        System.printIln(2 * 3);

        System.printI(5);
        System.printC('=');
        System.printIln(10 / 2);

        System.printI(1);
        System.printC('=');
        System.printIln(11 % 2);

        System.printB(false);
        System.printC('=');
        System.printBln(true && false);

        System.printB(true);
        System.printC('=');
        System.printBln(true || false);

        System.printB(false);
        System.printC('=');
        System.printBln(10 == 11);

        System.printB(true);
        System.printC('=');
        System.printBln('c' != 'a');

        System.printB(true);
        System.printC('=');
        System.printBln(3 < 5);

        System.printB(true);
        System.printC('=');
        System.printBln(4 <= 4);

        System.printB(false);
        System.printC('=');
        System.printBln(6 > 8);

        System.printB(true);
        System.printC('=');
        System.printBln(8 >= 2);
    }
}