import {Routes, Route} from 'react-router-dom';

import Home from './pages/home';
import Catalogo from './pages/catalogo';
import Carrito from './pages/carrito';
import Inicio from './pages/inicio';
import Registro from './pages/registro';
import Boleta from './pages/boleta';


export default function AppRoutes(){
    return(
        <Routes>
            <Route path='/' element = {<Home />} />
            <Route path='/catalogo' element ={<Catalogo />} />
            <Route path='/carrito' element ={<Carrito />} />
            <Route path='/inicio' element ={<Inicio />} />
            <Route path='/registro' element ={<Registro />} />
            <Route path='/boleta' element ={<Boleta />} />
            <Route path="/boleta/:ventaId" element={<Boleta />} />

        </Routes>
    )
}

