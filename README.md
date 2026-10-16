# Sistema Pizza FZ — Arquitectura Hexagonal

## Estructura

```
domain/
  model/        Cliente, Cocinero, Administrador (extienden Usuario)
  exception/    Errores de negocio (DatosInvalidosException, CuentaExistenteException)
  service/      Lógica de dominio pura, sin frameworks (ValidadorUsuarioService)
  port/in/      Qué puede pedir el exterior al dominio (casos de uso)
  port/out/     Qué necesita el dominio del exterior (persistencia, hashing...)

application/
  usecase/      Implementación de los puertos de entrada. Orquesta dominio + puertos.
                No contiene reglas de negocio, solo coordina.

infrastructure/
  adapter/in/web/          Controllers REST (traducen HTTP <-> dominio)
  adapter/out/persistence/ Implementaciones de los puertos de salida (JPA, hashing, etc.)
  config/                  Beans de Spring para objetos de dominio "puros"
```

Regla de dependencia: `infrastructure` → `application` → `domain`.
El dominio **nunca** importa nada de Spring ni de infraestructura.

## Cómo se mapeó tu diagrama de "registrarse"

| Elemento del diagrama                  | Dónde vive en el código                              |
|-----------------------------------------|-------------------------------------------------------|
| Caso de uso "registrarse"               | `RegistrarUsuarioUseCase` (puerto in) + `RegistrarUsuarioService` (impl) |
| `<<incluye>>` validar datos del usuario | `ValidadorUsuarioService.validar()` — se ejecuta SIEMPRE, primero |
| `<<extend>>` informar cuenta existente  | `CuentaExistenteException`, lanzada solo si el email ya existe |
| `<<extend>>` corregir datos             | `DatosInvalidosException`, lanzada solo si falla la validación; el controller la traduce a HTTP 400 |
| Actor "Nuevo cliente"                   | `Cliente extends Usuario` |

**Patrón general**: un `<<incluye>>` es un paso que siempre se ejecuta dentro
del método del use case (llamada directa, sin condición). Un `<<extend>>` es
un paso condicional: se modela como una rama `if` dentro del use case que,
si se cumple, lanza una excepción de dominio específica — el adaptador de
entrada decide cómo comunicarlo (HTTP 400, 409, un evento, etc.).

## Cómo replicar el patrón para los demás casos de uso

Para cada burbuja verde/rosa/azul del diagrama general (ej. "realizar pedido",
"iniciar sesión", "gestionar inventario"):

1. **Puerto in**: `domain/port/in/XxxUseCase.java` con su `record` de comando.
2. **Puertos out** que necesite (repositorios, notificador de pago, etc.) en `domain/port/out/`.
3. **Reglas de negocio puras** (si las hay) como método de un modelo o un
   servicio de dominio en `domain/service/`.
4. **Implementación** en `application/usecase/XxxService.java`, orquestando
   validaciones (include) y condiciones (extend) como en `RegistrarUsuarioService`.
5. **Adaptador de entrada** (controller) que traduce HTTP <-> comando/dominio.
6. **Adaptador de salida** que implemente los puertos out con JPA u otra tecnología.

## Próximos pasos sugeridos

- Iniciar sesión (`IniciarSesionUseCase`) con Spring Security + JWT.
- Reemplazar `UsuarioRepositoryAdapter` (memoria) por una versión JPA:
  crear `UsuarioJpaEntity`, `UsuarioJpaRepository extends JpaRepository`,
  y un mapper Entity <-> Dominio.
- Modelar `Pizza`, `Pedido`, `ItemPedido`, `Carrito` como agregados del dominio.
- Repetir el patrón de "registrarse" para "realizar pedido" (que probablemente
  incluya "validar stock" y extienda a "aplicar promoción").

## CI: pruebas en Codespaces y GitHub Actions

- Pruebas JUnit: `src/test/java/.../*ServiceTest.java`
- Pruebas Mockito (puertos simulados con `@Mock`/`@InjectMocks`): `*MockitoTest.java`
- **Errores premeditados**: `ErroresPremeditadosTest.java` (2 pruebas que fallan a propósito para verlas en Actions).
- Workflow: `.github/workflows/ci.yml` corre `mvn -B test` en cada push.
- Codespaces: `.devcontainer/devcontainer.json` (Java 17 + Maven + Docker).

### Subir a GitHub (esta carpeta `pizza-fz` debe ser la raíz del repo)

```bash
git init
git add .
git commit -m "CI con JUnit y Mockito"
git branch -M main
git remote add origin https://github.com/<usuario>/<repo>.git
git push -u origin main
```

Luego: pestaña **Actions** → el job saldrá en rojo con los 2 errores premeditados.
En Codespaces (**Code → Codespaces → Create codespace**): `mvn test`.

### Docker (en la terminal del Codespace)

```bash
docker build -t pizza-fz-app .
docker run -d -p 8080:8080 --name pizza-fz-container pizza-fz-app
docker logs -f pizza-fz-container
```
