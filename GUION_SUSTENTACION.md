# 🎓 GUION DE SUSTENTACIÓN Y EXPLICACIÓN DE DIAGRAMAS
## Caso Práctico: Patrón Command en Automatización de Edificios Inteligentes

---

## 📊 BLOQUE 1: EXPLICACIÓN DE LOS DOS DIAGRAMAS

### 🔴 1. DIAGRAMA ANTES (Arquitectura Acoplada - Sin Patrón)
* **¿Qué clase está arriba?**  
  `ControlPanel` (Panel de Control central).
* **¿Qué significan las 4 flechas que bajan?**  
  Son flechas de **asociación y dependencia directa** hacia `Light`, `AirConditioner`, `SmartLock` y `AlarmSystem`.
* **¿Qué hace el código por dentro?**  
  `ControlPanel` tiene atributos directos como `List<Light> lights` o `AlarmSystem alarm`. Para prender una luz, llama directamente a `light.turnOn()`.
* **Los 3 problemas graves para explicar:**
  1. **Alto Acoplamiento:** El panel está "amarrado" a 4 clases concretas.
  2. **Viola el Principio Abierto/Cerrado (OCP):** Si mañana instalas persianas o rociadores, estás obligado a modificar el código del `ControlPanel`.
  3. **Imposible hacer Deshacer (Undo):** El panel no guarda historial ni sabe qué estado tenían los dispositivos antes de dar la orden.

---

### 🟢 2. DIAGRAMA DESPUÉS (Arquitectura Desacoplada - Con Patrón Command)
* **¿Qué cambió a simple vista?**  
  ¡`ControlPanel` **ya no tiene ninguna flecha hacia los dispositivos**! Se eliminó el acoplamiento directo.
* **Los 4 participantes del Patrón GoF:**
  1. **Invocador (`ControlPanel` - *Invoker*):** Solo conoce la interfaz `Command`. No sabe qué aparato físico existe; solo llama a `command.execute()`.
  2. **Interfaz (`Command`):** Contrato que obliga a implementar `execute()` y `undo()`.
  3. **Comandos Concretos (`TurnOnLightCommand`, `SetTemperatureCommand`, etc.):** Encapsulan la orden. **Tienen memoria:** guardan en atributos privados el estado previo del aparato para poder restaurarlo con precisión en `undo()`.
  4. **Receptores (`Light`, `AirConditioner`, etc. - *Receivers*):** Conservan el control real del hardware, pero ahora nadie los conoce directamente excepto su comando específico.
* **Los dos componentes avanzados:**
  * **`MacroCommand`:** Agrupa varios comandos para el **Modo Noche** (apagar luces, clima a 21°C, cerrar puertas, armar alarma). Al hacer `undo()`, **los revierte en orden inverso** (LIFO: $N \rightarrow 1$).
  * **`CommandHistory`:** Pila LIFO que registra las órdenes para permitir auditoría y deshacer ilimitado.

---

## 🎙️ BLOQUE 2: GUION PALABRA POR PALABRA PARA EXPONER
*(Tiempo aproximado: 4 a 5 minutos)*

### Minuto 1: Introducción
> *"Buenos días, profesor y jurado. Hoy presento el desarrollo de un **Sistema Inteligente de Automatización de Edificios**, diseñado para demostrar de forma práctica el funcionamiento e impacto arquitectónico del **Patrón de Diseño Command**."*
>
> *"El sistema controla cuatro familias de dispositivos: luces, climatización, cerraduras y alarmas. Para evidenciar las ventajas del patrón, desarrollé el proyecto en dos implementaciones independientes: la versión inicial **sin el patrón** y la versión final **con el patrón Command**."*

### Minuto 2: Explicando el Diagrama "ANTES"
*(Señala el diagrama ANTES)*
> *"Analicemos primero la arquitectura inicial. Como observan en este diagrama, la clase `ControlPanel` depende directamente de las cuatro clases concretas de los dispositivos."*
>
> *"Esto genera tres problemas críticos:"*
> 1. *"**Alto Acoplamiento:** El panel conoce los métodos específicos de cada hardware."*
> 2. *"**Violación de OCP:** Si agregamos persianas o aspersores, tenemos que modificar y recompilar `ControlPanel`."*
> 3. *"**Imposibilidad de Deshacer (Undo):** Como las llamadas son directas, no hay memoria que recuerde el estado anterior de los aparatos."*

### Minuto 3: Explicando el Diagrama "DESPUÉS"
*(Cambia al diagrama DESPUÉS)*
> *"Para solucionar esto, aplicamos el patrón **Command**, cuyo principio es 'encapsular una petición como un objeto'. Fíjense en los cambios principales:"*
>
> 1. *"**Desacoplamiento:** `ControlPanel` ahora actúa como Invocador puro. Ya no conoce a ningún dispositivo receptor; únicamente interactúa con la interfaz abstracta `Command`."*
> 2. *"**Comandos con Memoria:** Creamos clases concretas como `TurnOnLightCommand`, `SetTemperatureCommand` o `LockDoorCommand`. Cada comando almacena en variables privadas el estado previo del receptor antes de mutarlo, lo que permite restaurarlo con fidelidad total al invocar `undo()`."*
> 3. *"**Historial LIFO:** Incorporamos `CommandHistory` para registrar las operaciones y soportar auditoría y reversión."*
> 4. *"**Modo Noche con MacroCommand:** Agrupamos las acciones en un solo macro. La regla de oro: al presionar 'Deshacer', el macro revierte los comandos **en orden inverso**, garantizando la simetría y consistencia del sistema."*

### Minuto 4: Demostración Práctica (GUI o Consola)
*(Muestra la GUI abierta o la consola)*
> *"Para validar este comportamiento, desarrollamos una interfaz gráfica táctil en Java con diseño dark mode. Al activar el **Modo Noche**, todos los dispositivos pasan al estado nocturno en una sola transacción. Si ahora presiono **DESHACER (Undo)**, el sistema extrae el macro de la pila LIFO y revierte cada dispositivo a su estado diurno exacto anterior."*

### Minuto 5: Conclusión
> *"En conclusión, el patrón Command nos permitió transformar un sistema rígido en una arquitectura modular, extensible y transaccional, cumpliendo con los principios SOLID."*
>
> *"Muchas gracias. Quedo atento a sus preguntas."*

---

## 🧠 BLOQUE 3: PREGUNTAS TÍPICAS DEL JURADO

* **¿Por qué el `MacroCommand` revierte en orden inverso?**  
  * *"Por consistencia causal y simetría transaccional. Si primero desbloqueas una puerta y luego la abres, para deshacer debes primero cerrarla y luego bloquearla. El orden inverso LIFO evita inconsistencias."*
* **¿Dónde se guarda el estado para el Undo?**  
  * *"En cada comando concreto. Al ejecutar `execute()`, guarda el estado actual del receptor en variables privadas antes de aplicar el cambio."*
* **¿Por qué Command es de comportamiento y no estructural?**  
  * *"Porque su propósito no es ensamblar estructuras de datos, sino organizar la comunicación, desacoplamiento y flujo de responsabilidades entre objetos en tiempo de ejecución."*

