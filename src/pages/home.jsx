export default function Home (){
    return(
        <div class="container px-4 px-lg-5">

            <div class="row gx-4 gx-lg-5 align-items-center my-5">
                <div class="col-lg-7"><img class="img-fluid rounded mb-4 mb-lg-0" src="src\components\img\FOTO-GAME-1 (1) (1).jpg" alt="..."/></div>
                <div class="col-lg-5">
                    <h1 class="font-weight-light">Tienda Gamer</h1>
                    <p>!Bienvenido a la Tienda Gamer¡ Aqui encontraras los mejores precios para tus productos gamer</p>
                </div>
            </div>

            <div class="row gx-4 gx-lg-5">
                <div class="col-md-4 mb-5">
                    <div class="card h-100">
                        <div class="card-body">
                            <h2 class="card-title">Teclados Mecánicos</h2>
                            <p class="card-text">
                                Descubre teclados mecánicos de alto rendimiento ideales para gaming y productividad, con switches precisos, retroiluminación RGB y una experiencia de escritura superior.
                            </p>
                        </div>
                        <div class="card-footer"><a class="btn btn-primary btn-sm" href="#!">Ver Más</a></div>
                    </div>
                </div>

                <div class="col-md-4 mb-5">
                    <div class="card h-100">
                        <div class="card-body">
                            <h2 class="card-title">Tarjetas Gráficas</h2>
                            <p class="card-text">
                                Potencia tu PC con tarjetas gráficas de última generación, perfectas para juegos exigentes, edición de video y rendimiento gráfico de alto nivel.
                            </p>
                        </div>
                        <div class="card-footer"><a class="btn btn-primary btn-sm" href="#!">Ver Más</a></div>
                    </div>
                </div>

                <div class="col-md-4 mb-5">
                    <div class="card h-100">
                        <div class="card-body">
                            <h2 class="card-title">Sillas Gamer</h2>
                            <p class="card-text">
                                Sillas gamer ergonómicas diseñadas para largas sesiones de juego, brindando comodidad, soporte lumbar y estilo moderno para tu setup.
                            </p>
                        </div>
                        <div class="card-footer"><a class="btn btn-primary btn-sm" href="#!">Ver Más</a></div>
                    </div>
                </div>
            </div>
        </div>
    )
}