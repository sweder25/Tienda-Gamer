import React, { createContext, useState, useContext, useEffect } from 'react';

const CarritoContext = createContext();

export const useCarrito = () => {
    const context = useContext(CarritoContext);
    if (!context) {
        throw new Error('useCarrito debe usarse dentro de CarritoProvider');
    }
    return context;
};

export const CarritoProvider = ({ children }) => {
    const [carrito, setCarrito] = useState([]);

    // Cargar carrito del localStorage al iniciar
    useEffect(() => {
        const carritoGuardado = localStorage.getItem('carrito');
        if (carritoGuardado) {
            try {
                setCarrito(JSON.parse(carritoGuardado));
            } catch (error) {
                console.error('Error al cargar carrito:', error);
                localStorage.removeItem('carrito');
            }
        }
    }, []);

    // Guardar carrito en localStorage cuando cambie
    useEffect(() => {
        localStorage.setItem('carrito', JSON.stringify(carrito));
    }, [carrito]);

    const agregarAlCarrito = (producto, cantidad = 1) => {
        setCarrito(prevCarrito => {
            const existe = prevCarrito.find(item => item.id === producto.id);
            
            if (existe) {
                // Si ya existe, aumentar cantidad
                return prevCarrito.map(item =>
                    item.id === producto.id
                        ? { ...item, cantidad: item.cantidad + cantidad }
                        : item
                );
            }
            
            // Si no existe, agregarlo
            return [...prevCarrito, { ...producto, cantidad }];
        });
    };

    const eliminarDelCarrito = (productoId) => {
        setCarrito(prevCarrito => prevCarrito.filter(item => item.id !== productoId));
    };

    const actualizarCantidad = (productoId, cantidad) => {
        if (cantidad <= 0) {
            eliminarDelCarrito(productoId);
            return;
        }
        
        setCarrito(prevCarrito =>
            prevCarrito.map(item =>
                item.id === productoId ? { ...item, cantidad } : item
            )
        );
    };

    const limpiarCarrito = () => {
        setCarrito([]);
        localStorage.removeItem('carrito');
    };

    const calcularTotal = () => {
        return carrito.reduce((total, item) => total + (item.precio * item.cantidad), 0);
    };

    const cantidadTotal = carrito.reduce((total, item) => total + item.cantidad, 0);

    const value = {
        carrito,
        agregarAlCarrito,
        eliminarDelCarrito,
        actualizarCantidad,
        limpiarCarrito,
        calcularTotal,
        cantidadTotal
    };

    return (
        <CarritoContext.Provider value={value}>
            {children}
        </CarritoContext.Provider>
    );
};