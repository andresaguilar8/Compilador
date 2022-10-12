class A extends B {

    public String y;

    void met() {
        var x = this.y(y);
    }

    B y(String x) {
    }
}

class B {
    static void main() {}
}