//[Error:MiInterfaz4|9]

interface MiInterfaz1 extends MiInterfaz2, MiInterfaz3{}

interface MiInterfaz2 extends MiInterfaz3{}

interface MiInterfaz3{}

interface MiInterfaz4 extends MiInterfaz4, MiInterfaz1, MiInterfaz3{}