# challenge

API reactiva con Spring Boot para consultar el precio aplicable de un producto por marca y fecha.

## Tecnologias

- Java 25
- Spring Boot 4.0.2
- Spring WebFlux (Reactor)
- Spring Data R2DBC
- H2 en memoria
- MapStruct
- JUnit + Mockito

## Estructura del proyecto

- `src/main/java/com/napptilus/challenge/domain`: modelo, puerto y servicio de dominio
- `src/main/java/com/napptilus/challenge/application`: handler de aplicacion
- `src/main/java/com/napptilus/challenge/infrastructure/http`: controller, DTOs, mappers y manejo de excepciones
- `src/main/java/com/napptilus/challenge/infrastructure/persistence`: repositorio R2DBC, entidad, mapper y excepcion
- `src/main/resources/db`: scripts SQL (`schema.sql`, `data.sql`)
- `src/test/java`: tests unitarios
- `src/testIntegration/java`: tests de integracion

## Ejecucion local

```bash
./mvnw spring-boot:run
```

La base de datos H2 en memoria se inicializa automaticamente con:

- `src/main/resources/db/schema.sql`
- `src/main/resources/db/data.sql`

## Endpoint principal

### Obtener precio aplicable

```http
GET /api/v1/brands/{brand_id}/products/{product_id}/prices?application_date=YYYY-MM-DDTHH:MM:SS
```

Ejemplo:

```bash
curl "http://localhost:8080/api/v1/brands/1/products/35455/prices?application_date=2020-06-14T16:00:00"
```

Respuesta 200 (ejemplo):

```json
{
  "product_id": 35455,
  "brand_id": 1,
  "price_list": 2,
  "start_date": "2020-06-14T15:00:00",
  "end_date": "2020-06-14T18:30:00",
  "price": 25.45,
  "currency": "EUR"
}
```

## Codigos de error implementados

- `404` cuando no existe precio (`ProductPriceNotFoundException`)
- `400` para parametros invalidos/faltantes:
    - `ERR_PRODUCT_PRICE_MISSING_APPLICATION_DATE`
    - `ERR_PRODUCT_PRICE_INVALID_PARAM`
    - `ERR_PRODUCT_PRICE_RESOURCE_NOT_FOUND`
- `500` para error inesperado:
    - `ERR_PRODUCT_PRICE_UNEXPECTED`

## Tests

### Unitarios

```bash
./mvnw test
```

### Integracion

```bash
./mvnw failsafe:integration-test failsafe:verify
```

## Packaging

### JAR (por defecto)

```bash
./mvnw clean package
```

### WAR (perfil `war`)

```bash
./mvnw clean package -Pwar
```

## Nota sobre despliegue WAR

Se priorizó la coherencia con WebFlux (runtime reactivo puro) y se descartó SpringBootServletInitializer para evitar
dependencia servlet en un servicio Reactor/R2DBC. Esto elimina errores de arranque por clases jakarta.servlet y mantiene
una ejecución consistente con la arquitectura no bloqueante del proyecto.
