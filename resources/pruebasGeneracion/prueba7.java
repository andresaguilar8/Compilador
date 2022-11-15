//<0
//00
//>0

class A{
    static void main(){
        A.print(-100);
        A.print(0);
        A.print(1);
    }

    static void print(int i){
        if(i < 0) {
            System.printC('<');
            System.printCln('0');
        }
        else if(i > 0) {
            System.printC('>');
            System.printCln('0');
        }
        else {
            if (i == 0)
                ;
            System.printC('0');
            System.printCln('0');

        }
    }
}