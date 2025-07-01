# 🛒 MeLi Box

## 📱 Descripción del Proyecto

MeLi Box es una aplicación Android que permite a los usuarios buscar productos utilizando las APIs públicas de Mercado Libre, visualizar los resultados de búsqueda y ver detalles de cada producto.

## 🔌 Integración con API

### Consideraciones sobre la API de Mercado Libre

Inicialmente, la prueba técnica sugería utilizar los endpoints disponibles en [developers.mercadolibre.com.ar/es_ar/items-y-busquedas](https://developers.mercadolibre.com.ar/es_ar/items-y-busquedas), indicando que no sería necesario el uso de TOKEN para realizar el ejercicio.

Sin embargo, es importante destacar que Mercado Libre ha normalizado sus APIs, y actualmente las consultas genéricas realizadas a través del endpoint 'search' han dejado de funcionar sin autenticación. El nuevo esquema requiere:

- Especificar el ID de usuario al que se apunta en la consulta
- Incluir un TOKEN de acceso válido

### Solución implementada

Se optó por utilizar una API alternativa dentro del ecosistema de Mercado Libre: la API de catálogo. Esta API, aunque parece estar más orientada al uso de sellers, permite cumplir con los requisitos fundamentales de la prueba:

- Búsqueda de productos
- Visualización de listados
- Acceso a detalles de productos individuales

Cabe mencionar que esta API no proporciona ciertos datos comerciales como precios.

## ✨ Características Principales

- **Búsqueda de productos**: Interfaz para buscar productos del catálogo de Mercado Libre.
- **Visualización de resultados**: Lista de productos con información relevante.
- **Detalle de productos**: Vista con imágenes, descripción, características y atributos del producto.
- **Manejo de estados**: Gestión adecuada de estados de carga, éxito y error.
- **Soporte para rotación**: Preservación del estado de la aplicación durante cambios de configuración.
- **Arquitectura robusta**: Implementación de Clean Architecture y MVVM.

## 🏗️ Arquitectura

La aplicación está construida siguiendo los principios de **Clean Architecture** y el patrón **MVVM**, organizando el código en capas claramente definidas:

```
com.meli.test/
├── data/               # Capa de datos (repositorios, fuentes de datos)
│   ├── remote/         # Implementación de API y modelos de respuesta
│   │   ├── api/        # Servicios de API
│   │   ├── auth/       # Manejo de autenticación
│   │   └── model/      # Modelos de respuesta
│   └── repository/     # Implementación de repositorios
├── domain/             # Lógica de negocio
│   ├── model/          # Modelos de dominio
│   ├── repository/     # Interfaces de repositorio
│   └── usecase/        # Casos de uso
├── presentation/       # Capa de presentación (UI)
│   ├── components/     # Componentes UI reutilizables
│   ├── search/         # Pantalla de búsqueda y resultados
│   └── productdetail/  # Pantalla de detalle de producto
├── di/                 # Inyección de dependencias
├── navigation/         # Navegación entre pantallas
└── util/               # Utilidades y clases auxiliares
```

## 🛠️ Tecnologías Utilizadas

- **Kotlin**: Lenguaje principal de desarrollo
- **Jetpack Compose**: Framework para UI
- **Coroutines & Flow**: Para operaciones asíncronas y flujos de datos
- **Koin**: Inyección de dependencias
- **Retrofit**: Cliente HTTP para consumo de APIs
- **Coil**: Carga de imágenes
- **Navigation Compose**: Navegación entre pantallas
- **Unit Testing**: JUnit, Mockk

## 📱 Pantallas de la Aplicación

La aplicación consta de tres pantallas principales:

1. **Pantalla de Búsqueda**: Permite al usuario ingresar términos de búsqueda para encontrar productos.
2. **Pantalla de Resultados**: Muestra los productos encontrados en una lista optimizada.
3. **Pantalla de Detalle**: Presenta información completa sobre un producto seleccionado.

## 🧪 Testing

El proyecto incluye pruebas unitarias para garantizar la calidad del código:

- Pruebas de ViewModels
- Pruebas de Casos de Uso
- Pruebas de Repositorios

---
