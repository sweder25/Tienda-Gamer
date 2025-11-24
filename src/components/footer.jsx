export default function Footer() {
  return (
    <div className="container">
      <footer className="py-5">
        <div className="row">
          <div className="col-6 col-md-2 mb-3">
            <h5>Accesos rapidos</h5>
            <ul className="nav flex-column">
              <li className="nav-item mb-2"><a href="#" className="nav-link p-0 text-body-secondary">Home</a></li>
              <li className="nav-item mb-2"><a href="#" className="nav-link p-0 text-body-secondary">Precios</a></li>
              <li className="nav-item mb-2"><a href="#" className="nav-link p-0 text-body-secondary">Preguntas Frecuentes</a></li>
              <li className="nav-item mb-2"><a href="#" className="nav-link p-0 text-body-secondary">Acerca de</a></li>
              <li className="nav-item mb-2"><a href="/noticias" className="nav-link p-0 text-body-secondary">Noticias</a></li>
            </ul>
          </div>


          <div className="col-md-5 offset-md-1 mb-3">
            <form>
              <h5>Suscríbete a nuestro correo </h5>
              <p>!No te pierdas ninguna de nuestras increibles ofertas¡</p>
              <div className="d-flex flex-column flex-sm-row w-100 gap-2">
                {/* Corrección de 'for' a 'htmlFor' */}
                <label htmlFor="newsletter1" className="visually-hidden">Email</label>
                {/* Corrección: etiqueta input cerrada con '/>' */}
                <input id="newsletter1" type="email" className="form-control" placeholder="Email address" />
                <button className="btn btn-primary" type="button">Suscribirse</button>
              </div>
            </form>
          </div>
        </div>

        <div className="d-flex flex-column flex-sm-row justify-content-between py-4 mt-4 border-top">
        <p className="mb-0">© 2025 Company, Inc. All rights reserved.</p>
            <ul className="list-unstyled d-flex mb-0">
            <li className="ms-3">
              <a className="link-body-emphasis" href="#" aria-label="Instagram">
                <svg className="bi" width="24" height="24">

                  <use xlinkHref="#instagram"></use>
                </svg>
              </a>
            </li>
            <li className="ms-3">
              <a className="link-body-emphasis" href="#" aria-label="Facebook">
                <svg className="bi" width="24" height="24" aria-hidden="true">
                  <use xlinkHref="#facebook"></use>
                </svg>
              </a>
            </li>
          </ul>
        </div>
      </footer>
    </div>
  );
}