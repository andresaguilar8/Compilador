///[Error:y|8]
//error en el pasaje de parametros al invocar al metodo y
class A extends B {

    public boolean y;

    void met() {
        var x = this.y(y);
    }

    B y(String x) {
    }
}