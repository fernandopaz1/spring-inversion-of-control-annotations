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

No necesariamente tiene que ser un método setter para poder hacer el autowiring, podemos hacerlo con cualquier tipo de método.

### Inyección de campos

De esta forma podemos inyectar dependencias a valores de las clases directamente incluso si son valores privados (usando reflexión). Las ventajas son que la inyección es aplicada directamente al campo y que no son necesarios métodos setter.

En este caso la annotation `@Autowired` va sobre el campo que queremos inyectar sin necesidad de agregar un setter.

```
@Component
public class TennisCoach implements Coach {
    @Autowired
    private FortuneService fortuneService;

    public TennisCoach() {

    }
    ...
}
```

Si tenemos multiples implementaciones compatibles con el autowiring nos dará excepción ya que para inyectar dependencias esta debe estar determinada univocamente. Esto lo hacemos con la annotation `@Qualifier` y le pasamos como parámetro el BeanId. La annotation `@Qualifier` puede usarse en cualquier tipo de inyección.

@Component
public class TennisCoach implements Coach {
@Autowired
@Qualifier("happyFortuneService")
private FortuneService fortuneService;

    public TennisCoach() {

    }
    ...

}

```

En el caso especifico de inyección por constructor el `@Qualifier` no va sobre el constructor sino que como modificador del parámetro:

@Component
public class TennisCoach implements Coach {

    private FortuneService fortuneService;

    @Autowired
    public TennisCoach( @Qualifier("happyFortuneService") FortuneService theFortuneService){
        fortuneService = theFortuneService;
    }
    ...
}
```

Esto se debe a que de esta forma el constructor puede inyectar mas de una dependencia.
Es importante también aclarar que si el bean tiene un nombre con las dos primeras letras en mayúscula spring no transforma el nombre para crear el beanId por lo que el BeanId es el mismo que el nombre de la clase.

## Ciclo de vida con annotations

### Scopes

Podemos especificar el scope que tiene cada bean con la annotation `@scope` pasando como parámetro el valor de scope deseado.

### PostConstruct y PreDestroy

Para especificar que métodos llamar al crear o eliminar un objeto usamos las annotations `@PostConstruct` y `@PreDestroy` respectivamente.
En caso de que estemos en java 9 o versiones posteriores es necesario hacer unos pasos extras ya que javax.annotation fue removido del classpath por defecto:

<details>
 <summary>Solucón</summary>

1. Download the javax.annotation-api-1.3.2.jar from

https://search.maven.org/remotecontent?filepath=javax/annotation/javax.annotation-api/1.3.2/javax.annotation-api-1.3.2.jar

2. Copy the JAR file to the lib folder of your project

---

Use the following steps to add it to your Java Build Path.

3. Right-click your project, select Properties

4. On left-hand side, click Java Build Path

5. In top-center of dialog, click Libraries

6. Click Classpath and then Click Add JARs ...

7. Navigate to the JAR file <your-project>/lib/javax.annotation-api-1.3.2.jar

8. Click OK then click Apply and Close

Eclipse will perform a rebuild of your project and it will resolve the related build errors.

</details>

# Configuración de spring con código Java

Hay otra forma de configurar el contenedor de spring que es usando solamente código java sin archivos xml.

Para esto usamos una clase de configuración que tiene que tener las annotations `@Configuration` que indica que es la clase de configuración y `ComponentScan` al cual le pasamos un parámetro para indicar que paquete debe escanear.

```

@Configuration
@ComponentScan("com.luv2code.springdemo")
public class SportsConfig {

}

```

Para obtener el contexto ahora lo que hacemos es instanciar el contexto con la clase `AnnotationConfigApplicationContext` de pasando como parámetro la clase de configuración

```
AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SportsConfig.class);

```

Una ventaja de este forma de configurar el spring container es que dentro de la clase podemos crear beans sin la necesidad de haber implementado annotations en las clases. Esto lo hacemos con la annotation `@Bean` sobre un método, el nombre del método pasara a ser el BeanId y el valor que devuelve el el Bean que queremos recuperar para usar en el programa.

```
@Configuration
@ComponentScan("com.luv2code.springdemo")
public class SportsConfig {

    @Bean
    public Coach swimCoach(){
        SwimCoach mySwimCoach = new SwimCoach();
        return mySwimCoach;
    }

}

```

En este caso no estamos usando component scan estamos directamente instanciando la clase.

Es importante notar que los beans son singleton es decir la primera vez que se ejecute va a crear un nuevo objeto que quedara registrado en el spring container. Si luego se quiere acceder a este bean la annotation intercepta la llamada en caso de que ya este creado devuelve la referencia en memoria y no ejecuta el código del método.
