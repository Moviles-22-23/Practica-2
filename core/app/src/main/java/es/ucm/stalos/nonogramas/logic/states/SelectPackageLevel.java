package es.ucm.stalos.nonogramas.logic.states;


//TODO: Hacer toda la lógica de los paquetes

// LÓGICA: Crear una interfaz que nos muestre las categorías y niveles disponibles, diferenciando
// entre bloqueados (no pueden jugarse) y desbloqueados (podemos volver a acceder a ellos en
// cualquier momento).

// LÓGICA CON DATOS GUARDADOS: Consiste en un conjunto de categorías y niveles en los que
// únicamente podremos avanzar al siguiente si hemos completado el nivel actual. Los niveles y
// categorías en este modo son fijos. Para poder tener este modo tenemos que guardar cuantos niveles
// ha desbloqueado el jugador hasta el momento

// GRAFICOS: En estos niveles del modo historia, deben representarse imágenes pixeladas (una
//abeja, una casa, un árbol, un faro, etc.)

// CATEGORÍAS POR DIFICULTAD: ada categoría tendrá X niveles de una misma dificultad / tamaño, y
// según avance el jugador completando los niveles de la categoría actual se desbloquearan nuevas
// categorías con mayor dificultad / tamaño de los tableros.
// EJEMPLO: Categoría fácil, 20 con tableros de 5x5. Al completar los 20 niveles se desbloquea la
// categoría intermedia con 20 niveles de 10x10.

public class SelectPackageLevel {
}
