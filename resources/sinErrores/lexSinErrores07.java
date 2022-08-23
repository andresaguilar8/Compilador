private Token estado34() throws IOException, ExcepcionLexica {
        if (this.esCaracterEspecialString()) {
        this.actualizarLexema();
        this.actualizarCaracterActual();
        //return this.estado32();
        /-*/*}
        else {
        this.*/actualizarLexema();
        throw new ExcepcionLexica(this.lexema, this.manejadorDeArchivo.obtenerNumeroLinea());
        }
        }