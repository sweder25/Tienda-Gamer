import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import AppRoutes from './route.jsx'
import { BrowserRouter } from 'react-router-dom'
import Navbar from './components/navbar.jsx'
import Footer from './components/footer.jsx'
import { AuthProvider } from './context/AuthContext.jsx'
import { CarritoProvider } from './context/CarritoContext.jsx'

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <BrowserRouter>
      <AuthProvider>
        <CarritoProvider>
          <Navbar />
          <AppRoutes />
          <Footer />
        </CarritoProvider>
      </AuthProvider>
    </BrowserRouter>
  </StrictMode>,
)