# Servicios de ThanosGym

Backend SOA para registrar clientes, membresías, ventas, pagos, notificaciones, autenticación, gateway y orquestación de procesos de ThanosGym. El proyecto se puede probar desde Postman y se diseñará un Frontend para consumir los servicios.

## Antes de iniciar

Se necesita Java JDK 21. Gradle ya viene incluido en el proyecto, así que no hace falta instalarlo aparte.


## Servicios y puertos

| Servicio | Puerto | Uso |
|---|---:|---|
| API Gateway | 8080 | Entrada principal |
| Clientes | 9001 | Registro y consulta de clientes |
| Membresías | 9002 | Planes y membresías |
| Ventas | 9003 | Registro de ventas |
| Pagos | 9004 | Confirmación de pagos y compensación |
| Notificaciones | 9005 | Notificación simulada |
| Acceso | 9006 | Login y validación de token |
| Orquestador | 9007 | Coordina el flujo completo de venta de membresía |


## Cómo levantar cada servicio

Abre una terminal por cada servicio y ejecuta el comando correspondiente:

```powershell
.\gradlew.bat :servicio-autenticacion:bootRun
.\gradlew.bat :servicio-cliente:bootRun
.\gradlew.bat :servicio-membresia:bootRun
.\gradlew.bat :servicio-venta:bootRun
.\gradlew.bat :servicio-pago:bootRun
.\gradlew.bat :servicio-notificacion:bootRun
.\gradlew.bat :servicio-orquestador:bootRun
.\gradlew.bat :servicio-gateway:bootRun
```

Recomendación de orden:

1. Primero `servicio-autenticacion`.
2. Luego clientes, membresías, ventas, pagos y notificaciones.
3. Después `servicio-orquestador`.
4. Al final `servicio-gateway`.

Los datos se guardan localmente con H2. Si se quiere trabajar con MySQL, se debe iniciar el servicio con el perfil `mysql` y configurar `DB_URL`, `DB_USER` y `DB_PASSWORD`.

## API Gateway y Orquestador

El gateway corre en:

```text
http://localhost:8080
```

Este servicio recibe todas las peticiones y verifica el token para poder redirigir a los demás servicios.

El orquestador corre en:

```text
http://localhost:9007
```

Su trabajo es coordinar el proceso de negocio completo. A diferencia del gateway, el orquestador sí llama a varios servicios en secuencia.

Ruta principal del flujo orquestado:

```text
POST http://localhost:8080/api/v1/orquestacion/venta-membresia
```

Flujo que ejecuta:

```text
Orquestador → Cliente → Membresía → Venta → Pago → Notificación simulada
```

## Usuarios de prueba

Toma el token y se envía en las demás peticiones:

```text
Authorization: Bearer <token>
```

Si se llama a clientes, ventas, pagos, notificaciones u orquestación sin token, el gateway responde `401 TOKEN_REQUERIDO`.

## Ejemplo de flujo orquestado

```json
{
  "cliente": {
    "dni": "74638291",
    "nombres": "Carlos",
    "apellidos": "Mendoza Ruiz",
    "telefono": "987654321",
    "email": "carlos@gmail.com",
    "fechaNacimiento": "1998-05-10"
  },
  "idPlan": "MENSUAL",
  "fechaInicio": "2026-07-11",
  "monto": 60.00,
  "metodoPago": "YAPE",
  "observaciones": "Venta completa desde orquestador",
  "enviarNotificacion": true
}
```

Resultado esperado:

```text
Cliente verificado o registrado
Membresía registrada
Venta registrada como PENDIENTE_PAGO
Pago confirmado y venta activada
Notificación simulada registrada
```

## Integración entre servicios

Las llamadas internas se realizan con `RestClient`:

```text
Gateway → servicios internos
Orquestador → Cliente, Membresía, Venta, Pago y Notificación
Cliente → Membresía para completar idMembresia y estadoMembresia en consultas por DNI o ID
Membresía → Cliente
Venta → Cliente y Membresía
Pago → Venta
```

Cada cliente traduce las respuestas HTTP del servicio remoto en errores de negocio, como `CLIENTE_NO_ENCONTRADO`, `PLAN_NO_DISPONIBLE`, `VENTA_NO_ENCONTRADA` o `SERVICIO_VENTA_NO_DISPONIBLE`.

También se configuraron límites de espera:

```text
Tiempo máximo para conectar: 2000 ms
Tiempo máximo para responder: 3000 ms
```

Si un servicio no responde, la operación termina con `502 Bad Gateway` y no se guarda información incompleta.

## Plan de compensación

La venta se registra primero con estado `PENDIENTE_PAGO`. Cuando Pago confirma el cobro, llama a Venta por REST y la cambia a `ACTIVA`.

```text
Venta PENDIENTE_PAGO → Pago CONFIRMADO → Venta ACTIVA
```

Si Venta no responde durante el registro del pago, la transacción de Pago se revierte. No queda un pago guardado a medias y la venta continúa pendiente para poder reintentarla.

El orquestador también aplica compensación si el flujo completo falla después de crear recursos:

```text
Pago confirmado → reembolso
Venta creada → anulación
Membresía creada → suspensión
```

Cuando el reembolso se completa, Pago ya deja la venta en `ANULADA`. El orquestador evita repetir esa anulación y continúa con la suspensión de la membresía.

Para revertir una operación ya completada se usa:

```text
POST http://localhost:8080/api/v1/pagos/{idPago}/reembolsar
```

El servicio de Pago solicita a Venta que la anule y después cambia el pago a `REEMBOLSADO`:

```text
Pago CONFIRMADO + Venta ACTIVA → Pago REEMBOLSADO + Venta ANULADA
```

## Notificaciones

El envío de notificaciones es simulado. Los mensajes quedan registrados en la base de datos, se genera un identificador tipo `MSG-XXXXXXXX`, pero no se envían a un teléfono real.

## Pruebas

```powershell
.\gradlew.bat test
.\gradlew.bat bootJar
```
