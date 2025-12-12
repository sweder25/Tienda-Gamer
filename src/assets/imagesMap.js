
export const productImages = {
  1: '/images/teclado.png',
  2: '/images/mouse.png',
  3: '/images/auriculares-gaming.png',
};

export function getImageForProduct(product) {
  if (!product) return undefined;
  // Prefer explicit URL fields from backend
  if (product.imagenUrl) return product.imagenUrl;
  if (product.imagen && typeof product.imagen === 'string' && product.imagen.startsWith('http')) return product.imagen;

  const slug = (product.nombre || '').toLowerCase().trim().replace(/\s+/g, '-');
  // Try id mapping
  if (product.id && productImages[product.id]) return productImages[product.id];
  // Try slug mapping
  if (slug && productImages[slug]) return productImages[slug];
  // Default to conventional public path
  if (slug) return `/images/${slug}.png`;
  return undefined;
}
