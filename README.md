# Spring proyecto 2

La idea de este proyecto es aplicar inversion de control utilizando annotations en vez de archivo de configuración.

## Para correr este proyecto

Hay que agregar spring de forma manual. Se puede hacer descargando spring del siguiente [enlace](https://repo.spring.io/ui/native/libs-release/org/springframework/spring/5.3.9/spring-5.3.9-dist.zip) y copiando el contenido de `spring-5.3.9-dist/spring-framework-5.3.9/libs` en una carpeta dentro de nuestro proyecto. Todos estos archivos copiados deben ser agregados al classpath.

## Java annotations

Son etiquetas/marcadores especiales que agregamos a las clases de java y proveen metadatos acerca de las clases.
Estas anotaciones pueden ser procesadas en tiempo de ejecución o tiempo de compilación.

Por ejemplo la annotation `@override` se procesa en tiempo de compilación indicando que la clase va a usar el método indicado en la clase y no la superclase.

### ¿Por que usar spring con annotations?

Los archivos de configuración XML pueden ser engorrosos, por ejemplo con 300 beans debemos escribir todos en el archivo de configuraciones. En cambio las annotations nos permiten minimizar el archivo de configuraciones XML.

### Escaneo de componentes de clase

Spring escanea las clases de java en busca de annotations y cuando las encuentra automáticamente registra el bean en el spring container.
Para que spring escanee nuestras clases es necesario habilitar este comportamiento en el archivo de configuraciones.

```
<beans ..->
	<context:component-scan base-package="com.luv2code.springdemo" />
</beans>
```

La idea es que con este código le decimos a spring que escanee todo las clases del paquete `com.luv2code.springdemo` y los subpaquetes.

Para convertir una clase en un bean con annotations usamos la annotation `@Component`

```
@Component("thatSillyCoach")
public class TennisCoach implements Coach {

    @Override
    public String getDailyWorkout() {
        return "Practice your backhand volley";
    }
}

```

Dentro de la annotation lo que hacemos es aportar el bean Id (`thatSillyCoach`).

Y finalmente con el bean Id lo que podemos hacer es obtener el bean mediante el context de la misma forma que se hacia con archivo de configuraciones

```
Coach theCoach = context.getBean(thatSillyCoach, Coach.class);
```

Si en la definición de la annotation component no proporcionamos un bean Id el Id por default se crea con el nombre de la clase pero con la primer letra en minúscula.

## Spring Autowiring injection

En el caso de annotations inyectamos dependencias mediante autowiring. Spring busca por una clase que coincida con la propiedad indicada, ya sea por clase o por interface. Una vez que encuentra un match automáticamente lo inyecta.

Si spring nota que alguna inyección de, por ejemplo FortuneService en Coach, lo que hace es escanear todas las clases con `@Component` y si alguna implementa la interface FortuneService automáticamente la inyecta.

Como ocurría en la inyección por archivo de configuración con autowiring también hay tres tipos de inyección: Inyección de constructor, inyección de setter e Inyección de campos.

### Inyección en constructor

Para esto debemos definir un bean que implemente la interfaz que queremos inyectar.

```
 @Component
 public class HappyFortuneService implements FortuneService {

     public String getFortune(){
         return "Today is your lucky day!";
     }
 }
```

En la clase a la cual le inyectamos la dependencia tenemos que agregar una variable de instancia que sea del mismo tipo que la interfaz que queremos inyectar. Y agregamos un constructor que defina esa variable.
Para que haga el autowiring tenemos que agregar en el constructor la annotations `@Autowired`

```
@Component
public class TennisCoach implements Coach {
    private FortuneService fortuneService;

    @Autowired
    public TennisCoach(FortuneService theFortuneService){
        fortuneService = theFortuneService;
    }
}
```

A partir de spring 4.3, si el bean definido solo posee un constructor y este requiere inyección de dependencias el `@Autowired` no es necesario. Pero si hay varios constructores debemos especificar cual usar con la annotation.

### Inyección en setter

Este caso es similar al autowiring por constructor solo que aca usamos un constructor vacío o que no requiere inyección de dependencias, y añadimos un método setter para el fortuneService. Este método setter es al que debemos hacer autowiring.

```
@Component
public class TennisCoach implements Coach {
    private FortuneService fortuneService;

    public TennisCoach() {

    }

    @Autowired
    public void setFortuneService(FortuneService theFortuneService){
        fortuneService = theFortuneService;
    }
}
```
